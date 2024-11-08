// src/main/java/dev/TradeFlow/RapiPay/WalletManagement/entities/Bill.java
package dev.TradeFlow.RapiPay.WalletManagement.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.TradeFlow.RapiPay.Shared.entities.ObjectIdSerializer;
import dev.TradeFlow.RapiPay.WalletManagement.valueobjects.BillTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "bills")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String billNumber;

    private String addressee;

    private BillTypes billType;

    private float netValue;

    private float discount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date emissionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dueDate;

    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId walletId;

    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId userId;
}