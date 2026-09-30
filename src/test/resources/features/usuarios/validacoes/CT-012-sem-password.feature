@CT-012
@usuarios
Feature: Validação da senha

  Scenario: Impedir cadastro sem password
    Given que possuo dados de usuário sem password
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o password é obrigatório
