package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bank;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.BankRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> getBankById(ObjectId id) {
        return bankRepository.findById(id);
    }

    public Bank insertBank(Bank bank) {
        return bankRepository.insert(bank);
    }

    public Optional<Bank> updateBank(ObjectId id, Bank bank) {
        Optional<Bank> existingBank = bankRepository.findBankById(id);
        if (existingBank.isPresent()) {
            bank.setId(id);
            return Optional.of(bankRepository.save(bank));
        } else {
            return Optional.empty();
        }
    }

    public void deleteBank(ObjectId id) {
        bankRepository.deleteById(id);
    }
}
