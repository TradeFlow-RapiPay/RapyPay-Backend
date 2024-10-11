package dev.TradeFlow.RapiPay.WalletManagement.controllers;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.services.WalletService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Wallet>> getAllWallets() {
        return new ResponseEntity<>(walletService.getAllWallets(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Wallet>> getWalletById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(walletService.getWalletById(id), HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<Wallet> insertWallet(@RequestBody Wallet wallet) {
        Wallet insertedWallet = walletService.insertWallet(wallet);
        return new ResponseEntity<>(insertedWallet, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Wallet> updateWallet(@PathVariable ObjectId id, @RequestBody Wallet wallet) {
        Optional<Wallet> updatedWallet = walletService.updateWallet(id, wallet);
        return updatedWallet.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable ObjectId id) {
        walletService.deleteWallet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/calculateAndUpdate/{id}")
    public ResponseEntity<Wallet> calculateAndUpdateWallet(@PathVariable ObjectId id) {
        Optional<Wallet> updatedWallet = walletService.calculateAndUpdateWallet(id);
        return updatedWallet.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}