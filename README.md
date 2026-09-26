# API Automation

Projeto de automação de testes de API desenvolvido como parte de um desafio técnico de QA Automation.

A solução utiliza **Java + RestAssured + JUnit 5**, com cenários adicionais em **Cucumber/Gherkin**, autenticação **JWT**, geração de relatórios com **Allure** e execução automatizada em **Jenkins**.

## Objetivo

Automatizar os principais fluxos da API de gerenciamento de usuários, contemplando cenários positivos, negativos, validações de dados, autenticação e operações protegidas.

A API utilizada no projeto é a **ServeRest**:

`https://serverest.dev`

## Tecnologias

* Java 21
* Maven
* RestAssured
* JUnit 5
* Cucumber / Gherkin
* Allure Report
* Jenkins
* Git / GitHub

## Estrutura do projeto

```text
src
├── main
│   └── java
│       └── br/com/qaautomation
│
└── test
    ├── java
    │   └── br/com/qaautomation/teste
    │       ├── config
    │       │   ├── ApiLogFilter.java
    │       │   ├── BaseTest.java
    │       │   ├── TestDataFactory.java
    │       │   └── UserData.java
    │       ├── runners
    │       │   └── CucumberTest.java
    │       ├── steps
    │       │   └── UsuarioSteps.java
    │       ├── JwtAuthenticationTest.java
    │       ├── LoginTest.java
    │       ├── UserCreationTest.java
    │       ├── UserTest.java
    │       └── UserValidationTest.java
    │
    └── resources
        ├── allure.properties
        └── features
            └── usuarios.feature
```

## Pré-requisitos

Para executar o projeto localmente é necessário ter instalado:

* JDK 21
* Maven 3.9+
* Git

O Jenkins é necessário apenas para execução do pipeline de CI.

## Executando os testes

Clone o repositório:

```bash
git clone https://github.com/marcoslfreire/api-automation.git
```

Acesse o projeto:

```bash
cd api-automation
```

Execute a suíte completa:

```bash
mvn clean test
```

O Maven executará os testes automatizados e disponibilizará os resultados do Allure em:

```text
target/allure-results
```

### Executando um teste específico

Também é possível executar uma classe específica:

```bash
mvn test -Dtest=JwtAuthenticationTest
```

Ou um método específico:

```bash
mvn test -Dtest=JwtAuthenticationTest#deveUtilizarJwtEmOperacaoProtegida
```

## Relatório Allure

Após executar os testes, gere o relatório localmente com:

```bash
mvn allure:report
```

O relatório será gerado em:

```text
target/site/allure-maven-plugin
```

O projeto também está configurado para publicação automática do relatório no Jenkins.

## Autenticação JWT

O projeto possui um fluxo dedicado para validar autenticação baseada em JWT.

O cenário executa o fluxo completo:

```text
Criação de usuário administrador
          ↓
POST /login
          ↓
Obtenção do JWT
          ↓
POST /produtos com Authorization
          ↓
Consulta do produto criado
```

O token é utilizado no header:

```text
Authorization: Bearer <JWT>
```

O fluxo também valida o uso do token em uma operação protegida da API.

Por segurança, tokens JWT são mascarados nos logs de execução:

```text
"authorization": "Bearer ***"
```

O token real continua sendo utilizado internamente pelo teste.

## Jenkins

O projeto possui um `Jenkinsfile` configurado para execução do pipeline.

O pipeline realiza:

```text
Checkout do código
       ↓
Configuração do Maven
       ↓
Execução dos testes
       ↓
Geração dos resultados Allure
       ↓
Publicação do relatório Allure
```

### Pipeline

O Jenkins executa:

```bash
mvn clean test
```

Após a execução dos testes, os resultados localizados em:

```text
target/allure-results
```

são utilizados para gerar o relatório Allure.

O relatório fica disponível diretamente na execução do job no Jenkins.

## Cenários automatizados

### Autenticação

* Login com credenciais válidas
* Login com senha inválida

### Criação de usuário

* Cadastro de usuário válido
* Cadastro com e-mail inválido
* Cadastro com e-mail duplicado
* Cadastro sem nome
* Cadastro sem e-mail
* Cadastro sem senha
* Cadastro sem administrador
* Cadastro com administrador inválido

### Consulta de usuários

* Listagem de usuários
* Busca de usuário por e-mail
* Busca de usuário inexistente
* Consulta de usuário existente após criação

### Atualização

* Atualização de usuário existente
* Atualização utilizando ID inexistente

### Exclusão

* Exclusão de usuário existente
* Exclusão de usuário inexistente
* Validação do usuário após exclusão

### Autenticação JWT e operações protegidas

* Criação de usuário administrador
* Autenticação via `/login`
* Validação do recebimento do JWT
* Utilização do JWT em endpoint protegido
* Criação de produto autenticado
* Consulta e validação do produto criado

### Cucumber / BDD

Os principais fluxos funcionais de gerenciamento de usuários também possuem cenários escritos em Gherkin, permitindo uma representação mais próxima da linguagem de negócio.

Os cenários Cucumber complementam os testes implementados diretamente com JUnit 5 e RestAssured.

## Resultado atual

A última execução completa validada localmente apresentou:

```text
Tests run: 34
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Resultado:

```text
34/34 testes aprovados
```

A suíte contempla os principais endpoints e comportamentos previstos no escopo do desafio, incluindo:

* autenticação;
* criação;
* consulta;
* atualização;
* exclusão;
* validações negativas;
* autenticação JWT;
* operação protegida.

## CI/CD

O projeto utiliza Jenkins para integração contínua.

A cada execução do pipeline:

1. O código é obtido do GitHub.
2. O ambiente Maven é configurado.
3. A suíte de testes é executada.
4. Os resultados dos testes são gerados.
5. O relatório Allure é publicado no Jenkins.

## Repositório

Código-fonte:

`https://github.com/marcoslfreire/api-automation`

## Observações

Os testes utilizam dados dinâmicos para evitar conflitos entre execuções, principalmente durante o cadastro de usuários.

O projeto prioriza a validação dos comportamentos funcionais da API, incluindo:

* códigos HTTP;
* mensagens retornadas;
* identificadores gerados;
* dados dos recursos;
* regras de validação;
* pós-condições das operações;
* autenticação e autorização.

Os logs HTTP possuem mascaramento do token JWT para evitar exposição de credenciais durante a execução dos testes.
