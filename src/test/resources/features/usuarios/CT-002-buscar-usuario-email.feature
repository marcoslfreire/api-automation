@CT-002
@usuarios
Feature: Busca de usuário por email

  Scenario: Buscar usuário cadastrado pelo email
    Given que possuo um usuário cadastrado
    When busco o usuário pelo email
    Then o usuário deve ser encontrado
