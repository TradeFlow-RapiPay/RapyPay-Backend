package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.Iam.repositories.UserRepository;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Bank;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.WalletRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
            wallet.setBillsList(new ArrayList<>());
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
        System.out.println("Calculating data for wallet: " + wallet.getBillsList());
        applyDiscount(wallet);
        walletRepository.save(wallet);
    }

    private void applyDiscount(Wallet wallet) {
        wallet.setTotalDiscount(0.0f);
        wallet.setTotalNetValue(0.0f);

        for (ObjectId billId : wallet.getBillsList()) {
            Bill bill = billService.getBillById(billId).orElseThrow(() -> new IllegalArgumentException("Bill not found"));
            float valorPresente = calcularValorPresente(bill.getNetValue(), bill.getEmissionDate(), bill.getDueDate(), wallet.getClosingDate(), getTea(wallet));
            float descuento = bill.getNetValue() - valorPresente;
            wallet.setTotalDiscount(wallet.getTotalDiscount() + descuento);
            wallet.setTotalNetValue(wallet.getTotalNetValue() + valorPresente);
        }

        wallet.setTcea(calculateTcea(wallet));
    }

    private float calcularValorPresente(float valorNominal, Date fechaEmision, Date fechaVencimiento, Date fechaCierre, float tea) {
        // Convertir TEA a tasa efectiva diaria
        float ted = (float) (Math.pow(1 + tea, 1.0 / 360) - 1);

        // Calcular días entre fecha de cierre y vencimiento
        long diasAlVencimiento = TimeUnit.DAYS.convert(fechaVencimiento.getTime() - fechaCierre.getTime(), TimeUnit.MILLISECONDS);

        // Calcular valor presente
        return (float) (valorNominal / Math.pow(1 + ted, diasAlVencimiento));
    }

    private float getTea(Wallet wallet) {
        Bank bankEntity = bankService.getBankById(wallet.getBank()).orElseThrow(() -> new IllegalArgumentException("Bank or TEA is null"));
        return bankEntity.getTea();
    }

    private float calculateTcea(Wallet wallet) {
        // Implement the logic to calculate TCEA based on wallet's data
        // This is a placeholder implementation
        return wallet.getTotalDiscount() / wallet.getTotalNetValue() * 100;
    }
}