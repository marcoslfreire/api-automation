@CT-018
@autenticacao
Feature: Autorização com JWT

  Scenario: Acessar operação protegida sem JWT
    Given que possuo um usuário administrador
    When acesso uma operação protegida sem autenticação
    Then a operação deve ser rejeitada
