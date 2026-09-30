package br.com.qaautomation.teste.steps;

import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import br.com.qaautomation.teste.context.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsuarioSteps {

    private final ScenarioContext context;
    private final UsuarioApi usuarioApi;

    public UsuarioSteps(ScenarioContext context) {
        this.context = context;
        this.usuarioApi = new UsuarioApi();
    }

    @Given("que possuo os dados válidos de um novo usuário")
    public void quePossuoOsDadosValidosDeUmNovoUsuario() {
        UserData usuario = TestDataFactory.criarUsuario();
        context.setUsuario(usuario);
    }

    @Given("que possuo um usuário cadastrado")
    public void quePossuoUmUsuarioCadastrado() {
        UserData usuario = TestDataFactory.criarUsuario();

        context.setUsuario(usuario);

        context.setResponse(
                usuarioApi.criarUsuario(usuario)
        );

        context.setUsuarioId(
                context.getResponse()
                        .jsonPath()
                        .getString("_id")
        );
    }

    @When("realizo o cadastro do usuário")
    public void realizoOCadastroDoUsuario() {

        if (context.getRequestBody() != null) {
            context.setResponse(
                    usuarioApi.criarUsuario(
                            context.getRequestBody()
                    )
            );
            return;
        }

        context.setResponse(
                usuarioApi.criarUsuario(
                        context.getUsuario()
                )
        );
    }

    @Then("o usuário deve ser criado com sucesso")
    public void oUsuarioDeveSerCriadoComSucesso() {
        context.getResponse()
                .then()
                .statusCode(201)
                .body(
                        "message",
                        equalTo("Cadastro realizado com sucesso")
                )
                .body(
                        "_id",
                        notNullValue()
                );
    }

    @Then("o identificador do usuário deve ser retornado")
    public void oIdentificadorDoUsuarioDeveSerRetornado() {

        String usuarioId =
                context.getResponse()
                        .jsonPath()
                        .getString("_id");

        assertNotNull(usuarioId);
        assertFalse(usuarioId.isBlank());

        context.setUsuarioId(usuarioId);
    }

    @When("busco o usuário pelo email")
    public void buscoOUsuarioPeloEmail() {
        context.setResponse(
                usuarioApi.buscarUsuarioPorEmail(
                        context.getUsuario().getEmail()
                )
        );
    }

    @Then("o usuário deve ser encontrado")
    public void oUsuarioDeveSerEncontrado() {
        context.getResponse()
                .then()
                .statusCode(200)
                .body(
                        "quantidade",
                        equalTo(1)
                )
                .body(
                        "usuarios[0].email",
                        equalTo(
                                context.getUsuario().getEmail()
                        )
                );
    }

    @Given("que existem usuários cadastrados")
    public void queExistemUsuariosCadastrados() {
        UserData usuario = TestDataFactory.criarUsuario();

        context.setUsuario(usuario);

        context.setResponse(
                usuarioApi.criarUsuario(usuario)
        );

        context.getResponse()
                .then()
                .statusCode(201);
    }

    @When("consulto a lista de usuários")
    public void consultoAListaDeUsuarios() {
        context.setResponse(
                usuarioApi.listarUsuarios()
        );
    }

    @Then("a lista de usuários deve ser retornada com sucesso")
    public void aListaDeUsuariosDeveSerRetornadaComSucesso() {
        context.getResponse()
                .then()
                .statusCode(200)
                .body("quantidade", greaterThanOrEqualTo(0))
                .body("usuarios", notNullValue());
    }

    @Given("que possuo um ID de usuário inexistente")
    public void quePossuoUmIdDeUsuarioInexistente() {
        context.setUsuarioId(
                "ZZZZZZZZZZZZZZZZ"
        );
    }

    @When("busco o usuário pelo ID")
    public void buscoOUsuarioPeloId() {
        context.setResponse(
                usuarioApi.buscarUsuarioPorId(
                        context.getUsuarioId()
                )
        );
    }

    @Then("o usuário não deve ser encontrado")
    public void oUsuarioNaoDeveSerEncontrado() {
        context.getResponse()
                .then()
                .statusCode(400)
                .body(
                        "message",
                        equalTo("Usuário não encontrado")
                );
    }

    @When("atualizo os dados do usuário")
    public void atualizoOsDadosDoUsuario() {

        UserData usuarioAtualizado =
                new UserData(
                        "QA Automation Atualizado",
                        context.getUsuario().getEmail(),
                        context.getUsuario().getPassword(),
                        "false"
                );

        context.setUsuarioAtualizado(usuarioAtualizado);

        context.setResponse(
                usuarioApi.atualizarUsuario(
                        context.getUsuarioId(),
                        usuarioAtualizado
                )
        );
    }

    @Then("o usuário deve ser atualizado com sucesso")
    public void oUsuarioDeveSerAtualizadoComSucesso() {
        context.getResponse()
                .then()
                .statusCode(200)
                .body(
                        "message",
                        equalTo("Registro alterado com sucesso")
                );
    }

    @And("os dados atualizados devem ser retornados")
    public void osDadosAtualizadosDevemSerRetornados() {

        Response responseConsulta =
                usuarioApi.buscarUsuarioPorId(
                        context.getUsuarioId()
                );

        responseConsulta
                .then()
                .statusCode(200)
                .body(
                        "nome",
                        equalTo("QA Automation Atualizado")
                )
                .body(
                        "email",
                        equalTo(
                                context.getUsuario().getEmail()
                        )
                )
                .body(
                        "administrador",
                        equalTo("false")
                );
    }

    @When("atualizo um usuário utilizando esse ID")
    public void atualizoUmUsuarioUtilizandoEsseId() {

        String email =
                "qa-put-"
                        + System.currentTimeMillis()
                        + "@teste.com";

        UserData usuario =
                new UserData(
                        "QA Automation",
                        email,
                        "123456",
                        "true"
                );

        context.setUsuarioAtualizado(usuario);

        context.setResponse(
                usuarioApi.atualizarUsuario(
                        context.getUsuarioId(),
                        usuario
                )
        );
    }

    @Then("um novo usuário deve ser criado")
    public void umNovoUsuarioDeveSerCriado() {
        context.getResponse()
                .then()
                .statusCode(201)
                .body(
                        "message",
                        equalTo("Cadastro realizado com sucesso")
                )
                .body(
                        "_id",
                        notNullValue()
                );
    }

    @When("excluo o usuário")
    public void excluoOUsuario() {
        context.setResponse(
                usuarioApi.excluirUsuario(
                        context.getUsuarioId()
                )
        );
    }

    @Then("o usuário deve ser excluído com sucesso")
    public void oUsuarioDeveSerExcluidoComSucesso() {
        context.getResponse()
                .then()
                .statusCode(200)
                .body(
                        "message",
                        equalTo("Registro excluído com sucesso")
                );
    }

    @And("o usuário excluído não deve ser encontrado")
    public void oUsuarioExcluidoNaoDeveSerEncontrado() {

        Response responseConsulta =
                usuarioApi.buscarUsuarioPorId(
                        context.getUsuarioId()
                );

        responseConsulta
                .then()
                .statusCode(400)
                .body(
                        "message",
                        equalTo("Usuário não encontrado")
                );
    }

    @Then("nenhum registro deve ser excluído")
    public void nenhumRegistroDeveSerExcluido() {
        context.getResponse()
                .then()
                .statusCode(200)
                .body(
                        "message",
                        equalTo("Nenhum registro excluído")
                );
    }

    @Given("que possuo dados de usuário com email inválido")
    public void quePossuoDadosDeUsuarioComEmailInvalido() {

        context.setRequestBody(
                criarBodyUsuario(
                        "QA Automation",
                        "email-invalido",
                        "123456",
                        "true"
                )
        );
    }

    @Given("que possuo dados de usuário sem nome")
    public void quePossuoDadosDeUsuarioSemNome() {

        context.setRequestBody("""
                {
                    "email": "qa-%s@teste.com",
                    "password": "123456",
                    "administrador": "true"
                }
                """.formatted(
                System.currentTimeMillis()
        ));
    }

    @Given("que possuo dados de usuário sem email")
    public void quePossuoDadosDeUsuarioSemEmail() {

        context.setRequestBody("""
                {
                    "nome": "QA Automation",
                    "password": "123456",
                    "administrador": "true"
                }
                """);
    }

    @Given("que possuo dados de usuário sem password")
    public void quePossuoDadosDeUsuarioSemPassword() {

        context.setRequestBody("""
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "administrador": "true"
                }
                """.formatted(
                System.currentTimeMillis()
        ));
    }

    @Given("que possuo dados de usuário sem administrador")
    public void quePossuoDadosDeUsuarioSemAdministrador() {

        context.setRequestBody("""
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "password": "123456"
                }
                """.formatted(
                System.currentTimeMillis()
        ));
    }

    @Given("que possuo dados de usuário com administrador inválido")
    public void quePossuoDadosDeUsuarioComAdministradorInvalido() {

        context.setRequestBody("""
                {
                    "nome": "QA Automation",
                    "email": "qa-%s@teste.com",
                    "password": "123456",
                    "administrador": "valor-invalido"
                }
                """.formatted(
                System.currentTimeMillis()
        ));
    }

    @When("tento cadastrar novamente o mesmo usuário")
    public void tentoCadastrarNovamenteOMesmoUsuario() {

        context.setResponse(
                usuarioApi.criarUsuario(
                        context.getUsuario()
                )
        );
    }

    @Then("o cadastro deve ser rejeitado")
    public void oCadastroDeveSerRejeitado() {
        context.getResponse()
                .then()
                .statusCode(400);
    }

    @Then("os dados do usuário criado devem ser persistidos")
    public void osDadosDoUsuarioCriadoDevemSerPersistidos() {
        context.setResponse(
                usuarioApi.buscarUsuarioPorId(
                        context.getUsuarioId()
                )
        );

        context.getResponse()
                .then()
                .statusCode(200)
                .body("nome", equalTo(context.getUsuario().getNome()))
                .body("email", equalTo(context.getUsuario().getEmail()))
                .body("administrador", equalTo(context.getUsuario().getAdministrador()));
    }

    @And("a API deve informar que o email é inválido")
    public void aApiDeveInformarQueOEmailEInvalido() {
        context.getResponse()
                .then()
                .body(
                        "email",
                        equalTo("email deve ser um email válido")
                );
    }

    @And("a API deve informar que o email já está sendo usado")
    public void aApiDeveInformarQueOEmailJaEstaSendoUsado() {
        context.getResponse()
                .then()
                .body(
                        "message",
                        equalTo("Este email já está sendo usado")
                );
    }

    @And("a API deve informar que o nome é obrigatório")
    public void aApiDeveInformarQueONomeEObrigatorio() {
        context.getResponse()
                .then()
                .body(
                        "nome",
                        equalTo("nome é obrigatório")
                );
    }

    @And("a API deve informar que o email é obrigatório")
    public void aApiDeveInformarQueOEmailEObrigatorio() {
        context.getResponse()
                .then()
                .body(
                        "email",
                        equalTo("email é obrigatório")
                );
    }

    @And("a API deve informar que o password é obrigatório")
    public void aApiDeveInformarQueOPasswordEObrigatorio() {
        context.getResponse()
                .then()
                .body(
                        "password",
                        equalTo("password é obrigatório")
                );
    }

    @And("a API deve informar que o administrador é obrigatório")
    public void aApiDeveInformarQueOAdministradorEObrigatorio() {
        context.getResponse()
                .then()
                .body(
                        "administrador",
                        equalTo("administrador é obrigatório")
                );
    }

    @And("a API deve informar que o administrador deve ser true ou false")
    public void aApiDeveInformarQueOAdministradorEInvalido() {
        context.getResponse()
                .then()
                .body(
                        "administrador",
                        equalTo(
                                "administrador deve ser 'true' ou 'false'"
                        )
                );
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