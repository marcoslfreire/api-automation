@CT-007
@usuarios
Feature: Exclusão de usuário inexistente

  Scenario: Excluir usuário utilizando ID inexistente
    Given que possuo um ID de usuário inexistente
    When excluo o usuário
    Then nenhum registro deve ser excluído
