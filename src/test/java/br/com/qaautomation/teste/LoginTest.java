package br.com.qaautomation.teste;

import br.com.qaautomation.teste.api.AutenticacaoApi;
import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Autenticação")
@Feature("Login")
class LoginTest {

    private final UsuarioApi usuarioApi = new UsuarioApi();
    private final AutenticacaoApi autenticacaoApi = new AutenticacaoApi();

    @Test
    @Story("Realizar login com credenciais válidas")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que um usuário administrador cadastrado consegue realizar login e receber um token JWT.")
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
    @Story("Impedir login com senha incorreta")
    @Severity(SeverityLevel.NORMAL)
    @Description("Valida que a API rejeita a autenticação quando o usuário informa uma senha incorreta.")
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