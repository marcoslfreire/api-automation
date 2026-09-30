@CT-014
@usuarios
Feature: Validação do administrador

  Scenario: Impedir cadastro com administrador inválido
    Given que possuo dados de usuário com administrador inválido
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o administrador deve ser true ou false
