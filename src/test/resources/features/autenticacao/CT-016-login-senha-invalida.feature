@CT-016
@autenticacao
Feature: Login

  Scenario: Impedir login com senha inválida
    Given que possuo um usuário administrador cadastrado
    When realizo o login com uma senha inválida
    Then o login deve ser rejeitado
