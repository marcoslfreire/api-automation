# API Automation

Projeto de automação de testes de API desenvolvido como parte de um desafio técnico de QA Automation.

A solução utiliza **Java + RestAssured + JUnit 5**, com cenários adicionais em **Cucumber**, geração de relatórios com **Allure** e execução automatizada em **Jenkins**.

## Objetivo

Automatizar os principais fluxos da API de gerenciamento de usuários, contemplando cenários positivos, negativos e de validação.

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
    │       │   ├── BaseTest.java
    │       │   ├── TestDataFactory.java
    │       │   └── UserData.java
    │       ├── runners
    │       │   └── CucumberTest.java
    │       ├── steps
    │       │   └── UsuarioSteps.java
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

O Jenkins é necessário apenas para executar o pipeline de CI.

## Executando os testes

Clone o repositório:

```bash
git clone https://github.com/marcoslfreire/api-automation.git
```

Acesse o projeto:

```bash
cd api-automation
```

Execute os testes:

```bash
mvn clean test
```

O Maven executará os testes automatizados e disponibilizará os resultados em:

```text
target/allure-results
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

### Cucumber

Os principais fluxos funcionais também possuem cenários escritos em Gherkin, permitindo uma representação mais próxima da linguagem de negócio.

## Resultado atual

Na última execução validada no Jenkins:

```text
Total de testes: 31
Passaram:        31
Falharam:         0
Erros:            0
```

Resultado:

```text
31/31 testes aprovados
```

O resultado acima representa a execução dos testes automatizados. A cobertura funcional foi estruturada a partir dos principais endpoints e comportamentos previstos no escopo do desafio.

## CI/CD

O projeto utiliza Jenkins para integração contínua.

A cada execução do pipeline:

1. O código é obtido do GitHub.
2. O ambiente Maven é configurado.
3. A suíte de testes é executada.
4. Os resultados são gerados.
5. O relatório Allure é publicado no Jenkins.

## Repositório

Código-fonte:

`https://github.com/marcoslfreire/api-automation`

## Observações

Os testes utilizam dados dinâmicos para evitar conflitos entre execuções, principalmente durante o cadastro de usuários.

O projeto prioriza a validação dos comportamentos funcionais da API, incluindo respostas HTTP, mensagens retornadas e dados dos usuários.
