@CT-003
@usuarios
Feature: Busca de usuário inexistente

  Scenario: Buscar usuário com ID inexistente
    Given que possuo um ID de usuário inexistente
    When busco o usuário pelo ID
    Then o usuário não deve ser encontrado
