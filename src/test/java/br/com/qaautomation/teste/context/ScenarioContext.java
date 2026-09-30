package br.com.qaautomation.teste.context;

import br.com.qaautomation.teste.config.UserData;
import io.restassured.response.Response;

public class ScenarioContext {

    private UserData usuario;
    private UserData usuarioAtualizado;
    private Response response;
    private String usuarioId;
    private String token;
    private String email;
    private String password;
    private String requestBody;

    public UserData getUsuario() {
        return usuario;
    }

    public void setUsuario(UserData usuario) {
        this.usuario = usuario;
    }

    public UserData getUsuarioAtualizado() {
        return usuarioAtualizado;
    }

    public void setUsuarioAtualizado(UserData usuarioAtualizado) {
        this.usuarioAtualizado = usuarioAtualizado;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}