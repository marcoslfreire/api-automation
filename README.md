# API Automation

Projeto de automação de testes de API desenvolvido como parte de um desafio técnico de QA Automation.

A solução utiliza **Java 21, Rest Assured, JUnit 5, Cucumber/Gherkin, Allure Report e Jenkins**, com foco na validação dos principais fluxos da API de gerenciamento de usuários, incluindo cenários positivos, negativos, validações, autenticação e operações protegidas.

A API utilizada no projeto é a **ServeRest**.

**API:** https://serverest.dev

---

## Objetivo

Automatizar e validar os principais comportamentos da API de gerenciamento de usuários, contemplando:

* criação de usuários;
* consulta de usuários;
* atualização de usuários;
* exclusão de usuários;
* validações de dados;
* autenticação por login;
* autenticação baseada em JWT;
* utilização de JWT em operação protegida;
* cenários positivos e negativos.

O projeto também possui cenários BDD implementados com **Cucumber/Gherkin**, além dos testes implementados diretamente com **JUnit 5 e Rest Assured**.

---

## Tecnologias

* **Java 21**
* **Maven**
* **JUnit 5**
* **Rest Assured**
* **Cucumber / Gherkin**
* **Allure Report**
* **Jenkins**
* **Git / GitHub**

### Principais versões utilizadas

| Tecnologia     | Versão |
| -------------- | ------ |
| Java           | 21     |
| JUnit Jupiter  | 5.13.4 |
| Rest Assured   | 6.0.1  |
| Cucumber       | 7.28.2 |
| Allure Java    | 2.35.3 |
| Allure Maven   | 2.17.0 |
| JUnit Platform | 1.13.4 |

---

## Estrutura do projeto

```text
api-automation/
├── pom.xml
├── Jenkinsfile
├── README.md
├── .gitignore
│
├── .allure/
│   └── allure-2.36.0/
│
├── allure-results/
│
└── src/
    ├── main/
    │   └── java/
    │       └── br/com/qaautomation/
    │           └── Main.java
    │
    └── test/
        ├── java/
        │   └── br/com/qaautomation/teste/
        │       ├── JwtAuthenticationTest.java
        │       ├── LoginTest.java
        │       ├── UserCreationTest.java
        │       ├── UserTest.java
        │       ├── UserValidationTest.java
        │       │
        │       ├── config/
        │       │   ├── ApiLogFilter.java
        │       │   ├── BaseTest.java
        │       │   ├── TestDataFactory.java
        │       │   └── UserData.java
        │       │
        │       ├── runners/
        │       │   └── CucumberTest.java
        │       │
        │       └── steps/
        │           ├── Hooks.java
        │           └── UsuarioSteps.java
        │
        └── resources/
            ├── allure.properties
            ├── cucumber.properties
            └── features/
                └── usuarios.feature
```

---

## Organização dos testes

Os testes estão organizados por responsabilidade.

### `config`

Contém componentes reutilizáveis utilizados pelos testes:

* `BaseTest` — centraliza a URL base utilizada pelos testes JUnit.
* `TestDataFactory` — gera dados dinâmicos para os usuários.
* `UserData` — representa os dados de um usuário.
* `ApiLogFilter` — fornece logging HTTP e mascara o token JWT quando a propriedade `authorization` aparece na resposta.

### `runners`

* `CucumberTest` — configura a execução dos cenários Cucumber através do JUnit Platform.

### `steps`

* `UsuarioSteps` — implementa os passos dos cenários definidos em `usuarios.feature`.
* `Hooks` — executa ações antes e depois de cada cenário Cucumber e registra o status do cenário no console.

---

## Pré-requisitos

Para executar o projeto localmente:

* JDK 21;
* Maven 3.9 ou superior;
* Git;
* acesso à internet para comunicação com a API ServeRest.

O Jenkins é utilizado para execução do pipeline de CI e requer a configuração das ferramentas utilizadas pelo `Jenkinsfile`.

---

## Configuração

Não é necessário configurar banco de dados localmente.

Os testes utilizam diretamente a API:

```text
https://serverest.dev
```

Os dados de usuários são gerados dinamicamente durante os testes.

A `TestDataFactory` utiliza UUID para gerar e-mails diferentes entre as execuções, reduzindo conflitos com registros existentes na API.

---

## Executando os testes

Clone o repositório:

