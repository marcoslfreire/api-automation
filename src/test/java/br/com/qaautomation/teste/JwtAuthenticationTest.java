package br.com.qaautomation.teste;

import br.com.qaautomation.teste.api.AutenticacaoApi;
import br.com.qaautomation.teste.api.UsuarioApi;
import br.com.qaautomation.teste.config.AuthRequest;
import br.com.qaautomation.teste.config.TestDataFactory;
import br.com.qaautomation.teste.config.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Autenticação")
@Feature("JWT")
public class JwtAuthenticationTest {

    private final AutenticacaoApi autenticacaoApi = new AutenticacaoApi();
    private final UsuarioApi usuarioApi = new UsuarioApi();

    @Test
    @Story("Criar usuário administrador")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida a criação de um usuário administrador que será utilizado no fluxo de autenticação.")
    void deveCriarUsuarioAdministrador() {

        logEtapa("1. CRIANDO USUÁRIO ADMINISTRADOR");

        UserData usuario = TestDataFactory.criarUsuario();

        System.out.println("Enviando POST /usuarios...");
        System.out.println("E-mail: " + usuario.getEmail());

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        System.out.println("Usuário administrador criado com sucesso.");
    }

    @Test
    @Story("Autenticar usuário e obter JWT")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que um usuário administrador cadastrado consegue realizar login e receber um token JWT no campo authorization.")
    void deveAutenticarUsuarioAdministrador() {

        logEtapa("2. AUTENTICANDO USUÁRIO E OBTENDO JWT");

        UserData usuario = TestDataFactory.criarUsuario();

        System.out.println("Criando usuário administrador...");

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        System.out.println("Usuário criado.");
        System.out.println("Realizando POST /login...");

        String token =
                autenticacaoApi.login(usuario)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("authorization");

        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));

        System.out.println("Login realizado com sucesso.");
        System.out.println("JWT recebido com sucesso.");
        System.out.println("Token: Bearer ***");
    }

    @Test
    @Story("Utilizar JWT em operação protegida")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Valida o fluxo completo de autenticação JWT, desde a criação e autenticação do usuário até a utilização do token em uma operação protegida de criação e consulta de produto.")
    void deveUtilizarJwtEmOperacaoProtegida() {

        logEtapa("3. FLUXO COMPLETO DE AUTENTICAÇÃO JWT");

        UserData usuario = TestDataFactory.criarUsuario();

        // ============================================================
        // ETAPA 1 - CRIAR USUÁRIO ADMINISTRADOR
        // ============================================================

        System.out.println("[1/4] Criando usuário administrador...");

        usuarioApi.criarUsuario(usuario)
                .then()
                .statusCode(201);

        System.out.println("[1/4] Usuário administrador criado.");

        // ============================================================
        // ETAPA 2 - AUTENTICAR E OBTER JWT
        // ============================================================

        System.out.println();
        System.out.println("[2/4] Realizando login...");

        String token =
                autenticacaoApi.login(usuario)
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("authorization");

        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));

        System.out.println("[2/4] Login realizado.");
        System.out.println("[2/4] JWT recebido: Bearer ***");

        // ============================================================
        // ETAPA 3 - UTILIZAR JWT EM OPERAÇÃO PROTEGIDA
        // ============================================================

        System.out.println();
        System.out.println("[3/4] Criando produto utilizando JWT...");

        String nomeProduto = "Produto JWT " + System.currentTimeMillis();

        String idProduto =
                AuthRequest
                        .comToken(token)
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
                        .then()
                        .statusCode(201)
                        .body(
                                "message",
                                equalTo("Cadastro realizado com sucesso")
                        )
                        .body("_id", notNullValue())
                        .extract()
                        .path("_id");

        assertNotNull(idProduto);

        System.out.println("[3/4] Produto criado com sucesso.");
        System.out.println("[3/4] ID do produto: " + idProduto);

        // ============================================================
        // ETAPA 4 - VALIDAR PRODUTO CRIADO
        // ============================================================

        System.out.println();
        System.out.println("[4/4] Consultando produto criado...");

        AuthRequest
                .comToken(token)
                .contentType("application/json")
                .when()
                .get("/produtos/{id}", idProduto)
                .then()
                .statusCode(200)
                .body("_id", equalTo(idProduto))
                .body("nome", equalTo(nomeProduto))
                .body("preco", equalTo(100))
                .body(
                        "descricao",
                        equalTo("Produto criado para validar autenticacao JWT")
                )
                .body("quantidade", equalTo(1));

        System.out.println("[4/4] Produto consultado e validado com sucesso.");

        // ============================================================
        // FINALIZAÇÃO
        // ============================================================

        System.out.println();
        System.out.println("==================================================");
        System.out.println("FLUXO JWT CONCLUÍDO COM SUCESSO");
        System.out.println("==================================================");
    }

    private static void logEtapa(String mensagem) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(mensagem);
        System.out.println("==================================================");
        System.out.println();
    }
}