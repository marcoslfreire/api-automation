package br.com.qaautomation.teste.config;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public final class AuthRequest {

    private AuthRequest() {
    }

    public static RequestSpecification comToken(String token) {
        return given()
                .baseUri(ApiConfig.getBaseUrl())
                .header("Authorization", token);
    }
}