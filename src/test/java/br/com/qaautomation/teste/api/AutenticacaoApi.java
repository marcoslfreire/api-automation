package br.com.qaautomation.teste.api;

import br.com.qaautomation.teste.config.ApiConfig;
import br.com.qaautomation.teste.config.UserData;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AutenticacaoApi {

    private final String baseUrl;

    public AutenticacaoApi() {
        this.baseUrl = ApiConfig.getBaseUrl();
    }

    public Response login(UserData usuario) {
        return Allure.step(
                "Realizar login com usuário cadastrado",
                () -> executarLogin(usuario.getEmail(), usuario.getPassword())
        );
    }

    public Response login(String email, String password) {
        return Allure.step(
                "Realizar login com credenciais informadas",
                () -> executarLogin(email, password)
        );
    }

    private Response executarLogin(String email, String password) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """.formatted(email, password))
                .when()
                .post("/login");
    }
}