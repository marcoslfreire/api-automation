package br.com.qaautomation.teste.steps;

import br.com.qaautomation.teste.config.ApiLogFilter;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import static io.restassured.RestAssured.replaceFiltersWith;

public class Hooks {

    @Before
    public void antesDoCenario(Scenario scenario) {

        System.out.println();
        System.out.println("============================================================");
        System.out.println("CENÁRIO: " + scenario.getName());
        System.out.println("============================================================");

        replaceFiltersWith(new ApiLogFilter());
    }

    @After
    public void depoisDoCenario(Scenario scenario) {

        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (scenario.isFailed()) {
            System.out.println("CENÁRIO FINALIZADO: FALHOU");
        } else {
            System.out.println("CENÁRIO FINALIZADO: PASSOU");
        }

        System.out.println("CENÁRIO: " + scenario.getName());
        System.out.println("============================================================");
        System.out.println();
    }
}