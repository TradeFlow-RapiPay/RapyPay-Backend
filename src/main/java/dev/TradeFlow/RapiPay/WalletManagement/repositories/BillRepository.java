package dev.TradeFlow.RapiPay.WalletManagement.repositories;

import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends MongoRepository<Bill, ObjectId> {
    Optional<Bill> findBillById(ObjectId id);
}
