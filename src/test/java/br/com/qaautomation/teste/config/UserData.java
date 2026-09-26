package br.com.qaautomation.teste.config;

public class UserData {

    private final String nome;
    private final String email;
    private final String password;
    private final String administrador;

    public UserData(String nome, String email, String password, String administrador) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.administrador = administrador;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAdministrador() {
        return administrador;
    }


}
