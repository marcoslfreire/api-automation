package br.com.qaautomation.teste;
import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTest extends BaseTest{

    @Test
    void deveListarUsuariosComSucesso() {

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("quantidade", greaterThanOrEqualTo(0))
                .body("usuarios", notNullValue());
    }
    @Test
    void deveBuscarUsuarioPorEmail() {

        UserData usuario = TestDataFactory.criarUsuario();

        String body = """
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
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201);

        given()
                .baseUri(BASE_URL)
                .queryParam("email", usuario.getEmail())
                .when()
                .get("/usuarios")
                .then()
                .statusCode(200)
                .body("quantidade", equalTo(1))
                .body("usuarios[0].email", equalTo(usuario.getEmail()));
    }

    @Test
    void deveRetornarErroAoBuscarUsuarioInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/usuarios/{id}", idInexistente)
                .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }


    @Test
    void deveCriarUsuarioAoAtualizarIdInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";

        String body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-put-%s@teste.com",
                    "password": "123456",
                    "administrador": "true"
                }
                """.formatted(System.currentTimeMillis());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/usuarios/{id}", idInexistente)
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }


    @Test
    void deveAtualizarUsuarioComSucesso() {

        UserData usuario = TestDataFactory.criarUsuario();

        String bodyCriacao = """
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

        String id = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(bodyCriacao)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract()
                .path("_id");

        String novoNome = "QA Automation Atualizado";

        String bodyAtualizacao = """
                {
                    "nome": "%s",
                    "email": "%s",
                    "password": "%s",
                    "administrador": "false"
                }
                """.formatted(novoNome, usuario.getEmail(), usuario.getPassword());

        given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(bodyAtualizacao)
                .when()
                .put("/usuarios/{id}", id)
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/usuarios/{id}", id)
                .then()
                .statusCode(200)
                .body("nome", equalTo(novoNome))
                .body("email", equalTo(usuario.getEmail()))
                .body("administrador", equalTo("false"));
    }

    @Test
    void deveExcluirUsuarioComSucesso() {

        UserData usuario = TestDataFactory.criarUsuario();

        String body = """
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

        String id = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract()
                .path("_id");

        given()
                .baseUri(BASE_URL)
                .when()
                .delete("/usuarios/{id}", id)
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));

        given()
                .baseUri(BASE_URL)
                .when()
                .get("/usuarios/{id}", id)
                .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    void deveRetornarNenhumRegistroAoExcluirUsuarioInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";

        given()
                .baseUri(BASE_URL)
                .when()
                .delete("/usuarios/{id}", idInexistente)
                .then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"));
    }
}
