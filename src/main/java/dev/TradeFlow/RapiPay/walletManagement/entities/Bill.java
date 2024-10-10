package dev.TradeFlow.RapiPay.walletManagement.entities;

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
    private ObjectId id;

    private  String billNumber;

    private Float netValue;

    private Date emissionDate;

    private Date dueDate;
}
