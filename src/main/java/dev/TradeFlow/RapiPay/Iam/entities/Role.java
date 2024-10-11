package dev.TradeFlow.RapiPay.Iam.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.TradeFlow.RapiPay.Iam.deserializers.RoleDeserializer;
import dev.TradeFlow.RapiPay.Iam.valueobjects.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = RoleDeserializer.class)
public class Role {
    private Roles name;
}