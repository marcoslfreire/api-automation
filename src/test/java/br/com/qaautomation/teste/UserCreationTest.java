package br.com.qaautomation.teste;

import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class UserCreationTest extends BaseTest {

    @Test
    void deveCriarUsuarioComSucesso() {

        UserData user = TestDataFactory.criarUsuario();

        String body = """
            {
                "nome": "%s",
                "email": "%s",
                "password": "%s",
                "administrador": "%s"
            }
            """.formatted(
                user.getNome(),
                user.getEmail(),
                user.getPassword(),
                user.getAdministrador()
        );

        String id = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .extract()
                .path("_id");

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/usuarios/{id}", id)
                .then()
                .statusCode(200)
                .body("nome", equalTo(user.getNome()))
                .body("email", equalTo(user.getEmail()))
                .body("administrador", equalTo(user.getAdministrador()));
    }
}