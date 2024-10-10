package dev.TradeFlow.RapiPay.walletManagement.entities;

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
    private ObjectId id;

    private String bankName;

    private Float TEA;

    private Float TCEA;

    private String additionalInfo;
}
