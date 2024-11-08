package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.BillRepository;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.WalletRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public BillService(BillRepository billRepository, WalletRepository walletRepository) {
        this.billRepository = billRepository;
        this.walletRepository = walletRepository;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(ObjectId id) {
        return billRepository.findById(id);
    }

    public List<Bill> getBillsByWalletId(ObjectId walletId) {
        return billRepository.findBillsByWalletId(walletId);
    }

    public Bill insertBill(ObjectId walletId, ObjectId userId, Bill bill) {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        return billRepository.save(bill);
    }

    public Optional<Bill> updateBill(ObjectId id, Bill bill) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()) {
            bill.setId(id);
            return Optional.of(billRepository.save(bill));
        } else {
            return Optional.empty();
        }
    }

    public void deleteBill(ObjectId id) {
        Optional<Bill> billOpt = billRepository.findById(id);
        if (billOpt.isPresent()) {
            Bill bill = billOpt.get();
            ObjectId walletId = bill.getWalletId();
            billRepository.deleteById(id);

            walletRepository.findById(walletId).ifPresent(wallet -> {
                wallet.getBillsList().remove(id);
                walletRepository.save(wallet);
            });
        }
    }

    public List<Bill> getBillsByUserId(ObjectId userId) {
        return billRepository.findBillsByUserId(userId);
    }
}