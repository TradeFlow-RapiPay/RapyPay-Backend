package dev.TradeFlow.RapiPay.WalletManagement.services;

import dev.TradeFlow.RapiPay.Iam.repositories.UserRepository;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.WalletRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Optional<Wallet> getWalletById(ObjectId id) {
        return walletRepository.findById(id);
    }

    public Wallet insertWallet(Wallet wallet) {
        if (userRepository.findById(wallet.getUserId()).isPresent()) {
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

    public Optional<Wallet> calculateAndUpdateWallet(ObjectId id) {
        return walletRepository.findById(id).map(wallet -> {
            wallet.calculateAndUpdateData();
            return walletRepository.save(wallet);
        });
    }

    public List<Wallet> getWalletsByUserId(ObjectId userId) {
        return walletRepository.findWalletsByUserId(userId);
    }
}