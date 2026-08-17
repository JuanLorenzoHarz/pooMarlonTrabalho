# Loja Virtual — Backend Java (POO)

Backend REST sem frontend para o trabalho de Programação Orientada a Objetos. O projeto parte da modelagem **Cliente -> Pedido -> Produto** e amplia o domínio com **Carrinho** e **Pagamento**.

## Tecnologias
- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- H2 em memória (padrão)
- PostgreSQL opcional
- JUnit 5

## Como executar
```bash
mvn spring-boot:run
```
A API sobe em `http://localhost:8080`. O H2 Console fica em `/h2-console`.

Para PostgreSQL:
```bash
SPRING_PROFILES_ACTIVE=postgres DATABASE_URL=jdbc:postgresql://localhost:5432/lojavirtual DATABASE_USER=postgres DATABASE_PASSWORD=postgres mvn spring-boot:run
```

## Endpoints
### Clientes
- `POST /api/clientes` cria cliente
- `GET /api/clientes` lista clientes
- `GET /api/clientes/{id}` busca cliente
- `PUT /api/clientes/{id}` atualiza cliente
- `DELETE /api/clientes/{id}` exclui cliente

### Produtos
- `POST /api/produtos` cria produto
- `GET /api/produtos?apenasAtivos=true` lista produtos
- `GET /api/produtos/{id}` busca produto
- `PUT /api/produtos/{id}` atualiza produto
- `DELETE /api/produtos/{id}` desativa produto

### Carrinho
- `GET /api/clientes/{clienteId}/carrinho`
- `POST /api/clientes/{clienteId}/carrinho/itens`
- `DELETE /api/clientes/{clienteId}/carrinho/itens/{produtoId}`
- `DELETE /api/clientes/{clienteId}/carrinho`

### Pedidos
- `POST /api/pedidos`
- `GET /api/pedidos`
- `GET /api/pedidos?clienteId=1`
- `GET /api/pedidos/{id}`
- `PATCH /api/pedidos/{id}/status?status=ENVIADO`
- `POST /api/pedidos/{id}/cancelamento`

### Pagamento
- `POST /api/pedidos/{pedidoId}/pagamento`
- `GET /api/pedidos/{pedidoId}/pagamento`
- `POST /api/pedidos/{pedidoId}/pagamento/aprovacao`
- `POST /api/pedidos/{pedidoId}/pagamento/recusa`
- `POST /api/pedidos/{pedidoId}/pagamento/reembolso`

## Exemplos JSON
Criar cliente:
```json
{"nome":"Juan Lorenzo","cpf":"12345678901","email":"juan@example.com","endereco":"Rua Exemplo, 123"}
```
Criar produto:
```json
{"nome":"Teclado","descricao":"Teclado mecânico","preco":249.90,"estoque":10,"ativo":true}
```
Criar pedido:
```json
{"clienteId":1,"itens":[{"produtoId":1,"quantidade":2}],"frete":20.00}
```
Criar pagamento:
```json
{"forma":"PIX"}
```

## Regras principais
- CPF e email de cliente não podem repetir.
- Produto inativo não pode entrar em pedido.
- Pedido verifica e baixa o estoque no momento da criação.
- Cancelamento devolve os itens ao estoque.
- Pedido entregue não pode ser cancelado.
- Um pedido possui no máximo um pagamento.
- Aprovar pagamento muda pedido `CRIADO` para `PAGO`.
- Reembolso cancela o pedido e devolve o estoque.

## Estrutura
`domain` contém as classes/objetos; `repository` cuida da persistência; `service` concentra regras de negócio; `controller` expõe endpoints; `dto` valida entradas; `exception` padroniza erros.
