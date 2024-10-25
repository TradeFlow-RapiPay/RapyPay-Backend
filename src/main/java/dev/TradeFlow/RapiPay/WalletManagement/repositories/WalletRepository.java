package dev.TradeFlow.RapiPay.WalletManagement.repositories;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Wallet;
import dev.TradeFlow.RapiPay.WalletManagement.valueobjects.MoneyTypes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, ObjectId> {
    Optional<Wallet> findWalletById(ObjectId id);

    List<Wallet> findWalletsByUserId(ObjectId userId);

    default boolean isValidMoneyType(MoneyTypes moneyType) {
        for (MoneyTypes type : MoneyTypes.values()) {
            if (type == moneyType) {
                return true;
            }
        }
        return false;
    }
}