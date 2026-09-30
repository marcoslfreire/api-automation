@CT-006
@usuarios
Feature: Exclusão de usuário

  Scenario: Excluir usuário cadastrado
    Given que possuo um usuário cadastrado
    When excluo o usuário
    Then o usuário deve ser excluído com sucesso
    And o usuário excluído não deve ser encontrado
