# =================================================================
# ESTÁGIO 1: O Construtor (Builder)
# =================================================================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copia os arquivos de configuração do Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Adiciona a permissão de execução ao script do Maven Wrapper
RUN chmod +x ./mvnw

# Baixa todas as dependências do projeto
RUN ./mvnw dependency:go-offline

# Copia o código-fonte da sua aplicação
COPY src ./src

# Compila o projeto e gera o arquivo .jar
RUN ./mvnw clean package -DskipTests


# =================================================================
# ESTÁGIO 2: A Imagem Final (Runner)
# =================================================================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]