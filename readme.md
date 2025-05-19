# 📦 API de Pagamentos

## ✅ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation (JSR-380)
- H2 Database

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17 ou compatível
- Maven 3.6+

### Executar localmente

Necessário ter maven instalado:
```bash
mvn spring-boot:run
```
Ou rodando o arquivo: PagamentosApiApplication.java

A aplicação estará disponível em:  
http://localhost:8080

---

## 🧪 Acessar Console do H2

- URL: http://localhost:8080/h2-console  
- JDBC URL: `jdbc:h2:mem:pagamentosdb`  
- User: `sa`  
- Password:

---

## 📌 Rotas da API

### 🔹 Criar Pagamento

- **POST** `/pagamentos`
- **Body (JSON)**:
```json
[
  {
    "codigoDebito": 1,
    "identificadorPagador": "00000000000",
    "metodoPagamento": "cartao_credito",
    "numeroCartao": "1234567891234567",
    "valor": 99.99,
    "status": "PENDENTE_PROCESSAMENTO",
    "ativo": true
  }
]

```
- **Validações**:
  - `valor` deve ser positivo.
  - `formaPagamento` aceito apenas (ex: `cartao_debito`, `cartao_credito`, `pix` ou `boleto`).
  - `formaPagamento` usando `cartao_debito` ou `cartao_credito` é obrigatório o número do cartão.
  - `formaPagamento` usando `cartao_debito` é obrigatório o código do débito.

---

### 🔹 Listar Todos os Pagamentos

- **GET** `/pagamentos`
- **Resposta**:
```json
[
	{
		"id": 1,
		"codigoDebito": 1,
		"identificadorPagador": "00000000000",
		"metodoPagamento": "cartao_debito",
		"numeroCartao": "5285870333718642",
		"valor": 99.99,
		"status": "PENDENTE_PROCESSAMENTO",
		"ativo": true
	},
	{
		"id": 2,
		"codigoDebito": 1,
		"identificadorPagador": "00000000000",
		"metodoPagamento": "cartao_debito",
		"numeroCartao": "5285870333718642",
		"valor": 100,
		"status": "PROCESSADO_FALHA",
		"ativo": true
	},
	{
		"id": 3,
		"identificadorPagador": "00000000000",
		"metodoPagamento": "cartao_credito",
		"numeroCartao": "5285870333718642",
		"valor": 299.99,
		"status": "PROCESSADO_SUCESSO",
		"ativo": true
	}
]
```

---

### 🔹 Buscar Pagamento por ID

- **GET**
`/pagamentos?codigoDebito={:codigoDebito}`
`/pagamentos/identificadorPagador={:identificadorPagador}`
`/pagamentos/status={:status}`

---

### 🔹 Atualizar Pagamento

- **PUT** `/pagamentos/{id}`
- **Body (JSON)**:
```json
{
  "status": "PENDENTE_PROCESSAMENTO"
}
{
  "status": "PROCESSADO_FALHA"
}
{
  "status": "PROCESSADO_SUCESSO"
}
```

---

### 🔹 Deletar Pagamento

- **DELETE** `/pagamentos/{id}`

---

## 📂 Estrutura de Pastas (Resumo)

```
src/
 └── main/
     ├── java/com/seuprojeto/
     │    ├── controller/
     │    ├── dto/
     │    ├── entity/
     │    ├── repository/
     │    └── service/
     └── resources/
          ├── application.properties
```

---

## ❗ Observações

- Todos os dados são armazenados em memória e se perdem ao reiniciar a aplicação.
- Validações são aplicadas automaticamente via anotação (`@Valid`) nos DTOs.

<<<<<<< HEAD
---
=======
---
>>>>>>> 624d8a5fc0e88df752f010ece17267aba1b5b4de
