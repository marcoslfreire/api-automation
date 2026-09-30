@CT-019
@autenticacao
Feature: Autorização com JWT

  Scenario: Acessar operação protegida com JWT inválido
    Given que possuo um token JWT inválido
    When acesso uma operação protegida
    Then a operação deve ser rejeitada
