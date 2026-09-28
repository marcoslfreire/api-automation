package br.com.qaautomation.teste;

import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.BaseTest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class UserTest extends BaseTest {

    private final UsuarioApi usuarioApi = new UsuarioApi();

    @Test
    void deveListarUsuariosComSucesso() {

        usuarioApi.listarUsuarios()
                .then()
                .statusCode(200)
                .body("quantidade", greaterThanOrEqualTo(0))
                .body("usuarios", notNullValue());
    }

    @Test
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
    void deveRetornarErroAoBuscarUsuarioInexistente() {

        usuarioApi.buscarUsuarioPorId("ZZZZZZZZZZZZZZZZ")
                .then()
                .statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }


    @Test
    void deveCriarUsuarioAoAtualizarIdInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";
        UserData usuario = TestDataFactory.criarUsuario();

        usuarioApi.atualizarUsuario(idInexistente, usuario)
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"));
    }

    @Test
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
    void deveRetornarNenhumRegistroAoExcluirUsuarioInexistente() {

        String idInexistente = "ZZZZZZZZZZZZZZZZ";

        usuarioApi.excluirUsuario(idInexistente)
                .then()
                .statusCode(200)
                .body("message", equalTo("Nenhum registro excluído"));
    }
}
