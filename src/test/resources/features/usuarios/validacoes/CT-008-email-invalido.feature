@CT-008
@usuarios
Feature: Validação de email

  Scenario: Impedir cadastro com email inválido
    Given que possuo dados de usuário com email inválido
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o email é inválido
