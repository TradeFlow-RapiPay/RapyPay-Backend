package dev.TradeFlow.RapiPay.Shared.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import dev.TradeFlow.RapiPay.WalletManagement.entities.Bill;
import dev.TradeFlow.RapiPay.WalletManagement.repositories.BillRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BillDeserializer extends JsonDeserializer<Bill> {

    @Autowired
    private BillRepository billRepository;

    @Override
    public Bill deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String billId = p.getText();
        return billRepository.findById(new ObjectId(billId)).orElse(null);
    }
}