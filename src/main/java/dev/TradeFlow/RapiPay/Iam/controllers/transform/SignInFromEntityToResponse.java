package dev.TradeFlow.RapiPay.Iam.controllers.transform;

import dev.TradeFlow.RapiPay.Iam.controllers.response.SignInResponse;
import dev.TradeFlow.RapiPay.Iam.entities.User;

public class SignInFromEntityToResponse {
    public static SignInResponse transform(User user, String token) {
        return new SignInResponse(user.getId().toString(), user.getRoles().get(0).getName().toString(), user.getUsername(), token);
    }
}