```bash
git clone https://github.com/marcoslfreire/api-automation.git
```

Acesse o diretório:

```bash
cd api-automation
```

Execute a suíte completa:

```bash
mvn clean test
```

O Maven compila o projeto e executa os testes JUnit e os cenários Cucumber configurados na suíte.

Os resultados utilizados pelo Allure são configurados em:

```text
target/allure-results
```

---

## Executando um teste específico

É possível executar uma classe JUnit específica:

```bash
mvn test -Dtest=JwtAuthenticationTest
```

Também é possível executar um método específico:

```bash
mvn test -Dtest=JwtAuthenticationTest#deveUtilizarJwtEmOperacaoProtegida
```

---

## Allure Report

O projeto utiliza Allure para geração dos resultados e visualização dos testes.

O arquivo:

```text
src/test/resources/allure.properties
```

define:

```properties
allure.results.directory=target/allure-results
```

Portanto, os resultados são gerados em:

```text
target/allure-results
```

### Gerando o relatório localmente

Com os resultados disponíveis, o relatório pode ser gerado utilizando:

```bash
mvn allure:report
```

O relatório gerado pelo plugin Maven fica em:

```text
target/site/allure-maven-plugin
```

O projeto também possui configuração para publicação do relatório durante a execução do pipeline Jenkins.

---

## Autenticação JWT

O projeto possui uma classe específica para validar o fluxo de autenticação JWT:

```text
JwtAuthenticationTest.java
```

O fluxo validado é:

```text
Criação de usuário administrador
        ↓
POST /login
        ↓
Obtenção do JWT
        ↓
POST /produtos com Authorization
        ↓
GET /produtos/{id}
        ↓
Validação dos dados do produto
```

O token recebido no login é validado quanto à existência e ao formato:

```text
Bearer <JWT>
```

Em seguida, o token é utilizado no header:

```text
Authorization: Bearer <JWT>
```

para executar uma operação protegida.

O teste também valida os dados retornados pelo produto criado.

### Proteção de logs

O projeto possui um `ApiLogFilter` preparado para mascarar tokens JWT exibidos nos logs:

```text
"authorization": "Bearer ***"
```

O token real continua sendo utilizado internamente pela requisição.

O filtro está implementado no projeto, mas sua ativação está atualmente comentada nos pontos em que ele foi preparado.

---

## Cenários automatizados

A suíte atual possui:

* **20 testes JUnit**
* **14 cenários Cucumber**
* **34 execuções automatizadas na suíte**

Os 14 cenários Cucumber representam fluxos funcionais que também possuem cobertura correspondente nos testes JUnit. Portanto, os 34 testes/cenários representam **execuções automatizadas**, e não 34 comportamentos funcionais distintos.

### Autenticação

* Login com credenciais válidas;
* Login com senha incorreta.

### Criação e validação de usuários

* Cadastro de usuário com sucesso;
* Cadastro com e-mail inválido;
* Cadastro com e-mail duplicado;
* Cadastro sem nome;
* Cadastro sem e-mail;
* Cadastro sem password;
* Cadastro sem administrador;
* Cadastro com administrador inválido.

### Consulta de usuários

* Listagem de usuários;
* Busca de usuário por e-mail;
* Busca de usuário por ID inexistente;
* Consulta do usuário criado pelo ID.

### Atualização

* Atualização de usuário existente;
* Atualização utilizando ID inexistente, validando o comportamento da API de criação de um novo usuário.

### Exclusão

* Exclusão de usuário existente;
* Validação de que o usuário excluído não é mais encontrado;
* Exclusão de usuário inexistente.

### JWT e operação protegida

* Criação de usuário administrador;
* Autenticação através de `/login`;
* Validação do recebimento do JWT;
* Utilização do JWT em endpoint protegido;
* Criação de produto autenticado;
* Consulta do produto criado;
* Validação dos dados retornados.

---

## Cucumber / BDD

Os principais fluxos de gerenciamento de usuários também estão descritos em Gherkin no arquivo:

```text
src/test/resources/features/usuarios.feature
```

O arquivo possui **14 cenários** envolvendo:

* criação;
* consulta;
* atualização;
* exclusão;
* validações de cadastro.

O runner:

```text
src/test/java/br/com/qaautomation/teste/runners/CucumberTest.java
```

utiliza o **JUnit Platform** para executar os cenários Cucumber.

