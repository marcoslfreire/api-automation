package br.com.qaautomation.teste;

import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

class LoginTest extends BaseTest {

    @Test
    void deveRealizarLoginComSucesso() {

        UserData user = TestDataFactory.criarUsuario();

        String createUserBody = """
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

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(createUserBody)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201);

        String loginBody = """
            {
                "email": "%s",
                "password": "%s"
            }
            """.formatted(
                user.getEmail(),
                user.getPassword()
        );

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("authorization", notNullValue());
    }

    @Test
    void deveImpedirLoginComSenhaIncorreta() {

        UserData usuario = TestDataFactory.criarUsuario();

        String bodyCadastro = """
            {
                "nome": "%s",
                "email": "%s",
                "password": "%s",
                "administrador": "%s"
            }
            """.formatted(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getAdministrador()
        );

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(bodyCadastro)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201);

        String bodyLogin = """
            {
                "email": "%s",
                "password": "senha-incorreta"
            }
            """.formatted(usuario.getEmail());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(bodyLogin)
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }


}