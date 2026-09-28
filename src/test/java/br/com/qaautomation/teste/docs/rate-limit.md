# Validação de Rate Limit

## 1. Objetivo

Registrar a análise realizada sobre o requisito de rate limit definido no desafio de Automação de Testes de API.

O desafio informa um limite de **100 requisições por minuto** para a API.

O objetivo desta etapa foi verificar se esse comportamento poderia ser validado de forma confiável no ambiente público utilizado pelo projeto.

---

## 2. Requisito analisado

### Rate Limit

- Limite informado no desafio: **100 requisições por minuto**
- API utilizada: ServeRest
- Endpoint utilizado na exploração: `GET /usuarios`

---

## 3. Análise realizada

Antes de implementar um teste automatizado, foram realizadas verificações exploratórias para identificar o comportamento real da API.

### 3.1 Busca no projeto

Foi realizada uma busca por referências a rate limit:

```bash
grep -R -i "rate\|limit\|100.*min" src/test README.md pom.xml