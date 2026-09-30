@CT-020
@autenticacao
Feature: Autorização por perfil

  Scenario: Usuário sem permissão acessa operação administrativa
    Given que estou autenticado como usuário não administrador
    When acesso uma operação exclusiva para administradores
    Then a operação deve ser rejeitada por falta de permissão
