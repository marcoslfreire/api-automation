package br.com.qaautomation.teste.legacy;

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
import static org.hamcrest.Matchers.equalTo;

@Epic("Usuários")
@Feature("Criação de usuário")
class UserCreationTest extends BaseTest {

    @Test
    @Story("Criar usuário com dados válidos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida a criação de um novo usuário com dados válidos e confirma os dados cadastrados por meio de uma consulta posterior.")
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