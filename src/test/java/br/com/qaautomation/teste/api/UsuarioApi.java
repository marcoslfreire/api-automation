package br.com.qaautomation.teste.api;

import br.com.qaautomation.teste.config.ApiConfig;
import br.com.qaautomation.teste.config.UserData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
//"Eu refatorei a camada de acesso à API, criando uma abstração UsuarioApi para centralizar os endpoints e métodos HTTP.
// Os testes continuam responsáveis pelas asserções, enquanto a camada de API fica responsável pela comunicação com o serviço."
public class UsuarioApi {

    private final String baseUrl;

    public UsuarioApi() {
        this.baseUrl = ApiConfig.getBaseUrl();
    }

    public Response listarUsuarios() {
        return given()
                .baseUri(baseUrl)
                .when()
                .get("/usuarios");
    }

    public Response buscarUsuarioPorId(String id) {
        return given()
                .baseUri(baseUrl)
                .when()
                .get("/usuarios/{id}", id);
    }

    public Response buscarUsuarioPorEmail(String email) {
        return given()
                .baseUri(baseUrl)
                .queryParam("email", email)
                .when()
                .get("/usuarios");
    }

    public Response criarUsuario(UserData usuario) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .post("/usuarios");
    }

    public Response atualizarUsuario(String id, UserData usuario) {
        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(usuario)
                .when()
                .put("/usuarios/{id}", id);
    }

    public Response excluirUsuario(String id) {
        return given()
                .baseUri(baseUrl)
                .when()
                .delete("/usuarios/{id}", id);
    }
}