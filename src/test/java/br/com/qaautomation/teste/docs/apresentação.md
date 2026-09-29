## Apresentação do Projeto de Automação de Testes de API

### 1. Contexto e objetivo

“Esse projeto foi desenvolvido como um desafio de automação de testes de API.

O objetivo principal foi criar uma suíte de testes automatizados para validar os principais comportamentos da API, principalmente o gerenciamento de usuários, contemplando cenários positivos, negativos, validações, autenticação e também uma operação protegida utilizando JWT.

Além de simplesmente automatizar os testes, eu procurei estruturar o projeto pensando em organização, manutenção, reutilização e execução em uma pipeline de CI.”

---

### 2. Tecnologias utilizadas

“Para desenvolver o projeto eu utilizei Java 21 e Maven.

Para fazer as chamadas HTTP e as validações da API, utilizei o Rest Assured.

Na camada de testes utilizei JUnit, e também implementei cenários utilizando Cucumber com Gherkin para representar os comportamentos da API em BDD.

Para os relatórios utilizei o Allure.

Também integrei o projeto com Jenkins para execução automatizada da suíte e publicação dos resultados.

O código ficou versionado no GitHub.”

---

### 3. Estrutura do projeto

“Na estrutura do projeto eu procurei separar as responsabilidades.

Eu tenho a parte de configuração, onde centralizei informações como a URL da API e alguns recursos utilizados pelos testes.

Também criei uma camada de API, onde concentrei as chamadas relacionadas aos usuários.

Os testes ficam responsáveis principalmente pelas validações e pelos comportamentos que eu quero verificar.

Para o Cucumber, tenho os arquivos `.feature` com os cenários em Gherkin e as classes de steps responsáveis pela implementação desses cenários.”

---

### 4. Configuração da API

“Uma das melhorias que eu fiz durante a evolução do projeto foi centralizar a configuração da URL da API.

Em vez de deixar a URL espalhada pelos testes, criei uma classe de configuração.

A URL padrão aponta para o ServeRest, mas também consigo sobrescrever essa configuração através de uma propriedade do Maven, utilizando `-DbaseUrl`.

Isso deixa o projeto mais flexível para executar em outros ambientes sem precisar alterar o código dos testes.”

---

### 5. Camada de API

“Também criei uma camada chamada `UsuarioApi`.

A ideia foi concentrar nela as operações relacionadas ao recurso de usuários.

Por exemplo, tenho métodos para listar usuários, buscar por ID, buscar por e-mail, criar, atualizar e excluir usuários.

Essa classe é responsável por realizar a comunicação com a API e retornar o `Response`.

Eu mantive as asserções fora dessa camada porque não queria misturar a responsabilidade de fazer a chamada HTTP com a responsabilidade de validar o comportamento.

Dessa forma, o teste decide o que precisa ser validado a partir da resposta recebida.”

---

### 6. Geração dos dados de teste

“Também procurei evitar depender de dados fixos.

Criei uma `TestDataFactory` para gerar os dados dos usuários utilizados nos testes.

Principalmente o e-mail é gerado dinamicamente utilizando UUID.

Isso é importante porque a API não permite cadastrar dois usuários com o mesmo e-mail.

Dessa forma, consigo executar os testes várias vezes sem depender de um usuário fixo que poderia já existir.”

---

### 7. Autenticação e JWT

“Na parte de autenticação, eu também procurei centralizar o comportamento.

Criei uma `AutenticacaoApi`, que concentra a chamada de login.

O fluxo que eu valido é basicamente:

criar um usuário administrador, realizar o login utilizando as credenciais desse u
