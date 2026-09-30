package br.com.qaautomation.teste.steps;

import br.com.qaautomation.teste.api.AutenticacaoApi;
import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.AuthRequest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import br.com.qaautomation.teste.context.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import br.com.qaautomation.teste.config.ApiConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AutenticacaoSteps {

    private final ScenarioContext context;
    private final UsuarioApi usuarioApi;
    private final AutenticacaoApi autenticacaoApi;

    public AutenticacaoSteps(ScenarioContext context) {
        this.context = context;
        this.usuarioApi = new UsuarioApi();
        this.autenticacaoApi = new AutenticacaoApi();
    }

    @Given("que possuo um usuário administrador cadastrado")
    public void quePossuoUmUsuarioAdministradorCadastrado() {

        UserData usuario = TestDataFactory.criarUsuario();

        context.setUsuario(usuario);

        context.setResponse(
                usuarioApi.criarUsuario(usuario)
        );

        context.getResponse()
                .then()
                .statusCode(201);
    }

    @When("realizo o login com suas credenciais")
    public void realizoOLoginComSuasCredenciais() {

        context.setResponse(
                autenticacaoApi.login(
                        context.getUsuario()
                )
        );
    }

    @Then("o login deve ser realizado com sucesso")
    public void oLoginDeveSerRealizadoComSucesso() {

        context.getResponse()
                .then()
                .statusCode(200);
    }

    @Then("um token JWT deve ser retornado")
    public void umTokenJwtDeveSerRetornado() {

        String token =
                context.getResponse()
                        .then()
                        .extract()
                        .path("authorization");

        org.junit.jupiter.api.Assertions.assertNotNull(token);
        org.junit.jupiter.api.Assertions.assertTrue(
                token.startsWith("Bearer ")
        );

        context.setToken(token);
    }

    @When("realizo o login com uma senha inválida")
    public void realizoOLoginComUmaSenhaInvalida() {

        context.setResponse(
                autenticacaoApi.login(
                        context.getUsuario().getEmail(),
                        "senha-incorreta"
                )
        );
    }

    @Then("o login deve ser rejeitado")
    public void oLoginDeveSerRejeitado() {

        context.getResponse()
                .then()
                .statusCode(401)
                .body(
                        "message",
                        equalTo("Email e/ou senha inválidos")
                );
    }

    @Given("que estou autenticado como administrador")
    public void queEstouAutenticadoComoAdministrador() {

        UserData usuario = TestDataFactory.criarUsuario();

        context.setUsuario(usuario);

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        String token =
                autenticacaoApi.login(usuario)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("authorization");

        org.junit.jupiter.api.Assertions.assertNotNull(token);

        context.setToken(token);
    }

    @When("acesso uma operação protegida")
    public void acessoUmaOperacaoProtegida() {

        String nomeProduto =
                "Produto JWT " + System.currentTimeMillis();

        context.setResponse(
                AuthRequest
                        .comToken(context.getToken())
                        .contentType("application/json")
                        .body("""
                                {
                                    "nome": "%s",
                                    "preco": 100,
                                    "descricao": "Produto criado para validar autenticacao JWT",
                                    "quantidade": 1
                                }
                                """.formatted(nomeProduto))
                        .when()
                        .post("/produtos")
        );
    }

    @Then("a operação deve ser autorizada")
    public void aOperacaoDeveSerAutorizada() {

        context.getResponse()
                .then()
                .statusCode(201)
                .body(
                        "message",
                        equalTo("Cadastro realizado com sucesso")
                )
                .body("_id", notNullValue());
    }

    @Given("que possuo um usuário administrador")
    public void quePossuoUmUsuarioAdministrador() {

        UserData usuario = TestDataFactory.criarUsuario();

        context.setUsuario(usuario);

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);
    }

    @When("acesso uma operação protegida sem autenticação")
    public void acessoUmaOperacaoProtegidaSemAutenticacao() {

        context.setResponse(
                io.restassured.RestAssured
                        .given()
                        .baseUri(ApiConfig.getBaseUrl())
                        .contentType("application/json")
                        .body("""
                                {
                                    "nome": "Produto sem JWT",
                                    "preco": 100,
                                    "descricao": "Teste sem autenticacao",
                                    "quantidade": 1
                                }
                                """)
                        .when()
                        .post("/produtos")
        );
    }

    @Then("a operação deve ser rejeitada")
    public void aOperacaoDeveSerRejeitada() {

        context.getResponse()
                .then()
                .statusCode(401);
    }

    @Given("que possuo um token JWT inválido")
    public void quePossuoUmTokenJwtInvalido() {

        context.setToken("Bearer token-invalido");
    }

    @Then("a operação deve ser rejeitada por falta de permissão")
    public void aOperacaoDeveSerRejeitadaPorFaltaDePermissao() {

        context.getResponse()
                .then()
                .statusCode(403);
    }

    @Given("que estou autenticado como usuário não administrador")
    public void queEstouAutenticadoComoUsuarioNaoAdministrador() {

        UserData usuario = new UserData(
                "QA Usuario",
                "qa-nao-admin-" + System.currentTimeMillis() + "@teste.com",
                "123456",
                "false"
        );

        context.setUsuario(usuario);

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        String token =
                autenticacaoApi.login(usuario)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("authorization");

        org.junit.jupiter.api.Assertions.assertNotNull(token);

        context.setToken(token);
    }

    @When("acesso uma operação exclusiva para administradores")
    public void acessoUmaOperacaoExclusivaParaAdministradores() {

        String nomeProduto =
                "Produto Admin " + System.currentTimeMillis();

        context.setResponse(
                AuthRequest
                        .comToken(context.getToken())
                        .contentType("application/json")
                        .body("""
                                {
                                    "nome": "%s",
                                    "preco": 100,
                                    "descricao": "Teste de permissao administrativa",
                                    "quantidade": 1
                                }
                                """.formatted(nomeProduto))
                        .when()
                        .post("/produtos")
        );
    }
}
