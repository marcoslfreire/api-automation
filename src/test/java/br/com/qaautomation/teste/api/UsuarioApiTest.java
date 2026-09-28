//package br.com.qaautomation.teste.api;
//
//import br.com.qaautomation.teste.config.TestDataFactory;
//import br.com.qaautomation.teste.config.UserData;
//import org.junit.jupiter.api.Test;
//
//import static org.hamcrest.Matchers.equalTo;
//
//class UsuarioApiTest {
//
//    private final UsuarioApi usuarioApi = new UsuarioApi();
//
//    @Test
//    void deveCriarUsuarioComSucesso() {
//
//        UserData usuario = TestDataFactory.criarUsuario();
//
//        usuarioApi.criarUsuario(usuario)
//                .then()
//                .statusCode(201)
//                .body("message", equalTo("Cadastro realizado com sucesso"));
//    }
//}