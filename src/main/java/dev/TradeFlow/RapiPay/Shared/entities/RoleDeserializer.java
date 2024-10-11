package dev.TradeFlow.RapiPay.Shared.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import dev.TradeFlow.RapiPay.Iam.entities.Role;
import dev.TradeFlow.RapiPay.Iam.valueobjects.Roles;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String roleName = p.getText();
        Roles roleEnum = Roles.valueOf(roleName);
        return new Role(roleEnum);
    }
}