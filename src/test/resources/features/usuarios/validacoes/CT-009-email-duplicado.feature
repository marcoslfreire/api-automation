@CT-009
@usuarios
Feature: Validação de email duplicado

  Scenario: Impedir cadastro com email já utilizado
    Given que possuo um usuário cadastrado
    When tento cadastrar novamente o mesmo usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o email já está sendo usado
