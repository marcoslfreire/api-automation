package br.com.qaautomation.teste;

import br.com.qaautomation.teste.api.AutenticacaoApi;
import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

class LoginTest {

    private final UsuarioApi usuarioApi = new UsuarioApi();
    private final AutenticacaoApi autenticacaoApi = new AutenticacaoApi();

    @Test
    void deveRealizarLoginComSucesso() {

        UserData usuario = TestDataFactory.criarUsuario();

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        autenticacaoApi.login(usuario)
                .then()
                .statusCode(200)
                .body("authorization", notNullValue());
    }

    @Test
    void deveImpedirLoginComSenhaIncorreta() {

        UserData usuario = TestDataFactory.criarUsuario();

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        autenticacaoApi.login(
                        usuario.getEmail(),
                        "senha-incorreta"
                )
                .then()
                .statusCode(401)
                .body(
                        "message",
                        equalTo("Email e/ou senha inválidos")
                );
    }
}