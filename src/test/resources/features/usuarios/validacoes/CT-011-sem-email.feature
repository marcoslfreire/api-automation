@CT-011
@usuarios
Feature: Validação do email obrigatório

  Scenario: Impedir cadastro sem email
    Given que possuo dados de usuário sem email
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o email é obrigatório
