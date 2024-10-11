package dev.TradeFlow.RapiPay.WalletManagement.controllers;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.services.BillService;
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

    @Autowired
    private BillService billService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Bill>> getAllBills() {
        return new ResponseEntity<>(billService.getAllBills(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Bill>> getBillById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(billService.getBillById(id), HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<Bill> insertBill(@RequestBody Bill bill) {
        Bill insertedBill = billService.insertBill(bill);
        return new ResponseEntity<>(insertedBill, HttpStatus.CREATED);
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
}