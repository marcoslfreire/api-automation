@CT-004
@usuarios
Feature: Atualização de usuário

  Scenario: Atualizar usuário cadastrado
    Given que possuo um usuário cadastrado
    When atualizo os dados do usuário
    Then o usuário deve ser atualizado com sucesso
    And os dados atualizados devem ser retornados
