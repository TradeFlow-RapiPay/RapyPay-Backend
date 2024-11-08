package dev.TradeFlow.RapiPay.Iam.repositories;

import dev.TradeFlow.RapiPay.Iam.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(ObjectId id);
}
