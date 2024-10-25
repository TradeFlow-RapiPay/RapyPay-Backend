package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.BillRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
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

    public Bill insertBill(ObjectId walletId, Bill bill) {
        if (!billRepository.isValidBillType(bill.getBillType())) {
            throw new IllegalArgumentException("Invalid bill type");
        }
        bill.setWalletId(walletId);
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
        billRepository.deleteById(id);
    }

    public List<Bill> getBillsByUserId(ObjectId userId) {
        return billRepository.findBillsByUserId(userId);
    }
}