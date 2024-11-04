package dev.TradeFlow.RapiPay.Iam.controllers.response;

public record SignInResponse(String userId, String role,String username, String token) {
}
