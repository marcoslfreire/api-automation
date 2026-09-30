@CT-013
@usuarios
Feature: Validação do administrador

  Scenario: Impedir cadastro sem administrador
    Given que possuo dados de usuário sem administrador
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o administrador é obrigatório
