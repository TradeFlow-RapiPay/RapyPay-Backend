package dev.TradeFlow.RapiPay.WalletManagement.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.TradeFlow.RapiPay.Shared.entities.ObjectIdSerializer;
import dev.TradeFlow.RapiPay.WalletManagement.valueobjects.MoneyTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "wallets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String walletName;

    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId bank;

    @JsonSerialize(contentUsing = ObjectIdSerializer.class)
    private List<ObjectId> billsList = new ArrayList<>();

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date closingDate;

    private float totalDiscount;

    private float totalNetValue;

    private float tcea;

    private MoneyTypes moneyType;

    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId userId;

    public void addBill(ObjectId billId) {
        billsList.add(billId);
    }
}