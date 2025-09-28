# 🍔 Menu Manager API

Bem-vindo à API do Menu Manager! Esta é uma API RESTful completa construída com Spring Boot para gerenciar cardápios de restaurantes, incluindo categorias, itens, pedidos e autenticação de usuários.

---
## ✨ Features

* **Autenticação e Autorização**: Sistema de segurança robusto usando Spring Security e JWT.
* **Gerenciamento de Cardápio**: CRUD completo para Categorias e Itens.
* **Gerenciamento de Pedidos**: Criação de novos pedidos (demands).
* **Gerenciamento de Usuários**: Registro de novos usuários com diferentes perfis (roles).
* **Ambiente Dockerizado**: Configuração completa com Docker Compose para rodar a aplicação e o banco de dados com um único comando.
* **Migrações de Banco de Dados**: Schema do banco gerenciado via Liquibase, garantindo consistência.
* **Documentação Interativa**: API 100% documentada com OpenAPI 3.0 (Swagger UI).

---
## 🛠️ Tech Stack

* **Backend**: Java 21, Spring Boot 3
* **Segurança**: Spring Security, JSON Web Tokens (JWT)
* **Banco de Dados**: PostgreSQL
* **Gerenciamento de BD**: Liquibase
* **Containerização**: Docker & Docker Compose
* **Build**: Maven
* **Documentação**: Springdoc OpenAPI (Swagger)

---
## 🚀 Como Rodar o Projeto

Siga estes passos para ter o ambiente completo rodando na sua máquina.

### Pré-requisitos
* [Docker](https://www.docker.com/products/docker-desktop/)
* [Docker Compose](https://docs.docker.com/compose/install/) (geralmente já vem com o Docker Desktop)
* [Java 21](https://www.oracle.com/java/technologies/downloads/#java21) (ou superior)
* [Maven](https://maven.apache.org/download.cgi)

### Passos para a Instalação

1.  **Clone o Repositório**
    ```bash
    git clone https://github.com/LuanGenro98/menu-manager
    cd menu-manager
    ```

2.  **Configure a Chave Secreta do JWT**
    Crie o arquivo `application.properties` dentro de `src/main/resources/`. Você pode copiar o conteúdo do arquivo `application.properties.example` (se houver) ou criar um novo com o conteúdo abaixo.

    **Importante**: Gere sua própria chave secreta! Abra um terminal e rode o comando abaixo para gerar uma chave segura:
    ```bash
    openssl rand -base64 32
    ```
    Copie o resultado e cole no arquivo.

    **`src/main/resources/application.properties`**:
    ```properties
    # Cole a chave secreta gerada pelo comando openssl aqui
    jwt.secret-key=SUA_CHAVE_GERADA_AQUI_EXEMPLO:Fq2/s8D+A4zG8L5N2aE9...
    ```

3.  **Construa e Suba os Containers**
    Na raiz do projeto, execute o seguinte comando. Ele irá construir a imagem da sua aplicação e iniciar os containers do app e do banco de dados.
    ```bash
    docker compose up --build
    ```
    Aguarde a inicialização completa. Você verá os logs da aplicação Spring Boot no seu terminal.

4.  **Pronto!**
    * 🚀 Sua **API** estará disponível em: `http://localhost:8080`
    * 🐘 Seu **banco de dados PostgreSQL** estará disponível em: `localhost:5432`

---
## 📚 Documentação da API (Swagger)

A documentação completa e interativa da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse:

* **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

Lá você poderá ver todos os endpoints, seus parâmetros, os corpos de requisição/resposta e testá-los diretamente.

---
## 🔑 Fluxo de Autenticação (Guia para o Frontend)

Para consumir os endpoints protegidos, o frontend precisa seguir este fluxo:

### **Passo 1: Registrar um Usuário (se necessário)**
Este endpoint é público e pode ser usado para criar novos usuários de teste.

* **Endpoint**: `POST /api/v1/users/register`
* **Corpo da Requisição (JSON)**:
    ```json
    {
      "username": "frontend_dev",
      "password": "password123",
      "role": "USER"
    }
    ```

### **Passo 2: Autenticar e Obter o Token**
O usuário envia suas credenciais para obter um token de acesso.

* **Endpoint**: `POST /api/v1/auth/login`
* **Corpo da Requisição (JSON)**:
    ```json
    {
      "username": "frontend_dev",
      "password": "password123"
    }
    ```
* **Resposta de Sucesso (JSON)**:
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmcm9udGVuZF9kZXYiLCJpYXQiOjE3Mjc1NzAzMDcsImV4cCI6MTcyNzY1NjcwN30.xxxxxxxxxxxx"
    }
    ```
  Guarde este `token` no `localStorage` ou `sessionStorage` do navegador.

### **Passo 3: Acessar Endpoints Protegidos**
Para todas as futuras requisições a endpoints que exigem autenticação, inclua o token no cabeçalho `Authorization`.

* **Formato do Cabeçalho**:
    ```
    Authorization: Bearer <o_token_jwt_que_voce_recebeu>
    ```
* **Exemplo com `curl`**:
    ```bash
    curl -X GET http://localhost:8080/api/v1/categories \
    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmcm9udGVuZF9kZXYiLCJpYXQiOjE3Mjc1NzAzMDcsImV4cCI6MTcyNzY1NjcwN30.xxxxxxxxxxxx"
    ```

### **Passo 4: Logout**
Para fazer logout, simplesmente remova o token do `localStorage` do navegador.

---
### **Observação sobre CORS**
O backend está configurado para aceitar requisições do frontend rodando em `http://localhost:3000`. Se o seu ambiente de desenvolvimento do frontend usar uma porta diferente, avise o desenvolvedor do backend para ajustar a configuração.