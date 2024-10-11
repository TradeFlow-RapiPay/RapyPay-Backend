package dev.TradeFlow.RapiPay.WalletManagement.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.TradeFlow.RapiPay.Shared.entities.ObjectIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "banks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String bankName;

    private Float tea;

    private Float tcea;

    private String additionalInfo;
}