package dev.TradeFlow.RapiPay.Iam.controllers.response;

import org.bson.types.ObjectId;

public record SignInResponse(ObjectId userId, String username, String token) {
}
