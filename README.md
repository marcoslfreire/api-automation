# API Automation

Automação de testes de API desenvolvida como parte de um desafio técnico de QA Automation.

O projeto utiliza **Java, Rest Assured, Cucumber/Gherkin, JUnit Platform, Allure e Jenkins** para validar diferentes comportamentos da API [ServeRest](https://serverest.dev/), incluindo cadastro, consulta, atualização, exclusão, validações, autenticação JWT e autorização.

---

## Objetivo

Construir uma suíte de automação de testes de API com foco em:

* validação de comportamentos funcionais;
* cobertura de cenários positivos e negativos;
* autenticação e autorização;
* organização dos cenários utilizando BDD;
* reutilização de componentes de automação;
* geração de resultados e relatórios;
* integração com pipeline de CI/CD;
* rastreabilidade entre comportamento, cenário e automação.

A suíte atual possui **21 cenários automatizados em Cucumber**.

---

## Tecnologias utilizadas

| Tecnologia     | Utilização                          |
| -------------- | ----------------------------------- |
| Java 21        | Linguagem de desenvolvimento        |
| Maven          | Gerenciamento do projeto e execução |
| Rest Assured   | Automação e validação das APIs REST |
| Cucumber       | Especificação dos cenários em BDD   |
| Gherkin        | Descrição dos comportamentos        |
| JUnit Platform | Engine de execução dos testes       |
| Allure Report  | Relatórios de execução              |
| Jenkins        | Pipeline de CI/CD                   |
| Git            | Controle de versão                  |
| GitHub         | Repositório do projeto              |
| ServeRest      | API utilizada nos testes            |

---

## Arquitetura

A automação foi organizada separando a especificação dos cenários da implementação técnica das chamadas HTTP.

```text
Feature
   ↓
Step Definitions
   ↓
ScenarioContext / Test Data
   ↓
API Client
   ↓
ServeRest API
```

### Responsabilidade das camadas

**Feature**

Contém os cenários escritos em Gherkin, representando o comportamento que deve ser validado.

**Step Definitions**

Implementam os passos definidos nos arquivos `.feature`.

**ScenarioContext**

Mantém os dados compartilhados durante a execução de um cenário, como:

* usuário;
* ID;
* token;
* resposta;
* dados de atualização;
* request body.

**API Client**

Centraliza as chamadas HTTP para os endpoints da API.

Exemplo:

```text
UsuarioApi
```

**ServeRest**

API utilizada como sistema sob teste.

---

## Estrutura do projeto

```text
api-automation/
│
├── Jenkinsfile
├── README.md
├── pom.xml
│
└── src/
    └── test/
        ├── java/
        │   └── br/
        │       └── com/
        │           └── qaautomation/
        │               └── teste/
        │                   ├── api/
        │                   │   └── UsuarioApi.java
        │                   │
        │                   ├── config/
        │                   │   ├── ApiConfig.java
        │                   │   ├── AuthRequest.java
        │                   │   ├── BaseTest.java
        │                   │   ├── TestDataFactory.java
        │                   │   └── UserData.java
        │                   │
        │                   ├── context/
        │                   │   └── ScenarioContext.java
        │                   │
        │                   ├── legacy/
        │                   │   ├── JwtAuthenticationTest.java
        │                   │   ├── LoginTest.java
        │                   │   ├── UserCreationTest.java
        │                   │   ├── UserTest.java
        │                   │   └── UserValidationTest.java
        │                   │
        │                   ├── runners/
        │                   │   └── CucumberTest.java
        │                   │
        │                   └── steps/
        │                       ├── AutenticacaoSteps.java
        │                       └── UsuarioSteps.java
        │
        └── resources/
            └── features/
                ├── autenticacao/
                │   ├── CT-015-login-sucesso.feature
                │   ├── CT-016-login-senha-invalida.feature
                │   ├── CT-017-jwt-valido.feature
                │   ├── CT-018-jwt-ausente.feature
                │   ├── CT-019-jwt-invalido.feature
                │   └── CT-020-usuario-sem-permissao.feature
                │
                └── usuarios/
                    ├── CT-001-criar-usuario.feature
                    ├── CT-002-buscar-usuario-email.feature
                    ├── CT-003-buscar-usuario-inexistente.feature
                    ├── CT-004-atualizar-usuario.feature
                    ├── CT-005-atualizar-id-inexistente.feature
                    ├── CT-006-excluir-usuario.feature
                    ├── CT-007-excluir-usuario-inexistente.feature
                    ├── CT-008-email-invalido.feature
                    ├── CT-009-email-duplicado.feature
                    ├── CT-010-sem-nome.feature
                    ├── CT-011-sem-email.feature
                    ├── CT-012-sem-password.feature
                    ├── CT-013-sem-administrador.feature
                    ├── CT-014-administrador-invalido.feature
                    └── CT-021-listar-usuarios.feature
```

---

## Cenários automatizados

A suíte possui 21 cenários funcionais.

### Usuários

| ID     | Cenário                                     |
| ------ | ------------------------------------------- |
| CT-001 | Criar usuário com dados válidos             |
| CT-002 | Buscar usuário por e-mail                   |
| CT-003 | Buscar usuário inexistente                  |
| CT-004 | Atualizar usuário existente                 |
| CT-005 | Atualizar utilizando ID inexistente         |
| CT-006 | Excluir usuário existente                   |
| CT-007 | Excluir usuário inexistente                 |
| CT-008 | Impedir cadastro com e-mail inválido        |
| CT-009 | Impedir cadastro com e-mail duplicado       |
| CT-010 | Impedir cadastro sem nome                   |
| CT-011 | Impedir cadastro sem e-mail                 |
| CT-012 | Impedir cadastro sem password               |
| CT-013 | Impedir cadastro sem administrador          |
| CT-014 | Impedir cadastro com administrador inválido |
| CT-021 | Listar usuários cadastrados                 |

### Autenticação e autorização

| ID     | Cenário                                                         |
| ------ | --------------------------------------------------------------- |
| CT-015 | Realizar login com credenciais válidas                          |
| CT-016 | Rejeitar login com senha inválida                               |
| CT-017 | Permitir operação protegida com JWT válido                      |
| CT-018 | Rejeitar operação protegida sem JWT                             |
| CT-019 | Rejeitar operação protegida com JWT inválido                    |
| CT-020 | Rejeitar operação administrativa para usuário não administrador |

---

## Estratégia de testes

Os cenários foram organizados de forma que cada execução tenha sua própria preparação de dados.

A automação evita depender de dados fixos previamente existentes na API sempre que o cenário exige um usuário específico.

Por exemplo, um cenário que precisa de um usuário cadastrado cria sua própria massa antes da execução.

Isso reduz a dependência entre cenários e facilita a execução isolada.

---

## BDD com Cucumber

O Cucumber é utilizado como camada de especificação funcional.

Exemplo:

```gherkin
Scenario: Criar usuário com dados válidos
  Given que possuo os dados válidos de um novo usuário
  When realizo o cadastro do usuário
  Then o usuário deve ser criado com sucesso
  And o identificador do usuário deve ser retornado
  And os dados do usuário criado devem ser persistidos
```

A intenção é manter o cenário orientado ao comportamento esperado, evitando colocar detalhes de implementação HTTP diretamente no Gherkin.

---

## JUnit Platform

O projeto utiliza o **JUnit Platform** como mecanismo de execução do Cucumber.

O runner principal é:

```text
CucumberTest.java
```

O runner seleciona os recursos Cucumber e configura o pacote responsável pelos Step Definitions.

Os testes funcionais atuais são executados pela engine do Cucumber.

---

## Testes legados

Durante a evolução do projeto, existiam testes funcionais implementados diretamente com JUnit.

Esses testes foram preservados em:

```text
src/test/java/br/com/qaautomation/teste/legacy/
```

Eles permanecem no projeto como referência histórica e técnica da evolução da automação.

A execução principal da V3 utiliza os cenários Cucumber.

Os testes legados não são executados pelo fluxo principal do Maven, evitando duplicidade de execução dos mesmos comportamentos.

---

## Configuração do ambiente

### Pré-requisitos

Instalar:

* Java 21;
* Maven;
* Git.

Verificar as versões:

```bash
java -version
mvn -version
git --version
```

---

## Clonar o projeto

```bash
git clone https://github.com/marcoslfreire/api-automation.git
```

Entrar no projeto:

```bash
cd api-automation
```

---

## Configuração da URL da API

A URL padrão utilizada pelo projeto é:

```text
https://serverest.dev
```

A configuração está centralizada em:

```text
ApiConfig.java
```

O projeto também permite sobrescrever a URL através da propriedade:

```text
baseUrl
```

Exemplo:

```bash
mvn clean test -DbaseUrl=https://serverest.dev
```

Quando nenhuma propriedade é informada, o projeto utiliza a URL padrão.

---

## Executar os testes

Para executar a suíte completa:

```bash
mvn clean test
```

A execução atual possui:

```text
Tests run: 21
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

---

## Relatórios Allure

Durante a execução são gerados resultados do Allure em:

```text
target/allure-results
```

Esses arquivos contêm os dados utilizados para geração do relatório.

Para gerar/visualizar o relatório localmente, utilizando a instalação do Allure CLI:

```bash
allure serve target/allure-results
```

O relatório permite analisar os resultados dos cenários executados e seus respectivos detalhes.

---

## Integração com Jenkins

O projeto possui um `Jenkinsfile` na raiz do repositório.

Pipeline atual:

```text
Jenkins
   ↓
Maven
   ↓
mvn clean test
   ↓
Cucumber
   ↓
21 cenários
   ↓
Surefire
   ↓
Allure Results
   ↓
Relatórios
```

O pipeline utiliza as ferramentas configuradas no Jenkins para:

* executar os testes;
* publicar os resultados JUnit/Surefire;
* disponibilizar os resultados do Allure.

O `Jenkinsfile` também possui configuração para executar as etapas de publicação no bloco `post`, permitindo que os resultados sejam processados após a execução da suíte.

---

## Autenticação e autorização

A API utiliza autenticação baseada em JWT.

Os testes validam diferentes situações:

### Login válido

Verifica:

* credenciais válidas;
* status HTTP `200`;
* retorno do token JWT.

### Login inválido

Verifica a rejeição de credenciais inválidas.

### JWT válido

Verifica o acesso a uma operação protegida utilizando um token válido.

### JWT ausente

Verifica a rejeição de uma operação protegida quando o token não é enviado.

### JWT inválido

Verifica a rejeição de um token inválido.

### Usuário sem permissão administrativa

Verifica a restrição de uma operação administrativa para um usuário autenticado sem perfil de administrador.

---

## Validações de dados

A suíte também cobre regras de validação do cadastro de usuários.

Entre elas:

* e-mail inválido;
* e-mail duplicado;
* nome obrigatório;
* e-mail obrigatório;
* password obrigatório;
* administrador obrigatório;
* administrador limitado aos valores esperados pela API.

Os testes validam tanto o status HTTP quanto as mensagens retornadas pela API quando aplicável.

---

## Observação sobre atualização de usuário

A API ServeRest possui um comportamento específico para `PUT /usuarios/{id}`.

Quando o ID informado não existe, a API pode realizar o cadastro de um novo usuário.

Esse comportamento é validado especificamente pelo cenário:

```text
CT-005
```

O cenário foi mantido separado do fluxo de atualização de um usuário existente para representar os dois comportamentos observados na API.

---

## Rate Limit

Durante a exploração da API foi realizada uma validação do comportamento de limite de requisições.

Foram executadas requisições sequenciais para verificar se a API retornaria um status de rate limit, como `429`, ou headers relacionados ao controle de requisições.

Na execução realizada, as requisições não apresentaram resposta `429` nem headers específicos de rate limit.

Por esse motivo, o rate limit não foi transformado em um cenário automatizado da suíte atual.

A validação permanece como uma investigação exploratória realizada durante o desenvolvimento do projeto.

---

## Decisões técnicas

### Cucumber como camada funcional

A V3 utiliza Cucumber para representar os comportamentos funcionais da API.

Isso permite separar:

```text
Especificação
    ↓
Implementação
```

Os arquivos `.feature` representam o comportamento esperado, enquanto os Step Definitions implementam a execução.

### JUnit como engine de execução

O JUnit Platform é utilizado como infraestrutura de execução do Cucumber.

A intenção não é manter uma duplicação de testes funcionais entre JUnit e Cucumber.

### Separação dos API Clients

As chamadas HTTP são centralizadas em classes específicas, como:

```text
UsuarioApi
```

Isso evita espalhar detalhes de comunicação HTTP pelos Step Definitions.

### ScenarioContext

O `ScenarioContext` permite compartilhar dados entre os passos de um mesmo cenário sem utilizar estado global.

### Dados independentes

Sempre que necessário, os cenários criam seus próprios dados para reduzir dependências entre execuções.

---

## Resultado atual

Última execução validada:

```text
Tests run: 21
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Todos os 21 cenários funcionais da suíte Cucumber foram executados com sucesso.

---

## Fluxo completo da solução

```text
                  GitHub
                     │
                     ▼
                  Jenkins
                     │
                     ▼
               Maven / Surefire
                     │
                     ▼
                Cucumber
                     │
             ┌───────┴───────┐
             ▼               ▼
        Usuários        Autenticação
             │               │
             └───────┬───────┘
                     ▼
                Rest Assured
                     │
                     ▼
                 ServeRest
                     │
             ┌───────┴────────┐
             ▼                ▼
          Surefire       Allure Results
                              │
                              ▼
                         Allure Report
```

---

## Repositório

GitHub:

https://github.com/marcoslfreire/api-automation

---

## Autor

**Marcos Luciano Freire**

QA Automation / Software Engineer

GitHub: https://github.com/marcoslfreire
