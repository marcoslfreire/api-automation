@usuarios
Feature: Gerenciamento de usuários

  @crud
  Scenario: Criar usuário com sucesso
    Given que possuo os dados de um novo usuário
    When realizo o cadastro do usuário
    Then o usuário deve ser criado com sucesso
    And o usuário criado deve ser encontrado pelo id

  @crud
  Scenario: Buscar usuário por email
    Given que possuo os dados de um novo usuário
    And o usuário foi cadastrado
    When busco o usuário pelo email
    Then o usuário deve ser encontrado

  @crud
  Scenario: Buscar usuário inexistente
    Given que possuo um id de usuário inexistente
    When busco o usuário pelo id
    Then a API deve informar que o usuário não foi encontrado

  @crud
  Scenario: Atualizar usuário com sucesso
    Given que possuo os dados de um novo usuário
    And o usuário foi cadastrado
    When atualizo os dados do usuário
    Then o usuário deve ser atualizado com sucesso
    And os dados atualizados devem ser retornados

  @crud
  Scenario: Atualizar usuário com id inexistente
    Given que possuo um id de usuário inexistente
    When atualizo um usuário com esse id
    Then um novo usuário deve ser criado

  @crud
  Scenario: Excluir usuário com sucesso
    Given que possuo os dados de um novo usuário
    And o usuário foi cadastrado
    When excluo o usuário
    Then o usuário deve ser excluído com sucesso
    And o usuário excluído não deve ser encontrado

  @crud
  Scenario: Excluir usuário inexistente
    Given que possuo um id de usuário inexistente
    When excluo o usuário
    Then nenhum registro deve ser excluído

  @validacao
  Scenario: Impedir cadastro com email inválido
    Given que possuo dados de usuário com email inválido
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o email é inválido

  @validacao
  Scenario: Impedir cadastro com email duplicado
    Given que possuo os dados de um novo usuário
    And o usuário foi cadastrado
    When tento cadastrar novamente o mesmo usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o email já está sendo usado

  @validacao
  Scenario: Impedir cadastro sem nome
    Given que possuo dados de usuário sem nome
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o nome é obrigatório

  @validacao
  Scenario: Impedir cadastro sem email
    Given que possuo dados de usuário sem email
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o email é obrigatório

  @validacao
  Scenario: Impedir cadastro sem password
    Given que possuo dados de usuário sem password
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o password é obrigatório

  @validacao
  Scenario: Impedir cadastro sem administrador
    Given que possuo dados de usuário sem administrador
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o administrador é obrigatório

  @validacao
  Scenario: Impedir cadastro com administrador inválido
    Given que possuo dados de usuário com administrador inválido
    When realizo o cadastro do usuário
    Then o cadastro deve ser rejeitado
    And a API deve informar que o administrador é inválido
