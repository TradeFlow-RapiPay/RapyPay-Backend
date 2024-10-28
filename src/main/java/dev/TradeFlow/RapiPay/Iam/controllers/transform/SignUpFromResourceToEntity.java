package dev.TradeFlow.RapiPay.Iam.controllers.transform;

import dev.TradeFlow.RapiPay.Iam.controllers.resource.UserRequest;
import dev.TradeFlow.RapiPay.Iam.entities.User;
import dev.TradeFlow.RapiPay.Iam.entities.Role;
import dev.TradeFlow.RapiPay.Iam.valueobjects.Roles;

import java.util.List;

public class SignUpFromResourceToEntity {
    public static User transform(UserRequest userRequest) {
        return new User(null, userRequest.username(), userRequest.password(), List.of(new Role(Roles.valueOf(userRequest.role()))));
    }
}
