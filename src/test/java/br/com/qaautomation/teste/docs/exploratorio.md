## Documentação da Exploração e Contrato da API — ServeRest

# 1. Objetivo

Esta documentação registry a análise exploratória realizada sobre a API ServeRest utilizada no projeto de Automação de Testes de API.



O objetivo é estabelecer a rastreabilidade entre:



Documentação da API → requisito → endpoint → entrada → comportamento esperado → comportamento observado → automação



Também é importante diferenciar:



Comportamentos definidos no contrato/documentação oficial da API;

Comportamentos identificados durante a exploração;

Comportamentos posteriormente automatizados nos testes.

# 2. API utilizada

API: ServeRest
URL base: https://serverest.dev



A API simula uma aplicação de comércio eletrónico e disponibiliza recursos para estudo e automação de testes de APIs REST.



O projeto utiliza principalmente os recursos relacionados a:



Usuários;

Autenticação;

Produtos;

JWT.

# 3. Estratégia de exploração

A exploração foi realizada antes e durante a construção da automação.



Para cada funcionalidade foram analisados:



Endpoint;

Método HTTP;

Dados de entrada;

Código de resposta;

Corpo da resposta;

Mensagens retornadas;

Cenários positivos;

Cenários negativos;

Comportamentos de borda;

Possibilidade de automação.



A exploração também foi utilizada para identificar comportamentos que não estavam inicialmente evidentes apenas pela leitura dos requisitos.

# 4. Cadastro de usuários

4.1 Endpoint

POST /usuarios


4.2 Objetivo

Cadastrar um novo usuário na API.

4.3 Estrutura esperada

O cadastro utiliza os campos:

{
"nome": "Fulano da Silva",
"email": "usuario@teste.com",
"password": "123456",
"administrador": "true"
}


O campo administrador utiliza os valores:

"true"
"false"


4.4 Cenário positivo

Entrada

Usuário contendo todos os campos obrigatórios e com dados válidos.

Comportamento esperado

A API deve retornar:

HTTP 201


Com uma resposta contendo a mensagem de sucesso e o identificador do usuário.



Exemplo:

