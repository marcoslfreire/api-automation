package br.com.qaautomation.teste;

import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Usuários")
@Feature("Validação de cadastro")
class UserValidationTest extends BaseTest {

    @Test
    @Story("Impedir cadastro com e-mail inválido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro quando o usuário informa um endereço de e-mail em formato inválido.")
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
    @Story("Impedir cadastro com e-mail duplicado")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro de um usuário quando o endereço de e-mail informado já está cadastrado.")
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
    @Story("Impedir cadastro sem nome")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro quando o campo nome não é informado.")
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
    @Story("Impedir cadastro sem e-mail")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro quando o campo e-mail não é informado.")
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
    @Story("Impedir cadastro sem senha")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro quando o campo password não é informado.")
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
    @Story("Impedir cadastro sem administrador")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro quando o campo administrador não é informado.")
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
    @Story("Impedir cadastro com administrador inválido")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita o cadastro quando o campo administrador recebe um valor diferente de true ou false.")
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