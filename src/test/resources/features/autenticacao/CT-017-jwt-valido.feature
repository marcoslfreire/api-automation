@CT-017
@autenticacao
Feature: Autorização com JWT

  Scenario: Acessar operação protegida com JWT válido
    Given que estou autenticado como administrador
    When acesso uma operação protegida
    Then a operação deve ser autorizada
