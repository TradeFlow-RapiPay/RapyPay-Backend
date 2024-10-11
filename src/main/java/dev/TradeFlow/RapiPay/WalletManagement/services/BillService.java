package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.BillRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(ObjectId id) {
        return billRepository.findBillById(id);
    }

    public Bill insertBill(Bill bill) {
        return billRepository.insert(bill);
    }

    public Optional<Bill> updateBill(ObjectId id, Bill bill) {
        Optional<Bill> existingBill = billRepository.findBillById(id);
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
}