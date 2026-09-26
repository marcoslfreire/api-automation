package br.com.qaautomation.teste.config;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class ApiLogFilter implements Filter {

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext context) {

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("HTTP REQUEST");
        System.out.println("--------------------------------------------------");
        System.out.println(requestSpec.getMethod() + " " + requestSpec.getURI());

        Response response = context.next(requestSpec, responseSpec);

        System.out.println();
        System.out.println("HTTP RESPONSE");
        System.out.println("--------------------------------------------------");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body:");

        String body = response.getBody().asPrettyString();

        if (body.contains("\"authorization\"")) {
            body = body.replaceAll(
                    "(\"authorization\"\\s*:\\s*\")[^\"]+(\")",
                    "$1Bearer ***$2"
            );
        }

        System.out.println(body);
        System.out.println("--------------------------------------------------");

        return response;
    }
}