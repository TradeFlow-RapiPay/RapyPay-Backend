package dev.TradeFlow.RapiPay.WalletManagement.controllers;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.services.BillService;
import dev.TradeFlow.RapiPay.WalletManagement.services.WalletService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    private final BillService billService;
    private final WalletService walletService;

    @Autowired
    public BillController(BillService billService, WalletService walletService) {
        this.billService = billService;
        this.walletService = walletService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Bill>> getAllBills() {
        return new ResponseEntity<>(billService.getAllBills(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Optional<Bill> getBillById(@PathVariable ObjectId id) {
        return billService.getBillById(id);
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<Bill>> getBillsByWalletId(@PathVariable ObjectId walletId) {
        List<Bill> bills = billService.getBillsByWalletId(walletId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PostMapping("/insert/{walletId}/{userId}")
    public ResponseEntity<Bill> insertBill(@PathVariable String walletId, @PathVariable String userId,@RequestBody Bill bill) {
        try {
            bill.setWalletId(new ObjectId(walletId));
            Bill insertedBill = billService.insertBill(new ObjectId(walletId), new ObjectId(userId),bill);
            walletService.addBillToWallet(insertedBill.getWalletId(), insertedBill.getId());
            return new ResponseEntity<>(insertedBill, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable ObjectId id, @RequestBody Bill bill) {
        Optional<Bill> updatedBill = billService.updateBill(id, bill);
        return updatedBill.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable ObjectId id) {
        billService.deleteBill(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable ObjectId userId) {
        List<Bill> bills = billService.getBillsByUserId(userId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }
}