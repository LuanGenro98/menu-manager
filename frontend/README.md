# 🚀 Como Rodar o Projeto (Frontend)

Bem-vindo ao guia oficial para executar o frontend do **Menu Manager** utilizando Docker!  
Siga os passos abaixo e tenha o ambiente funcionando em poucos minutos.

---

## ✅ 1. PRÉ-REQUISITOS

- Git instalado  
- Docker e Docker Compose instalados  
- Editor de texto (VSCode, IntelliJ, etc.) *(opcional)*  

---

## 📥 2. CLONAR O REPOSITÓRIO

Abra o terminal e execute:

```
git clone https://github.com/LuanGenro98/menu-manager
cd menu-manager/frontend
```

---

## ⚙️ 3. CONFIGURAR VARIÁVEIS DE AMBIENTE

Crie seu arquivo `.env` baseado no arquivo de exemplo:

```
cp .env.example .env
```

Edite o arquivo `.env` e defina os valores necessários:

```
API_URL=http://localhost:8000
API_KEY=sua_chave_aqui
```

---

## 🐳 4. BUILDAR E INICIAR O DOCKER

No diretório `frontend`, execute:

```
docker compose up --build
```

Para rodar em segundo plano:

```
docker compose up --build -d
```

---

## 🌐 5. ACESSAR A APLICAÇÃO

Após o container iniciar, acesse:

```
http://localhost:3000
```

---

## 🛑 6. PARAR O PROJETO

Para derrubar os containers:

```
docker compose down
```

---

## 📝 7. OBSERVAÇÕES IMPORTANTES

- Certifique-se de que a porta **3000** não esteja em uso.  
- Caso ocorra algum erro, veja os logs com:

```
docker compose logs -f
```

---

## 🧩 8. PROBLEMAS COMUNS

| Problema                     | Solução                                                                 |
|------------------------------|-------------------------------------------------------------------------|
| Porta 3000 em uso            | Finalize o processo ou altere a porta no `docker-compose.yml`.          |
| Erro ao conectar com API     | Verifique as variáveis do arquivo `.env`.                               |
| Containers não iniciam       | Execute `docker compose down -v` e tente novamente.                      |
| Frontend não carrega         | Verifique se o backend está rodando e acessível na URL da API.          |

---

🎉 **Pronto! Seu ambiente está rodando com Docker de forma rápida e prática.**  
Se precisar melhorar o README ou gerar versão em PDF/Markdown, posso ajudar!