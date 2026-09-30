package br.com.qaautomation.teste.exploratorio;

import br.com.qaautomation.teste.config.ApiConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Investigação exploratória do comportamento de rate limit da API ServeRest.
 *
 * <p>Este teste NÃO faz parte da suíte funcional principal do projeto.
 * Ele deve ser executado explicitamente quando for necessário investigar
 * o comportamento de limitação de requisições da API.</p>
 *
 * <h2>Execução</h2>
 *
 * <p>Por padrão, o teste realiza apenas 1 requisição:</p>
 *
 * <pre>
 * mvn test -Dtest=RateLimitExploratoryTest
 * </pre>
 *
 * <p>A quantidade de requisições pode ser parametrizada através da
 * propriedade {@code rateLimit.requests}.</p>
 *
 * <h3>Exemplo com 110 requisições:</h3>
 *
 * <pre>
 * mvn test -Dtest=RateLimitExploratoryTest -DrateLimit.requests=110
 * </pre>
 *
 * <h3>Exemplo com 500 requisições:</h3>
 *
 * <pre>
 * mvn test -Dtest=RateLimitExploratoryTest -DrateLimit.requests=500
 * </pre>
 *
 * <h2>O que é observado</h2>
 *
 * <ul>
 *     <li>Quantidade total de requisições realizadas;</li>
 *     <li>Quantidade de respostas HTTP 200;</li>
 *     <li>Quantidade de respostas HTTP 429 (Too Many Requests);</li>
 *     <li>Quantidade de outros códigos HTTP;</li>
 *     <li>Headers relacionados a rate limit;</li>
 *     <li>Header {@code Retry-After}, quando disponível.</li>
 * </ul>
 *
 * <h2>Interpretação</h2>
 *
 * <p>O teste é exploratório e não considera a ausência de HTTP 429 como
 * falha funcional. O objetivo é observar e registrar o comportamento
 * apresentado pela API durante a investigação.</p>
 *
 * <p>Essa abordagem evita transformar uma limitação que não foi
 * reproduzida de forma consistente em um requisito funcional
 * automatizado.</p>
 */
public class RateLimitExploratoryTest {

    private static final int DEFAULT_REQUESTS = 1;

    @Test
    void investigarRateLimit() {

        int quantidadeRequisicoes = Integer.parseInt(
                System.getProperty(
                        "rateLimit.requests",
                        String.valueOf(DEFAULT_REQUESTS)
                )
        );

        int status200 = 0;
        int status429 = 0;
        int outrosStatus = 0;

        System.out.println();
        System.out.println("========================================");
        System.out.println(" INVESTIGAÇÃO DE RATE LIMIT");
        System.out.println("========================================");
        System.out.println("Quantidade configurada: " + quantidadeRequisicoes);
        System.out.println();

        for (int i = 1; i <= quantidadeRequisicoes; i++) {

            Response response = given()
                    .baseUri(ApiConfig.getBaseUrl())
                    .when()
                    .get("/usuarios");

            int statusCode = response.statusCode();

            if (statusCode == 200) {
                status200++;
            } else if (statusCode == 429) {
                status429++;
            } else {
                outrosStatus++;
            }

            System.out.printf(
                    "Requisição %d/%d -> HTTP %d%n",
                    i,
                    quantidadeRequisicoes,
                    statusCode
            );

            if (i == 1) {
                System.out.println();
                System.out.println("Headers relacionados a rate limit:");

                imprimirHeader(response, "X-RateLimit-Limit");
                imprimirHeader(response, "X-RateLimit-Remaining");
                imprimirHeader(response, "X-RateLimit-Reset");
                imprimirHeader(response, "Retry-After");

                System.out.println();
            }
        }

        System.out.println("========================================");
        System.out.println(" RESULTADO");
        System.out.println("========================================");
        System.out.println("Total de requisições: " + quantidadeRequisicoes);
        System.out.println("HTTP 200: " + status200);
        System.out.println("HTTP 429: " + status429);
        System.out.println("Outros status: " + outrosStatus);
        System.out.println("========================================");
    }

    private void imprimirHeader(Response response, String nomeHeader) {

        String valor = response.getHeader(nomeHeader);

        if (valor == null) {
            System.out.println(nomeHeader + ": não identificado");
        } else {
            System.out.println(nomeHeader + ": " + valor);
        }
    }
}