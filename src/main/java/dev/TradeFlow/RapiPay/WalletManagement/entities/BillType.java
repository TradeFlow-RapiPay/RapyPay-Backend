package dev.TradeFlow.RapiPay.WalletManagement.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.TradeFlow.RapiPay.Shared.entities.RoleDeserializer;
import dev.TradeFlow.RapiPay.WalletManagement.valueobjects.BillTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "billTypes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = RoleDeserializer.class)
public class BillType {
    private BillTypes name;
}