{
"message": "Cadastro realizado com sucesso",
"_id": "..."



Comportamento observado

Durante a exploração, o comportamento esperado foi confirmado.

Automação

O cenário foi automatizado em:

UserCreationTest.deveCriarUsuarioComSucesso()


Também existe cobertura equivalente na camada Cucumber:

Scenario: Criar usuário com sucesso


5. Cadastro com e-mail duplicado

5.1 Regra

A API não permite cadastrar dois usuários utilizando o mesmo endereço de e-mail.

5.2 Estratégia exploratória

Foi realizado:



Cadastro do primeiro usuário;

Reutilização do mesmo corpo da requisição;

Nova tentativa de cadastro.

5.3 Resultado esperado

O primeiro cadastro deve ser realizado com sucesso.



A segunda tentativa deve ser rejeitada.

5.4 Comportamento observado

A API retorna:

HTTP 400


Com a mensagem:

Este email já está sendo usado


5.5 Automação

UserValidationTest.deveImpedirCadastroComEmailDuplicado()


Também existe o cenário Cucumber:

Scenario: Impedir cadastro com email duplicado


6. Validações obrigatórias do cadastro

Durante a exploração foram avaliadas as situações em que campos obrigatórios são removidos da requisição.

6.1 Nome ausente

Cenário

Cadastro sem o campo nome.

Resultado observado

HTTP 400


Mensagem:

Nome é obrigatório


Automação

UserValidationTest.deveImpedirCadastroSemNome()


6.2 E-mail ausente

Cenário

Cadastro sem o campo email.

Resultado observado

HTTP 400


Mensagem:

email é obrigatório


Automação

UserValidationTest.deveImpedirCadastroSemEmail()


6.3 Password ausente

Cenário

Cadastro sem o campo password.

Resultado observado

HTTP 400


Mensagem:

password é obrigatório


Automação

UserValidationTest.deveImpedirCadastroSemPassword()


6.4 Administrador ausente

Cenário

Cadastro sem o campo administrador.

Resultado observado

HTTP 400


Mensagem:

Administrador é obrigatório


Automação

UserValidationTest.deveImpedirCadastroSemAdministrador()


6.5 Administrador inválido

Cenário

Foi enviado:

"administrador": "valor-invalido"


Resultado observado

HTTP 400


Mensagem:

Administrador deve ser 'true' ou 'false'


Automação

UserValidationTest.deveImpedirCadastroComAdministradorInvalido()


7. Validação do formato do e-mail

Cenário

Foi enviado um e-mail em formato inválido:

email-invalido


Resultado observado

HTTP 400


Mensagem:

email deve ser um email válido


Automação

UserValidationTest.deveImpedirCadastroComEmailInvalido()


8. Consulta de usuários

8.1 Listagem

Endpoint

GET /usuarios


Cenário

Consultar a lista de usuários cadastrados.

Resultado esperado

HTTP 200


A resposta contém a quantidade de registros e a lista de usuários.

Automação

UserTest.deveListarUsuariosComSucesso()


9. Consulta de usuário por e-mail

Endpoint

GET /usuarios?email={email}


Estratégia

Criar um usuário;

Recuperar o e-mail utilizado;

Realizar uma consulta utilizando o parâmetro email;

Validar o resultado.

Resultado observado

HTTP 200


A consulta retorna o usuário correspondente.

Automação

UserTest.deveBuscarUsuarioPorEmail()


Cucumber:

Scenario: Buscar usuário por email


10. Consulta por ID

Endpoint

GET /usuarios/{_id}


10.1 Usuário existente

Após criar um usuário, o _id retornado pelo cadastro é utilizado para realizar a consulta.

Resultado esperado

HTTP 200


Os dados retornados devem corresponder ao usuário criado.

10.2 Usuário inexistente

Foi utilizado um identificador que não corresponde a um usuário cadastrado.

Resultado observado

HTTP 400


Mensagem:

Usuário não encontrado


Automação

UserTest.deveRetornarErroAoBuscarUsuarioInexistente()


Cucumber:

Scenario: Buscar usuário inexistente


11. Atualização de usuário

Endpoint

PUT /usuarios/{_id}


11.1 Usuário existente

Estratégia

Criar usuário;

Recuperar _id;

Enviar novos dados utilizando PUT;

Consultar novamente o usuário;

Validar os dados atualizados.

Resultado observado

HTTP 200


Mensagem:

Registry alterado com sucesso


Posteriormente, o GET confirma os novos dados.

Automação

UserTest.deveAtualizarUsuarioComSucesso()


12. Comportamento especial do PUT com ID inexistente

Este foi um dos comportamentos mais importantes identificados durante a exploração.

Endpoint

PUT /usuarios/{_id}


Cenário

Foi enviado um ID que não correspondia a nenhum usuário existente.



Inicialmente, poderia ser esperado que a API retornasse um erro de usuário inexistente.



Entretanto, a documentação do próprio endpoint informa que, quando o usuário não é encontrado pelo ID informado, a API realiza um novo cadastro em vez de uma alteração.

Comportamento observado

HTTP 201


Mensagem:

Cadastro realizado com sucesso


Portanto:

PUT + ID inexistente
↓
novo cadastro
↓
HTTP 201


Automação

JUnit:

UserTest.deveCriarUsuarioAoAtualizarIdInexistente()


Cucumber:

Scenario: Atualizar usuário com id inexistente


Importância para o QA

Esse caso demonstra a importância da exploração além dos cenários tradicionais de CRUD.



A operação possui um comportamento de borda que precisa de ser conhecido para que o teste não seja criado com uma expectativa incorreta.

13. Exclusão de usuário

Endpoint

DELETE /usuarios/{_id}


13.1 Usuário existente

Estratégia

Criar usuário;

Recuperar _id;

Excluir o usuário;

Consultar novamente o ID.

Resultado observado

A exclusão retorna:

HTTP 200


Mensagem:

Registry excluído com sucesso


Ao consultar posteriormente o mesmo ID:

HTTP 400


com:

Usuário não encontrado


Automação

UserTest.deveExcluirUsuarioComSucesso()


14. Exclusão de usuário inexistente

Cenário

Foi utilizado um ID inexistente numa requisição:

DELETE /usuarios/{_id}


Resultado observado

HTTP 200


Mensagem:

Nenhum registry excluído


Automação

UserTest.deveRetornarNenhumRegistroAoExcluirUsuarioInexistente()


15. Autenticação

Endpoint

POST /login


Estratégia exploratória

O fluxo utilizado foi:

Criar usuário administrador
↓
POST /login
↓
Obter JWT
↓
Utilizar JWT em operação protegida


Login com credenciais válidas

O login retorna:

HTTP 200


E o campo:

authorization


Contendo o token.

Automação

LoginTest.deveRealizarLoginComSucesso()


16. Login com senha incorreta

Cenário

Criar usuário;

Utilizar o e-mail correto;

Informar uma senha diferente da cadastrada.

Resultado observado

HTTP 401


Mensagem:

Email e/ou senha inválidos


Automação

LoginTest.deveImpedirLoginComSenhaIncorreta()


17. JWT e operações protegidas

A exploração também validou o uso do token recebido pelo login.



Fluxo:

POST /usuarios
↓
criação do usuário administrador
↓
POST /login
↓
recebimento do JWT
↓
Authorization: Bearer <token>
↓
operação protegida


No projeto, esse comportamento é centralizado em:

AutenticaçãoApi


e:

AuthRequest.comToken(...)


Isso evita que cada teste precise de implementar novamente a lógica de autenticação.

Automação

JwtAuthenticationTest.deveAutenticarUsuarioAdministrador()


e:

JwtAuthenticationTest.deveUtilizarJwtEmOperacaoProtegida()


18. Validade do token

A documentação oficial da API informa que o token de autenticação possui validade de aproximadamente:

600 segundos


ou:

10 minutos


Após esse período, o token pode deixar de ser válido e a API pode retornar:

HTTP 401


Esse comportamento é importante para uma possível evolução da cobertura de testes.

19. Rate Limit

O requisito de rate limit também foi investigado durante a exploração.

Estratégia

Foi realizada uma execução com:

110 requisições sequenciais


para:

GET /usuarios


Resultado

Foram observadas:

110 respostas HTTP 200


Não foi observado:

HTTP 429


Também não foram identificados headers de rate limit ou Retry-After nessa execução.

Conclusão

Não foi possível reproduzir o comportamento de bloqueio por limite de requisições nesse ambiente e intervalo de execução.



Por isso, o projeto não criou uma automação afirmando que determinado número de requisições deveria resultar em 429.



A decisão foi baseada em evidência observada:

Não foi observado o comportamento de rate limit durante a exploração realizada.

Isso evita criar uma expectativa de teste sem evidência suficiente.

20. Diferença entre contrato documentado e comportamento observado

Essa distinção é importante na documentação do projeto.

Contrato documentado

São comportamentos encontrados diretamente na documentação/OpenAPI da API.



Exemplos:



endpoints disponíveis;

métodos HTTP;

Campos de cadastro;

Administrador utilizando true ou false;

HTTP 201 no cadastro;

e-mail duplicado não permitido;

Comportamento especial do PUT com ID inexistente;

Autenticação utilizando Authorization;

Validade do token.

Comportamento observado

São comportamentos confirmados através da execução dos testes exploratórios.



Exemplos:

Nome é obrigatório
email é obrigatório
password é obrigatório
administrador é obrigatório
email deve ser um email válido
administrador deve ser 'true' ou 'false'


Também entram nessa categoria os resultados efetivamente observados durante os testes, como:

110 GETs → 110 respostas 200


Quando o rate limit estava sendo investigado.

21. Relação entre exploração e automação

A exploração serviu como base para a criação dos testes automatizados.



O fluxo adotado foi:

Documentação / requisito
↓
Exploração manual
↓
Identificação do comportamento
↓
Definição do cenário
↓
Automação
↓
Validação no Maven
↓
Execução no Jenkins
↓
Relatório Allure


Isso permite que a automação não seja apenas uma coleção de requisições, mas uma representação dos comportamentos relevantes da API.

22. Matriz resumida de cobertura

Área

Cenários principais

Automação

Cadastro

Sucesso

JUnit + Cucumber

Cadastro

E-mail duplicado

JUnit + Cucumber

Cadastro

Campos obrigatórios

JUnit + Cucumber

Cadastro

E-mail inválido

JUnit + Cucumber

Cadastro

Administrador inválido

JUnit + Cucumber

Consulta

Listagem

JUnit

Consulta

Busca por e-mail

JUnit + Cucumber

Consulta

Busca por ID

JUnit + Cucumber

Atualização

Usuário existente

JUnit + Cucumber

Atualização

ID inexistente

JUnit + Cucumber

Exclusão

Usuário existente

JUnit + Cucumber

Exclusão

Usuário inexistente

JUnit + Cucumber

Login

Credenciais válidas

JUnit

Login

Credenciais inválidas

JUnit

JWT

Obtenção do token

JUnit

JWT

Operação protegida

JUnit

Rate Limit

Investigação exploratória

Não automatizado

23. Conclusão

A exploração da API permitiu identificar tanto os comportamentos definidos no contrato quanto comportamentos observados diretamente durante a execução.



Um dos principais exemplos foi o PUT /usuarios/{_id} com ID inexistente, em que a API realiza um novo cadastro. Esse comportamento foi inicialmente identificado durante a exploração e posteriormente confirmado na documentação oficial do endpoint.



Outro ponto importante foi o rate limit. Embora esse requisito tenha sido investigado, o comportamento de bloqueio não foi reproduzido durante a execução realizada. Por isso, ele foi documentado como uma investigação exploratória e não como uma expectativa automatizada.



A estratégia adotada no projeto foi:



Não automatizar uma expectativa apenas porque ela parece fazer sentido; primeiro buscar evidência no contrato ou no comportamento real da API.



Dessa forma, a matriz de testes mantém rastreabilidade entre:

Requisito
↓
Endpoint
↓
Cenário
↓
Pré-condição
↓
Resultado esperado
↓
Automação
↓
Execução no CI
↓
Evidência / relatório


Essa rastreabilidade permite explicar não apenas o que foi automatizado, mas também por que cada cenário existe e de onde veio a expectativa validada.