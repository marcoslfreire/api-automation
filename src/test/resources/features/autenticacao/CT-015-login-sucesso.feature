@CT-015
@autenticacao
Feature: Login

  Scenario: Realizar login com credenciais válidas
    Given que possuo um usuário administrador cadastrado
    When realizo o login com suas credenciais
    Then o login deve ser realizado com sucesso
    And um token JWT deve ser retornado
