package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.Iam.repositories.UserRepository;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Bank;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.WalletRepository;
import dev.TradeFlow.RapiPay.WalletManagement.valueobjects.MoneyTypes;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final BankService bankService;
    private final BillService billService;

    @Autowired
    public WalletService(WalletRepository walletRepository, UserRepository userRepository, BankService bankService, BillService billService) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.bankService = bankService;
        this.billService = billService;
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Optional<Wallet> getWalletById(ObjectId id) {
        return walletRepository.findById(id);
    }

    public Wallet insertWallet(Wallet wallet) {
        if (userRepository.findById(wallet.getUserId()).isPresent()) {
            if (!walletRepository.isValidMoneyType(wallet.getMoneyType())) {
                throw new IllegalArgumentException("Invalid MoneyType");
            }
            return walletRepository.save(wallet);
        } else {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    public Optional<Wallet> updateWallet(ObjectId id, Wallet wallet) {
        if (userRepository.findById(wallet.getUserId()).isPresent()) {
            return walletRepository.findById(id).map(existingWallet -> {
                wallet.setId(id);
                return walletRepository.save(wallet);
            });
        } else {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    public void addBillToWallet(ObjectId walletId, ObjectId billId) {
        Optional<Wallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();
            if (wallet.getBillsList() == null) {
                wallet.setBillsList(new ArrayList<>());
            }
            wallet.getBillsList().add(billId);
            walletRepository.save(wallet);
        }
    }

    public void deleteWallet(ObjectId id) {
        walletRepository.deleteById(id);
    }

    public List<Wallet> getWalletsByUserId(ObjectId userId) {
        return walletRepository.findWalletsByUserId(userId);
    }

    public void calculateAndUpdateData(ObjectId walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        applyDiscount(wallet);
        walletRepository.save(wallet);
    }

    private void applyDiscount(Wallet wallet) {
        wallet.setTotalDiscount(0.0f);
        wallet.setTotalNetValue(0.0f);

        for (ObjectId billId : wallet.getBillsList()) {
            Bill bill = billService.getBillById(billId).orElseThrow(() -> new IllegalArgumentException("Bill not found"));
            float personalizedTea = calculatePersonalizedTea(wallet, bill);
            float discount = calculateDiscount(wallet, bill, personalizedTea);
            wallet.setTotalDiscount(wallet.getTotalDiscount() + discount);
            wallet.setTotalNetValue(wallet.getTotalNetValue() + bill.getNetValue());
        }
    }

    private float calculatePersonalizedTea(Wallet wallet, Bill bill) {
        Bank bankEntity = bankService.getBankById(wallet.getBank()).orElseThrow(() -> new IllegalArgumentException("Bank or TEA is null"));
        float bankTea = bankEntity.getTea();
        long days = (wallet.getClosingDate().getTime() - bill.getEmissionDate().getTime()) / (1000 * 60 * 60 * 24);
        return bankTea * (days / 360.0f);
    }

    private float calculateDiscount(Wallet wallet, Bill bill, float personalizedTea) {
        float discount = 0.0f;
        long days = (wallet.getClosingDate().getTime() - bill.getDueDate().getTime()) / (1000 * 60 * 60 * 24);

        if (days > 0) {
            discount = bill.getNetValue() * personalizedTea * (days / 360.0f);
        }

        return discount;
    }
}