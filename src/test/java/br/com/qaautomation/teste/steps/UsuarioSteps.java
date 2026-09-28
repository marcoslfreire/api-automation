package br.com.qaautomation.teste.steps;

import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import br.com.qaautomation.teste.config.ApiConfig;

public class UsuarioSteps {

//    private static final String BASE_URL = "https://serverest.dev";

    private UserData usuario;
    private Response response;
    private String idUsuario;
    private String body;

    @Given("que possuo os dados de um novo usuário")
    public void quePossuoOsDadosDeUmNovoUsuario() {

        usuario = TestDataFactory.criarUsuario();

        body = criarBodyUsuario(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getAdministrador()
        );
    }

    @When("realizo o cadastro do usuário")
    public void realizoOCadastroDoUsuario() {

        response = cadastrarUsuario(body);
    }

    @Then("o usuário deve ser criado com sucesso")
    public void oUsuarioDeveSerCriadoComSucesso() {

        response.then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue());

        idUsuario = response.path("_id");
    }

    @And("o usuário criado deve ser encontrado pelo id")
    public void oUsuarioCriadoDeveSerEncontradoPeloId() {

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .when()
                .get("/usuarios/{id}", idUsuario);

        response.then()
                .statusCode(200)
                .body("nome", equalTo(usuario.getNome()))
                .body("email", equalTo(usuario.getEmail()))
                .body("administrador", equalTo(usuario.getAdministrador()));
    }

    @And("o usuário foi cadastrado")
    public void oUsuarioFoiCadastrado() {

        response = cadastrarUsuario(body);

        response.then()
                .statusCode(201);

        idUsuario = response.path("_id");
    }

    @When("busco o usuário pelo email")
    public void buscoOUsuarioPeloEmail() {

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .queryParam("email", usuario.getEmail())
                .when()
                .get("/usuarios");
    }

    @Then("o usuário deve ser encontrado")
    public void oUsuarioDeveSerEncontrado() {

        response.then()
                .statusCode(200)
                .body("quantidade", equalTo(1))
                .body("usuarios[0].email", equalTo(usuario.getEmail()));
    }

    @Given("que possuo um id de usuário inexistente")
    public void quePossuoUmIdDeUsuarioInexistente() {

        idUsuario = "ZZZZZZZZZZZZZZZZ";
    }

    @When("busco o usuário pelo id")
    public void buscoOUsuarioPeloId() {

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .when()
                .get("/usuarios/{id}", idUsuario);
    }

    @Then("a API deve informar que o usuário não foi encontrado")
    public void aApiDeveInformarQueOUsuarioNaoFoiEncontrado() {

        response.then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @When("atualizo os dados do usuário")
    public void atualizoOsDadosDoUsuario() {

        String novoNome = "QA Automation Atualizado";

        body = criarBodyUsuario(
                novoNome,
                usuario.getEmail(),
                usuario.getPassword(),
                "false"
        );

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/usuarios/{id}", idUsuario);
    }

    @Then("o usuário deve ser atualizado com sucesso")
    public void oUsuarioDeveSerAtualizadoComSucesso() {

        response.then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));
    }

    @And("os dados atualizados devem ser retornados")
    public void osDadosAtualizadosDevemSerRetornados() {

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .when()
                .get("/usuarios/{id}", idUsuario);

        response.then()
                .statusCode(200)
                .body("nome", equalTo("QA Automation Atualizado"))
                .body("email", equalTo(usuario.getEmail()))
                .body("administrador", equalTo("false"));
    }

    @When("atualizo um usuário com esse id")
    public void atualizoUmUsuarioComEsseId() {

        String email = "qa-put-" + System.currentTimeMillis() + "@teste.com";

        body = criarBodyUsuario(
                "QA Automation",
                email,
                "123456",
                "true"
        );

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/usuarios/{id}", idUsuario);
    }

    @Then("um novo usuário deve ser criado")
    public void umNovoUsuarioDeveSerCriado() {

        response.then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }

    @When("excluo o usuário")
    public void excluoOUsuario() {

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .when()
                .delete("/usuarios/{id}", idUsuario);
    }

    @Then("o usuário deve ser excluído com sucesso")
    public void oUsuarioDeveSerExcluidoComSucesso() {

        response.then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
    }

    @And("o usuário excluído não deve ser encontrado")
    public void oUsuarioExcluidoNaoDeveSerEncontrado() {

        response = given()
                .baseUri(ApiConfig.getBaseUrl())
                .when()
                .get("/usuarios/{id}", idUsuario);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Then("nenhum registro deve ser excluído")
    public void nenhumRegistroDeveSerExcluido() {

        response.then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"));
    }

    @Given("que possuo dados de usuário com email inválido")
    public void quePossuoDadosDeUsuarioComEmailInvalido() {

        body = criarBodyUsuario(
                "QA Automation",
                "email-invalido",
                "123456",
                "true"
        );
    }

    @Given("que possuo dados de usuário sem nome")
    public void quePossuoDadosDeUsuarioSemNome() {

        body = """
                {
                    "email": "qa-%s@teste.com",
                    "password": "123456",
                    "administrador": "true"
                }
                """.formatted(System.currentTimeMillis());
    }

    @Given("que possuo dados de usuário sem email")
    public void quePossuoDadosDeUsuarioSemEmail() {

        body = """
                {
                    "nome": "QA Automation",
                    "password": "123456",
                    "administrador": "true"
                }
                """;
    }

    @Given("que possuo dados de usuário sem password")
    public void quePossuoDadosDeUsuarioSemPassword() {

        body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "administrador": "true"
                }
                """.formatted(System.currentTimeMillis());
    }

    @Given("que possuo dados de usuário sem administrador")
    public void quePossuoDadosDeUsuarioSemAdministrador() {

        body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "password": "123456"
                }
                """.formatted(System.currentTimeMillis());
    }

    @Given("que possuo dados de usuário com administrador inválido")
    public void quePossuoDadosDeUsuarioComAdministradorInvalido() {

        body = """
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "password": "123456",
                    "administrador": "valor-invalido"
                }
                """.formatted(System.currentTimeMillis());
    }

    @When("tento cadastrar novamente o mesmo usuário")
    public void tentoCadastrarNovamenteOMesmoUsuario() {

        response = cadastrarUsuario(body);
    }

    @Then("o cadastro deve ser rejeitado")
    public void oCadastroDeveSerRejeitado() {

        response.then()
                .statusCode(400);
    }

    @And("a API deve informar que o email é inválido")
    public void aApiDeveInformarQueOEmailEInvalido() {

        response.then()
                .body("email", equalTo("email deve ser um email válido"));
    }

    @And("a API deve informar que o email já está sendo usado")
    public void aApiDeveInformarQueOEmailJaEstaSendoUsado() {

        response.then()
                .body("message", equalTo("Este email já está sendo usado"));
    }

    @And("a API deve informar que o nome é obrigatório")
    public void aApiDeveInformarQueONomeEObrigatorio() {

        response.then()
                .body("nome", equalTo("nome é obrigatório"));
    }

    @And("a API deve informar que o email é obrigatório")
    public void aApiDeveInformarQueOEmailEObrigatorio() {

        response.then()
                .body("email", equalTo("email é obrigatório"));
    }

    @And("a API deve informar que o password é obrigatório")
    public void aApiDeveInformarQueOPasswordEObrigatorio() {

        response.then()
                .body("password", equalTo("password é obrigatório"));
    }

    @And("a API deve informar que o administrador é obrigatório")
    public void aApiDeveInformarQueOAdministradorEObrigatorio() {

        response.then()
                .body("administrador", equalTo("administrador é obrigatório"));
    }

    @And("a API deve informar que o administrador é inválido")
    public void aApiDeveInformarQueOAdministradorEInvalido() {

        response.then()
                .body(
                        "administrador",
                        equalTo("administrador deve ser 'true' ou 'false'")
                );
    }

    private Response cadastrarUsuario(String body) {

        return given()
                .baseUri(ApiConfig.getBaseUrl())
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/usuarios");
    }

    private String criarBodyUsuario(
            String nome,
            String email,
            String password,
            String administrador
    ) {

        return """
                {
                    "nome": "%s",
                    "email": "%s",
                    "password": "%s",
                    "administrador": "%s"
                }
                """.formatted(
                nome,
                email,
                password,
                administrador
        );
    }
}