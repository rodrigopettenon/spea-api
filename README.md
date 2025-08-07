# SPEA - Sistema de Precificação para Empreendimentos Alimentícios

API desenvolvida com Spring Boot para auxiliar pequenos empreendedores do ramo alimentício (como restaurantes, padarias, confeitarias e serviços de delivery) a calcular o custo de produção, precificar corretamente seus produtos e exportar planilhas de controle.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- H2 Database (modo memória)
- Maven

---

## Funcionalidades do MVP

- Cadastro de insumos com valor e quantidade
- Criação de receitas com custo automático com base nos insumos
- Precificação baseada em custo, margem de lucro e taxas
- Exportação de dados para planilhas
- Interface futura com foco mobile-first
- Sem necessidade de login para testes iniciais

---

## Como executar o projeto

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/spea-api.git

# Acesse a pasta do projeto
cd spea-api

# Execute a aplicação (modo dev)
./mvnw spring-boot:run
```

Acesse o H2 Console em `http://localhost:8080/h2-console`  
Use o JDBC URL: `jdbc:h2:mem:spea-db`

---

## Estrutura do projeto

```bash
com.spea.api
├── controllers
├── dtos
├── exceptions
├── models
├── repositories
├── services
└── utils
```

---

## Observações

- Este projeto está em fase de MVP (Produto Mínimo Viável).
- Em breve será implementado o controle multiusuário e login.
- O histórico de dados não é persistido por enquanto.

---

## 📃 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

## ✉️ Contato

Desenvolvido por [Rodrigo Pettenon Rodrigues](https://github.com/rodrigopettenon)
