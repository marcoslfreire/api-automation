@CT-010
@usuarios
Feature: Validação do nome

  Scenario: Impedir cadastro sem nome
    Given que possuo dados de usuário sem nome
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o nome é obrigatório
