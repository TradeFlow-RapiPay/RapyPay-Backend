package dev.TradeFlow.RapiPay.WalletManagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Document(collection = "wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    private ObjectId id;

    private String walletName;

    private Bank bank;

    private String description;

    @DocumentReference
    private List<Bill> billIds;

    private Date closingDate;
}
