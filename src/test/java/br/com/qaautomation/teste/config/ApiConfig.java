package br.com.qaautomation.teste.config;

public final class ApiConfig {

    private static final String DEFAULT_BASE_URL = "https://serverest.dev";

    private ApiConfig() {
    }

    public static String getBaseUrl() {
        return System.getProperty("baseUrl", DEFAULT_BASE_URL);
    }
}