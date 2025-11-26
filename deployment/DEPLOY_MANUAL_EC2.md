# 🚀 Guia de Deploy: Aplicação Spring Boot na AWS EC2 (Academy Sandbox)

Este documento é um guia passo a passo explícito para implantar a aplicação `menu-manager` (backend Spring Boot + banco de dados PostgreSQL) em uma instância EC2, utilizando o ambiente **AWS Academy Sandbox Academy** e o **Docker Compose**.

---

## Passo 1: Iniciar o Laboratório e Baixar a Chave

O ambiente do AWS Academy Sandbox é temporário e requer uma inicialização específica.

1.  **Acesse o AWS Academy** e entre no seu curso.
2.  Navegue até **Modules** (Módulos) e selecione **Sandbox Academy**.
3.  Clique no botão verde **"Start Lab"**. Aguarde o status ficar "Ready" (verde).
4.  Clique no menu **"Details"** (no topo da página) e depois em **"Show"**.
5.  Uma janela de credenciais aparecerá. Clique em **"Download PEM"**. Isso baixará o arquivo `labsuser.pem`. **Este é o seu único par de chaves.** Guarde-o em um local seguro.
6.  Feche a janela de credenciais e clique no botão cinza **"AWS"** para abrir o **AWS Management Console** (o painel de controle da AWS).

---

## Passo 2: Lançar a Instância EC2 (Servidor Virtual)

Agora, dentro do console da AWS, vamos criar nossa máquina virtual.

1.  **Confira a Região**: Verifique no canto superior direito se você está na região **N. Virginia (us-east-1)**. O Sandbox só funciona lá.
2.  **Acesse o EC2**: Na barra de busca de serviços, digite `EC2` e acesse o painel.
3.  **Inicie a Instância**: Clique no botão laranja **"Launch instances"**.
4.  **Configure os Detalhes da Instância**:
    * **Name**: Dê um nome, como `menu-manager-server`.
    * **AMI (Sistema Operacional)**: Mantenha o padrão **Amazon Linux 2023 AMI** (Free tier eligible).
    * **Instance type (Tipo)**: Selecione **`t3.micro`** (Free tier eligible e permitido pelo Sandbox).
    * **Key pair (Par de Chaves)**: **NÃO CRIE UMA NOVA.** No menu dropdown, selecione a chave existente chamada **`vockey`**.
    * **Network settings (Firewall)**:
        * Clique em **"Edit"**.
        * Crie um novo Security Group com o nome `menu-manager-sg`.
        * Em **"Inbound security groups rules"**, adicione as seguintes 3 regras:
          | Tipo | Porta | Fonte | Descrição |
          | :--- | :--- | :--- | :--- |
          | `SSH` | `22` | `Anywhere` (0.0.0.0/0) | Acesso ao terminal |
          | `Custom TCP` | `8080` | `Anywhere` (0.0.0.0/0) | Acesso à nossa API Spring Boot |
          | `PostgreSQL` | `5432` | `Anywhere` (0.0.0.0/0) | Acesso ao banco (opcional, bom p/ debug) |
    * **Storage (Armazenamento) - (USO DO KMS 🔑)**:
        * Na linha "Volume 1 (Root)", clique no botão **"Encrypt"** para marcar.
        * No campo "KMS key", deixe a chave padrão `(aws/ebs)`. (Isso cumpre o requisito de uso do KMS).
    * **Advanced details (Detalhes Avançados) - (USO DO IAM 👤)**:
        * Role para baixo e expanda a seção "Advanced details".
        * Em **"IAM instance profile"**, clique no dropdown e selecione `LabInstanceProfile`. (Isso cumpre o requisito de uso do IAM).
5.  **Launch**: Revise tudo e clique em **"Launch instance"**.

---

## Passo 3: Conectar-se à Instância (SSH)

1.  Aguarde o "Instance state" da sua `menu-manager-server` ficar **"Running"** (verde).
2.  Selecione a instância e copie o **"Public IPv4 address"** (Endereço IP público).
3.  Abra seu terminal local (PowerShell, CMD, ou terminal Linux/Mac).
4.  Navegue até a pasta onde você salvou o arquivo `labsuser.pem`.
5.  Execute os comandos abaixo (substituindo o IP pelo que você copiou):

    ```bash
    # 0. Passo extra para mover a pasta do Windows para dentro do WSL, caso esteja sendo utilizado
    mv /mnt/c/Users/<SEU_USUARIO_AQUI>/Downloads/labsuser.pem .
    
    # 1. Corrija a permissão da chave (necessário em Linux/Mac)
    # No Windows, pode pular este passo se o SSH reclamar.
    chmod 400 labsuser.pem

    # 2. Conecte-se à instância
    # Lembre-se: o usuário padrão do Amazon Linux é 'ec2-user'
    ssh -i "labsuser.pem" ec2-user@<SEU_IP_PÚBLICO_AQUI>
    ```
    Digite `yes` se for a primeira vez que você se conecta.

---

## Passo 4: Preparar o Servidor (Instalar Ferramentas)

Agora que você está dentro do terminal da sua instância EC2, execute este bloco de comandos para instalar o Git, Docker e Docker Compose.

```bash
# 1. Atualiza os pacotes do sistema
sudo yum update -y

# 2. Instala Git e Docker
sudo yum install -y git docker

# 3. Inicia o serviço do Docker
sudo systemctl start docker

# 4. Adiciona o usuário 'ec2-user' ao grupo do Docker (para não precisar usar 'sudo')
sudo usermod -aG docker ec2-user

# 5. Instala o Docker Compose (a versão com hífen)
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 6. IMPORTANTE: Saia e reconecte-se
# Isso é necessário para que a permissão de grupo do Docker seja aplicada.
exit
```

---

## Passo 5: Fazer o Deploy da Aplicação 🐳
Reconecte-se à instância com o mesmo comando ssh do Passo 3.

Clone seu repositório e entre na pasta do projeto:

```bash

git clone https://github.com/LuanGenro98/menu-manager.git
cd menu-manager
# Suba a aplicação! Use o comando docker-compose (com hífen) que aprendemos a usar:

# Constrói a imagem e sobe os containers em segundo plano (-d)
DOCKER_BUILDKIT=0 docker-compose up --build -d
# docker-compose up --build -d

# Verifique o Status:
docker-compose ps
```
Você deve ver os serviços app e db com o status Up ou Running.

---

## Passo 6: Acessar e Testar a API
Sua aplicação está finalmente rodando na nuvem!

Acesse a documentação do Swagger no seu navegador para testar: http://<SEU_IP_PÚBLICO_AQUI>:8080/swagger-ui/index.html

Agora você pode seguir o fluxo de autenticação (registrar, logar, etc.) para validar que tudo está funcionando como esperado.

---
## ⚠️ AVISO: Ambiente Temporário
Lembre-se que este ambiente do AWS Academy Sandbox é temporário. Assim que o timer do seu laboratório expirar, todos os seus dados e sua instância EC2 serão permanentemente excluídos. Este guia é para fins de teste e apresentação, não para uma aplicação em produção.