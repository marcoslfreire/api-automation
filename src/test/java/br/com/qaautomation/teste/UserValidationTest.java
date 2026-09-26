package br.com.qaautomation.teste;

import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UserValidationTest extends BaseTest {

    @Test
    void deveImpedirCadastroComEmailInvalido() {

        String body = """
                {
                    "nome": "QA Automation",
                    "email": "email-invalido",
                    "password": "123456",
                    "administrador": "true"
                }
                """;

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("email", equalTo("email deve ser um email válido"));


    }


    @Test
    void deveImpedirCadastroComEmailDuplicado() {

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

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201);

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"));
    }


    @Test
    void deveImpedirCadastroSemNome() {

        String body = """
                {
                    "email": "qa-%s@teste.com",
                    "password": "123456",
                    "administrador": "true"
                }
                """.formatted(System.currentTimeMillis());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("nome", equalTo("nome é obrigatório"));
    }

    @Test
    void deveImpedirCadastroSemEmail() {

        String body = """
                {
                    "nome": "QA Automation",
                    "password": "123456",
                    "administrador": "true"
                }
                """;

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("email", equalTo("email é obrigatório"));
    }



    @Test
    void deveImpedirCadastroSemPassword() {

        String body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "administrador": "true"
                }
                """.formatted(System.currentTimeMillis());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("password", equalTo("password é obrigatório"));
    }



    @Test
    void deveImpedirCadastroSemAdministrador() {

        String body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "password": "123456"
                }
                """.formatted(System.currentTimeMillis());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("administrador", equalTo("administrador é obrigatório"));
    }

    @Test
    void deveImpedirCadastroComAdministradorInvalido() {

        String body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "password": "123456",
                    "administrador": "valor-invalido"
                }
                """.formatted(System.currentTimeMillis());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(400)
                .body("administrador", equalTo("administrador deve ser 'true' ou 'false'"));
    }





}