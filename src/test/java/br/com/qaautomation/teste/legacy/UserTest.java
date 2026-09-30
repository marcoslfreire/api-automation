package br.com.qaautomation.teste.legacy;

import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

@Epic("Usuários")
@Feature("CRUD de usuários")
public class UserTest extends BaseTest {

    private final UsuarioApi usuarioApi = new UsuarioApi();

    @Test
    @Story("Listar usuários")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API permite consultar a lista de usuários e retorna uma resposta válida.")
    void deveListarUsuariosComSucesso() {

        usuarioApi.listarUsuarios()
                .then()
                .statusCode(200)
                .body("quantidade", greaterThanOrEqualTo(0))
                .body("usuarios", notNullValue());
    }

    @Test
    @Story("Buscar usuário por e-mail")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API permite localizar um usuário cadastrado utilizando seu endereço de e-mail.")
    void deveBuscarUsuarioPorEmail() {

        UserData usuario = TestDataFactory.criarUsuario();

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        usuarioApi.buscarUsuarioPorEmail(usuario.getEmail())
                .then()
                .statusCode(200)
                .body("quantidade", equalTo(1))
                .body("usuarios[0].email", equalTo(usuario.getEmail()));
    }

    @Test
    @Story("Buscar usuário inexistente")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API retorna erro ao tentar consultar um usuário utilizando um identificador inexistente.")
    void deveRetornarErroAoBuscarUsuarioInexistente() {

        usuarioApi.buscarUsuarioPorId("ZZZZZZZZZZZZZZZZ")
                .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @Story("Criar usuário ao atualizar ID inexistente")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida o comportamento da API ao enviar uma atualização utilizando um identificador de usuário inexistente.")
    void deveCriarUsuarioAoAtualizarIdInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";
        UserData usuario = TestDataFactory.criarUsuario();

        usuarioApi.atualizarUsuario(idInexistente, usuario)
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }

    @Test
    @Story("Atualizar usuário")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que um usuário cadastrado pode ser atualizado e que os novos dados persistem na consulta posterior.")
    void deveAtualizarUsuarioComSucesso() {

        UserData usuario = TestDataFactory.criarUsuario();

        String id = usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201)
                .extract()
                .path("_id");

        String novoNome = "QA Automation Atualizado";

        UserData usuarioAtualizado = new UserData(
                novoNome,
                usuario.getEmail(),
                usuario.getPassword(),
                "false"
        );

        usuarioApi.atualizarUsuario(id, usuarioAtualizado)
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));

        usuarioApi.buscarUsuarioPorId(id)
                .then()
                .statusCode(200)
                .body("nome", equalTo(novoNome))
                .body("email", equalTo(usuario.getEmail()))
                .body("administrador", equalTo("false"));
    }

    @Test
    @Story("Excluir usuário")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que um usuário cadastrado pode ser excluído e que a API não o encontra após a exclusão.")
    void deveExcluirUsuarioComSucesso() {

        UserData usuario = TestDataFactory.criarUsuario();

        String id = usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201)
                .extract()
                .path("_id");

        usuarioApi.excluirUsuario(id)
                .then()
                .statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));

        usuarioApi.buscarUsuarioPorId(id)
                .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    @Story("Excluir usuário inexistente")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API informa que nenhum registro foi excluído quando o identificador informado não existe.")
    void deveRetornarNenhumRegistroAoExcluirUsuarioInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";

        usuarioApi.excluirUsuario(idInexistente)
                .then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"));
    }
}
