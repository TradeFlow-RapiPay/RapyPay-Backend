package dev.TradeFlow.RapiPay.WalletManagement.controllers;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bank;
import dev.TradeFlow.RapiPay.WalletManagement.services.BankService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Bank>> getAllBanks() {
        return new ResponseEntity<>(bankService.getAllBanks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Bank>> getBankById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(bankService.getBankById(id), HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<Bank> insertBank(@RequestBody Bank bank) {
        Bank insertedBank = bankService.insertBank(bank);
        return new ResponseEntity<>(insertedBank, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Bank> updateBank(@PathVariable ObjectId id, @RequestBody Bank bank) {
        Optional<Bank> updatedBank = bankService.updateBank(id, bank);
        return updatedBank.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable ObjectId id) {
        bankService.deleteBank(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}