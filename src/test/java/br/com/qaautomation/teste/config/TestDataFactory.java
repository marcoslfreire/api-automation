package br.com.qaautomation.teste.config;

import java.util.UUID;

public class TestDataFactory {

    public static UserData criarUsuario() {

        String identificador = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        return new UserData(
                "QA Automation",
                "qa" + identificador + "@teste.com",
                "123456",
                "true"
        );
    }
}