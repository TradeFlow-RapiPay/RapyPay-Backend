package dev.TradeFlow.RapiPay.WalletManagement.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.TradeFlow.RapiPay.Shared.entities.ObjectIdSerializer;
import dev.TradeFlow.RapiPay.Shared.entities.BankDeserializer;
import dev.TradeFlow.RapiPay.Shared.entities.BillDeserializer;
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
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String walletName;

    @DocumentReference
    @JsonDeserialize(using = BankDeserializer.class)
    private Bank bank;

    @DocumentReference
    @JsonDeserialize(contentUsing = BillDeserializer.class)
    private List<Bill> billsList;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date closingDate;

    private float totalDiscount;
    private float totalNetValue;

    public void applyDiscount() {
        totalDiscount = 0.0f;
        totalNetValue = 0.0f;

        for (Bill bill : billsList) {
            float personalizedTea = calculatePersonalizedTea(bill);
            float discount = calculateDiscount(bill, personalizedTea);
            totalDiscount += discount;
            totalNetValue += bill.getNetValue();
        }
    }

    private float calculatePersonalizedTea(Bill bill) {
        float bankTea = bank.getTea();
        long days = (closingDate.getTime() - bill.getEmissionDate().getTime()) / (1000 * 60 * 60 * 24);
        return bankTea * (days / 360.0f);
    }

    private float calculateDiscount(Bill bill, float personalizedTea) {
        float discount = 0.0f;
        long days = (closingDate.getTime() - bill.getDueDate().getTime()) / (1000 * 60 * 60 * 24);

        if (days > 0) {
            discount = bill.getNetValue() * personalizedTea * (days / 360.0f);
        }

        return discount;
    }

    public void calculateAndUpdateData() {
        applyDiscount();
    }
}