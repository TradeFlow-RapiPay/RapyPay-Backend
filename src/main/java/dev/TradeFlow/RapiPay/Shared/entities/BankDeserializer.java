package dev.TradeFlow.RapiPay.Shared.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Bank;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.BankRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BankDeserializer extends JsonDeserializer<Bank> {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public Bank deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String bankId = p.getText();
        return bankRepository.findById(new ObjectId(bankId)).orElse(null);
    }
}