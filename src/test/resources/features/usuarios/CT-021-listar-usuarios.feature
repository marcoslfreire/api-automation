@CT-021
@usuarios
Feature: Listagem de usuários

  Scenario: Listar usuários cadastrados
    Given que existem usuários cadastrados
    When consulto a lista de usuários
    Then a lista de usuários deve ser retornada com sucesso
