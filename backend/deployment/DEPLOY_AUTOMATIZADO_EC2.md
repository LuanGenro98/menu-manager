# 🚀 Guia de Deploy Automatizado na AWS (CloudFormation)

Este documento detalha o processo de deploy **automatizado** da aplicação `menu-manager` no ambiente **AWS Academy Learner Lab Sandbox** usando **Infrastructure as Code (IaC)** com o AWS CloudFormation.

Este método substitui a criação manual da instância EC2. Em vez de 10 cliques, usamos 1 arquivo de template.

---
## 📋 Pré-requisitos

1.  Acesso ao **AWS Academy Learner Lab/Sandbox Academy**.
2.  O arquivo `template.yml` (o template do CloudFormation) deve estar na raiz do seu projeto.
3.  O arquivo `docker-compose.yml` (na pasta `backend/`) deve estar atualizado no seu repositório Git.

---
## ⚙️ Passo 1: Iniciar o Lab e o Console AWS

1.  Faça login no portal **AWS Academy** e entre no seu curso.
2.  Navegue até **Modules** (Módulos) e selecione **Sandbox Academy**.
3.  Clique no botão verde **"Start Lab"** e aguarde o status ficar "Ready".
4.  Clique no botão cinza **"AWS"** para abrir o **AWS Management Console**.

---
## 🤖 Passo 2: Executar o CloudFormation (A Automação)

1.  No Console AWS, verifique se você está na região **N. Virginia (us-east-1)**.
2.  Na barra de busca, procure pelo serviço **CloudFormation**.
3.  Clique em **"Create stack"** (Criar stack) e selecione **"With new resources (standard)"**.
4.  Em "Specify template", escolha a opção **"Upload a template file"**.
5.  Clique em **"Choose file"** e faça o upload do seu arquivo `template.yml` local.
6.  Clique em **"Next"**.
7.  Dê um nome para a stack, por exemplo: `MenuManagerStack`.
8.  Clique em **"Next"** e, na página seguinte, em **"Submit"** (ou "Create stack").

---
## ⏳ Passo 3: Acompanhar o Deploy (A Mágica)

Agora é só aguardar. O CloudFormation fará todo o trabalho pesado:

1.  Você verá o status da sua stack como **`CREATE_IN_PROGRESS`**.
2.  O CloudFormation vai ler seu `template.yml` e criar, em ordem:
    * O Firewall (`MenuManagerSecurityGroup`).
    * A Instância EC2, já com o **KMS (criptografia)** e o **IAM Profile** aplicados.
3.  Assim que a EC2 for criada, o script `UserData` (definido no template) será executado **automaticamente** dentro da máquina. Esse script:
    * Instala Git, Docker e Docker Compose.
    * Clona seu repositório do GitHub.
    * Entra na pasta `backend/`.
    * Roda o `docker-compose up --build -d` para você.

**Este processo leva de 5 a 10 minutos!** A máquina `t3.micro` é fraca e o build do Docker (compilando o Java) demora um pouco.

---
## ✅ Passo 4: Acessar a API (Vitória!)

1.  Aguarde o status da stack no CloudFormation mudar para **`CREATE_COMPLETE`** (verde).
2.  Vá até o console do **EC2**.
3.  Encontre a instância com o nome `menu-manager-server`.
4.  Selecione-a e copie o **"Public IPv4 address"**.
5.  Cole no seu navegador e acesse o Swagger UI:

    `http://<IP_PÚBLICO_DA_EC2>:8080/swagger-ui.html`

Se a página do Swagger carregar, o deploy automatizado foi um sucesso!

---
## (Opcional) Como Acessar a Máquina e Ver os Logs

Se a API não responder após 10 minutos, você pode "entrar" na máquina para investigar o que deu errado.

#### A. Acessando a Instância via SSH

1.  No painel do **Learner Lab** (não no console da AWS), clique em **"Details"** > **"Show"**.
2.  Clique em **"Download PEM"** para baixar o arquivo `labsuser.pem`.
3.  No seu terminal local, execute os comandos:

    ```bash
    # Ajusta a permissão da chave
    chmod 400 labsuser.pem

    # Conecta na máquina
    ssh -i "labsuser.pem" ec2-user@<IP_PÚBLICO_DA_EC2>
    ```

#### B. Verificando os Logs (O Diagnóstico)

Uma vez dentro da máquina, você tem dois logs principais para olhar:

* **Log 1: O Log do Script de Instalação (Cloud-Init)**
  *Este log mostra se a instalação do Docker, Git ou o `git clone` falharam.*
    ```bash
    cat /var/log/cloud-init-output.log
    ```
  *Role até o final para ver os últimos comandos executados.*


* **Log 2: O Log da Aplicação (Docker Compose)**
  *Este log mostra se a sua aplicação Spring Boot subiu corretamente.*
    ```bash
    # Primeiro, entre na pasta onde o docker-compose.yml está
    cd /home/ec2-user/menu-manager/backend/

    # Agora, veja os logs
    docker-compose logs
    ```
  *Aqui você verá os logs do Spring Boot, do Liquibase, etc.*

---
## ⚠️ Atenção: Ambiente Temporário

Lembre-se que este ambiente do AWS Academy Sandbox **é temporário**. Assim que o timer do seu laboratório expirar, **todos os seus dados e sua stack do CloudFormation (incluindo a EC2) serão permanentemente excluídos.**