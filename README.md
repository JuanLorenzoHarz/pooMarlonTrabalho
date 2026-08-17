# Loja Virtual — Backend Java (POO)

Backend REST sem frontend para o trabalho de Programação Orientada a Objetos. O sistema modela clientes, produtos, categorias, carrinho, pedidos, descontos, pagamentos, parcelamento e vendas.

## Tecnologias
- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- H2 em memória por padrão
- PostgreSQL opcional
- JUnit 5

## Executar
```bash
mvn test
mvn spring-boot:run
```
API: `http://localhost:8080`

## Catálogo e categorias
### Categorias
- `POST /api/categorias`
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}` desativa a categoria

Exemplo:
```json
{"nome":"Informática","descricao":"Periféricos e componentes","ativa":true}
```

### Produtos
- `POST /api/produtos`
- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}` desativa o produto

Produto com categoria:
```json
{"nome":"Teclado","descricao":"Teclado mecânico","preco":249.90,"estoque":10,"ativo":true,"categoriaId":1}
```

### Busca e filtros
`GET /api/produtos/busca`

Parâmetros opcionais:
- `termo`: pesquisa em nome e descrição
- `categoriaId`
- `precoMin`
- `precoMax`
- `emEstoque=true`
- `ativo=true|false`
- `ordenarPor=nome|preco|estoque|id`
- `direcao=asc|desc`

Exemplo:
```text
/api/produtos/busca?termo=teclado&categoriaId=1&precoMin=100&precoMax=500&emEstoque=true&ordenarPor=preco&direcao=asc
```

## Cupons e descontos
- `POST /api/cupons`
- `GET /api/cupons`
- `GET /api/cupons/{id}`
- `PATCH /api/cupons/{id}?ativo=true&limiteUsos=100`

Tipos: `PERCENTUAL` e `VALOR_FIXO`.

Exemplo:
```json
{"codigo":"POO10","tipo":"PERCENTUAL","valor":10,"minimoPedido":100,"validade":"2026-12-31T23:59:59","limiteUsos":50,"ativo":true}
```

## Pedidos
- `POST /api/pedidos`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `PATCH /api/pedidos/{id}/status?status=ENVIADO`
- `POST /api/pedidos/{id}/cancelamento`

Pedido com cupom:
```json
{
  "clienteId":1,
  "itens":[{"produtoId":1,"quantidade":2}],
  "frete":20.00,
  "codigoCupom":"POO10"
}
```

O pedido retorna `subtotal`, `desconto`, `frete`, `codigoCupom` e `total`.

## Pagamento, juros e parcelas
- `POST /api/pedidos/{pedidoId}/pagamento`
- `GET /api/pedidos/{pedidoId}/pagamento`
- `GET /api/pedidos/{pedidoId}/pagamento/simulacao?forma=CARTAO_CREDITO&parcelas=6`
- `POST /api/pedidos/{pedidoId}/pagamento/aprovacao`
- `POST /api/pedidos/{pedidoId}/pagamento/recusa`
- `POST /api/pedidos/{pedidoId}/pagamento/reembolso`

Exemplo:
```json
{"forma":"CARTAO_CREDITO","parcelas":6}
```

Política implementada:
- PIX, débito e boleto: pagamento à vista
- cartão de crédito: 1 a 12 parcelas
- 1 a 3 parcelas: 0% de juros
- 4 a 6 parcelas: 1,5% ao mês
- 7 a 12 parcelas: 2% ao mês
- juros compostos durante a quantidade de parcelas

O pagamento informa valor sem juros, juros, taxa mensal, valor final e valor de cada parcela.

## Vendas
São considerados vendas os pedidos em `PAGO`, `EM_PREPARACAO`, `ENVIADO` ou `ENTREGUE`.

- `GET /api/vendas`
- `GET /api/vendas?inicio=2026-08-01T00:00:00&fim=2026-08-31T23:59:59`
- `GET /api/vendas/resumo`
- `GET /api/vendas/resumo?inicio=2026-08-01T00:00:00&fim=2026-08-31T23:59:59`

O resumo contém quantidade de vendas, quantidade de itens vendidos, faturamento e ticket médio.

## Regras principais
- CPF e email de cliente não podem repetir.
- Produtos podem ser classificados em categorias.
- Produtos podem ser pesquisados e filtrados por vários critérios ao mesmo tempo.
- Pedido baixa estoque ao ser criado e cancelamento repõe estoque.
- Cupom pode ter validade, mínimo de compra e limite de usos.
- Desconto nunca ultrapassa o subtotal dos produtos.
- Apenas cartão de crédito aceita parcelas.
- O endpoint de simulação permite consultar juros antes de criar o pagamento.
- Venda exclui pedidos criados sem pagamento e pedidos cancelados.
