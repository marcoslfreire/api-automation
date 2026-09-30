@CT-005
@usuarios
Feature: Atualização com ID inexistente

  Scenario: Atualizar usuário utilizando ID inexistente
    Given que possuo um ID de usuário inexistente
    When atualizo um usuário utilizando esse ID
    Then um novo usuário deve ser criado