Os steps estão implementados em:

```text
src/test/java/br/com/qaautomation/teste/steps/UsuarioSteps.java
```

---

## Jenkins

O projeto possui um `Jenkinsfile` para execução da suíte em pipeline.

A configuração atual utiliza as ferramentas `Maven3` e `Allure` previamente configuradas no Jenkins:

```groovy
tools {
    maven 'Maven3'
    allure 'Allure'
}
```

### Pipeline atual

O pipeline possui duas etapas principais:

```text
Testes
   ↓
mvn clean test
   ↓
Relatório Allure
   ↓
target/allure-results
```

### Execução dos testes

O Jenkins executa:

```bash
mvn clean test
```

Após a execução, o estágio de relatório utiliza:

```text
target/allure-results
```

como origem dos resultados para publicação do relatório Allure.

### Observação sobre a configuração do Jenkins

O `Jenkinsfile` depende das ferramentas `Maven3` e `Allure` configuradas no ambiente Jenkins.

Portanto, para executar a pipeline em uma nova instalação do Jenkins, essas ferramentas precisam estar disponíveis e configuradas no servidor/agente utilizado pelo job.

A pipeline atual utiliza:

```groovy
bat 'mvn clean test'
```

e, portanto, está configurada para execução em um ambiente Windows.

---

## Resultado da execução

A suíte completa foi executada localmente utilizando:

```bash
mvn clean test
```

A execução foi concluída com sucesso:

```text
Tests run: 34
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Resultado da execução validada:

```text
34/34 execuções automatizadas concluídas com sucesso
```

---

## Cobertura funcional

A suíte atual cobre os principais comportamentos implementados no escopo do projeto, incluindo:

* criação de usuários;
* consulta de usuários;
* atualização de usuários;
* exclusão de usuários;
* validações de dados obrigatórios;
* validações de formato;
* validação de e-mail duplicado;
* login;
* tratamento de credenciais inválidas;
* autenticação JWT;
* utilização de JWT em operação protegida;
* validação das respostas HTTP;
* validação das mensagens retornadas;
* validação dos dados dos recursos;
* validação das pós-condições das operações.

A cobertura apresentada neste README representa os cenários efetivamente automatizados no projeto. Ela não deve ser interpretada como uma afirmação de cobertura de 100% de todas as possibilidades da API ou de todos os requisitos externos não automatizados.

---

## CI

O projeto utiliza Jenkins para execução contínua da suíte de testes.

O fluxo atual é:

```text
Código-fonte
     ↓
Jenkins
     ↓
Maven
     ↓
mvn clean test
     ↓
Testes JUnit + Cucumber
     ↓
target/allure-results
     ↓
Relatório Allure
```

O pipeline disponibiliza o relatório Allure associado à execução do job.

---

## Repositório

O código-fonte do projeto está disponível no GitHub:

https://github.com/marcoslfreire/api-automation

---

## Observações técnicas

### Dados dinâmicos

Os testes utilizam dados dinâmicos para reduzir conflitos entre execuções.

Os e-mails utilizados nos cadastros são gerados através de identificadores únicos.

### Validação das respostas

As validações não se limitam ao código HTTP. Os testes também verificam, conforme o cenário:

* mensagens retornadas pela API;
* identificadores gerados;
* atributos dos usuários;
* quantidade de registros;
* dados dos produtos;
* comportamento após atualização;
* comportamento após exclusão;
* token de autenticação;
* mensagens de validação.

### Organização

A solução separa:

* dados de teste;
* configuração básica;
* testes JUnit;
* cenários BDD;
* steps Cucumber;
* hooks;
* configuração do relatório;
* pipeline Jenkins.

Essa organização permite manter os testes funcionais separados dos componentes de suporte e da configuração de execução.

---

## Conclusão

O projeto implementa uma suíte de automação de testes de API utilizando **Java, Rest Assured, JUnit 5 e Cucumber**, com geração de resultados através do **Allure** e execução integrada ao **Jenkins**.

A execução local validada da suíte apresentou:

```text
34 execuções
0 falhas
0 erros
0 testes ignorados
BUILD SUCCESS
```

O projeto foi estruturado para demonstrar automação de APIs REST, validação funcional e negativa, autenticação JWT, BDD, geração de evidências de execução através do Allure e integração com uma pipeline de CI.
