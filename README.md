# API Automation

Automação de testes de API desenvolvida por mim utilizando **Java, Rest Assured, JUnit 5, Cucumber, Allure e Jenkins**, tendo como alvo a API pública **ServeRest**.

Meu objetivo neste projeto foi construir uma automação que não ficasse apenas limitada à execução de requisições, mas que também tivesse uma estrutura organizada de testes, rastreabilidade dos cenários, autenticação, geração de evidências e execução em CI.

---

## 📚 Navegação

* [🎯 Objetivo](#-objetivo)
* [🛠️ Tecnologias](#️-tecnologias)
* [🏗️ Arquitetura](#️-arquitetura)
* [🧪 Cenários automatizados](#-cenários-automatizados)
* [🥒 BDD com Cucumber](#-bdd-com-cucumber)
* [🔐 Autenticação e autorização](#-autenticação-e-autorização)
* [📊 Allure](#-allure)
* [⚙️ Jenkins](#️-jenkins)
* [▶️ Executando o projeto](#️-executando-o-projeto)
* [🔎 Comportamentos investigados](#-comportamentos-investigados)
* [🧠 Decisões técnicas](#-decisões-técnicas)
* [📁 Estrutura do projeto](#-estrutura-do-projeto)
* [✅ Resultado final](#-resultado-final)

---

# 🎯 Objetivo

Desenvolvi este projeto como uma solução de **QA Automation para testes de API**.

Durante a construção, meu foco foi trabalhar não somente a automação dos endpoints, mas também aspectos que considero importantes em um projeto real de qualidade:

* organização dos testes;
* reutilização de código;
* isolamento dos cenários;
* validação de respostas;
* autenticação;
* autorização;
* documentação dos comportamentos;
* geração de evidências;
* execução automatizada;
* integração contínua.

A API utilizada foi a **ServeRest**:

`https://serverest.dev`

---

# 🛠️ Tecnologias

Para desenvolver a automação utilizei:

| Tecnologia   | Utilização                 |
| ------------ | -------------------------- |
| Java 21      | Linguagem principal        |
| Maven        | Gerenciamento e execução   |
| Rest Assured | Automação de API           |
| JUnit 5      | Plataforma de execução     |
| Cucumber     | BDD                        |
| Gherkin      | Especificação dos cenários |
| Allure       | Relatórios                 |
| Jenkins      | CI                         |
| Git          | Controle de versão         |
| GitHub       | Repositório                |

---

# 🏗️ Arquitetura

Organizei a automação separando a especificação dos comportamentos da implementação técnica.

O fluxo principal ficou:

```text
Feature
   ↓
Steps
   ↓
ScenarioContext / Utils
   ↓
API Client
   ↓
ServeRest
```

As **Features** descrevem o comportamento esperado.

Os **Steps** implementam as ações e validações.

O **ScenarioContext** permite compartilhar informações durante um cenário, como:

* usuário;
* ID;
* token;
* email;
* senha;
* request;
* response.

O **API Client** concentra as chamadas HTTP para a API.

Com essa separação, evitei colocar toda a implementação diretamente dentro dos steps.

---

# 🧪 Cenários automatizados

Na versão atual da automação implementei **21 cenários Cucumber**.

### Usuários

| ID     | Cenário                             |
| ------ | ----------------------------------- |
| CT-001 | Criar usuário com dados válidos     |
| CT-002 | Buscar usuário por email            |
| CT-003 | Buscar usuário inexistente          |
| CT-004 | Atualizar usuário existente         |
| CT-005 | Atualizar utilizando ID inexistente |
| CT-006 | Excluir usuário existente           |
| CT-007 | Excluir usuário inexistente         |

### Validações

| ID     | Cenário                     |
| ------ | --------------------------- |
| CT-008 | Email inválido              |
| CT-009 | Email duplicado             |
| CT-010 | Nome não informado          |
| CT-011 | Email não informado         |
| CT-012 | Password não informado      |
| CT-013 | Administrador não informado |
| CT-014 | Administrador inválido      |

### Autenticação e autorização

| ID     | Cenário                                |
| ------ | -------------------------------------- |
| CT-015 | Login com sucesso                      |
| CT-016 | Login com senha inválida               |
| CT-017 | Operação protegida com JWT válido      |
| CT-018 | Operação protegida sem JWT             |
| CT-019 | Operação protegida com JWT inválido    |
| CT-020 | Usuário sem permissão de administrador |

### Listagem

| ID     | Cenário         |
| ------ | --------------- |
| CT-021 | Listar usuários |

---

# 🥒 BDD com Cucumber

Na evolução para a V3, utilizei o **Cucumber** para representar os comportamentos da API através de Gherkin.

Por exemplo:

```gherkin
Scenario: Criar usuário com dados válidos
    Given que possuo os dados válidos de um novo usuário
    When realizo o cadastro do usuário
    Then o usuário deve ser criado com sucesso
```

Minha intenção foi manter o Gherkin focado no **comportamento**, evitando colocar detalhes técnicos de HTTP diretamente na especificação.

A execução do Cucumber é realizada através do **JUnit Platform**.

Dessa forma, o JUnit funciona como mecanismo de execução e o Cucumber fica responsável pela camada BDD.

---

# 🔐 Autenticação e autorização

Também implementei cenários específicos para validar autenticação e autorização utilizando JWT.

O fluxo utilizado foi:

```text
Criar usuário administrador
        ↓
Realizar login
        ↓
Receber JWT
        ↓
Enviar JWT no Authorization
        ↓
Executar operação protegida
```

Validei:

* login com credenciais válidas;
* login com senha inválida;
* JWT válido;
* ausência de JWT;
* JWT inválido;
* usuário autenticado sem permissão de administrador.

Para validar a autorização utilizei uma operação protegida da própria API.

O objetivo foi testar a segurança da API sem criar uma suíte adicional de testes de produtos.

---

# 📊 Allure

Utilizei o **Allure** para gerar os resultados da execução dos testes.

Depois da execução:

```bash
mvn clean test
```

os resultados são gerados em:

```text
target/allure-results
```

Na validação local encontrei **21 arquivos de resultado**, correspondentes aos 21 cenários automatizados.

Também confirmei que cenários Cucumber estavam sendo registrados corretamente nos resultados do Allure.

---

# ⚙️ Jenkins

Também integrei o projeto ao Jenkins para validar a execução em um ambiente de CI.

O pipeline está definido no:

```text
Jenkinsfile
```

O fluxo principal é:

```text
GitHub
   ↓
Jenkins
   ↓
Jenkinsfile
   ↓
mvn clean test
   ↓
Cucumber
   ↓
21 cenários
   ↓
Surefire + Allure Results
   ↓
Allure Report
```

O Jenkins também publica os resultados do JUnit e o relatório do Allure.

Durante a validação real do pipeline, confirmei a geração do relatório:

```text
Allure report was successfully generated.
```

e o arquivamento do relatório:

```text
Allure artifact archived via ArtifactManager.
```

A execução finalizou com:

```text
Finished: SUCCESS
```

---

# ▶️ Executando o projeto

## Pré-requisitos

Antes de executar o projeto, preciso ter instalado:

* Java 21;
* Maven;
* Git.

Depois de clonar o projeto:

```bash
git clone https://github.com/marcoslfreire/api-automation.git
```

Acesso o diretório:

```bash
cd api-automation
```

E executo os testes com:

```bash
mvn clean test
```

---

## URL da API

A URL padrão utilizada pela automação é:

```text
https://serverest.dev
```

Também deixei a URL configurável através da propriedade:

```text
baseUrl
```

Por padrão, a aplicação utiliza:

```java
https://serverest.dev
```

Isso permite alterar o ambiente sem precisar modificar as classes da automação.

---

# 🔎 Comportamentos investigados

Durante a implementação, além de automatizar os cenários principais, também investiguei alguns comportamentos específicos da API.

## PUT com ID inexistente

Identifiquei que:

```text
PUT /usuarios/{id}
```

pode realizar o cadastro de um novo usuário quando o ID informado não existe.

Por isso criei o cenário:

```text
CT-005 — Atualizar utilizando ID inexistente
```

Mantive esse comportamento documentado porque ele faz parte do comportamento observado da API.

---

## Rate Limit

Também investiguei o comportamento de rate limit.

Realizei **110 requisições GET sequenciais** para verificar como a API se comportava.

Durante a exploração observei:

* respostas HTTP 200;
* nenhuma resposta HTTP 429;
* nenhum header específico de rate limit identificado;
* nenhum mecanismo de retry observado.

Como não consegui reproduzir o comportamento de limitação durante a execução, não transformei essa investigação em um teste funcional automatizado definitivo.

Mantive o resultado documentado para uma eventual investigação futura.

---

# 🧠 Decisões técnicas

Durante a evolução do projeto tomei algumas decisões importantes.

### Cucumber como camada funcional

Utilizei o Cucumber para representar os comportamentos funcionais da API.

### JUnit Platform como executor

Utilizei o JUnit Platform para executar os cenários Cucumber, evitando manter duas suítes funcionais duplicadas.

### Testes antigos preservados

Os testes JUnit que existiam antes da evolução para Cucumber foram mantidos em:

```text
src/test/java/br/com/qaautomation/teste/legacy
```

Eles servem como referência da implementação anterior, mas não fazem parte da execução funcional principal.

### Cenários independentes

Cada cenário prepara seus próprios dados quando necessário.

Evitei criar dependências entre cenários para que eles possam ser executados individualmente ou dentro do pipeline.

### API Client

Centralizei as chamadas HTTP em classes específicas, evitando duplicação de código nos steps.

### ScenarioContext

Utilizei um contexto para compartilhar informações durante a execução de um cenário.

### Autenticação

Utilizei JWT e uma operação protegida para validar autenticação e autorização.

---

# 📁 Estrutura do projeto

A estrutura principal ficou:

```text
api-automation/
│
├── src/
│   └── test/
│       ├── java/
│       │   └── br/com/qaautomation/teste/
│       │       ├── api/
│       │       ├── config/
│       │       ├── context/
│       │       ├── legacy/
│       │       ├── runners/
│       │       └── steps/
│       │
│       └── resources/
│           └── features/
│               ├── autenticacao/
│               └── usuarios/
│
├── docs/
│   └── arquitetura-e-pipeline-v3.md
│
├── Jenkinsfile
├── pom.xml
└── README.md
```

---

# 🔄 Fluxo completo

De forma resumida, o fluxo que construí foi:

```text
Feature Gherkin
      ↓
Step Definitions
      ↓
ScenarioContext / Utils
      ↓
API Client
      ↓
Rest Assured
      ↓
ServeRest
      ↓
Validações
      ↓
Allure Results
      ↓
Jenkins
      ↓
Allure Report
```

---

# ✅ Resultado final

Ao finalizar essa versão, consegui construir uma automação com:

* **21 cenários automatizados**;
* Java 21;
* Rest Assured;
* JUnit Platform;
* Cucumber;
* Gherkin;
* autenticação JWT;
* validação de autorização;
* isolamento dos cenários;
* Allure;
* Jenkins;
* GitHub;
* documentação técnica.

A execução local foi validada com:

```bash
mvn clean test
```

E também validei a execução completa no Jenkins, incluindo a geração e publicação do relatório Allure.

---

# 👨‍💻 Autor

**Marcos Luciano Freire**

QA Automation / Software Engineer

GitHub:

https://github.com/marcoslfreire

LinkedIn:

https://linkedin.com/in/marcosffreire

---

## 📚 Documentação complementar

Para entender com mais detalhes as decisões e a evolução da automação, consulte:

[Arquitetura e Pipeline V3](docs/arquitetura-e-pipeline-v3.md)

[⬆️ Voltar ao menu](#-navegação)
