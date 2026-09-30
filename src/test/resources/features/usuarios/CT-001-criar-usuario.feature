@CT-001
@usuarios
Feature: Cadastro de usuário

  Scenario: Criar usuário com dados válidos
    Given que possuo os dados válidos de um novo usuário
    When realizo o cadastro do usuário
    Then o usuário deve ser criado com sucesso
    And o identificador do usuário deve ser retornado
    And os dados do usuário criado devem ser persistidos
