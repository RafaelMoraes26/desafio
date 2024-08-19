FROM openjdk:21-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Instalar curl
RUN apt-get update && apt-get install -y curl

# Copia o jar da aplicação para o diretório de trabalho no container
COPY build/libs/desafio-0.0.1-SNAPSHOT.jar /app/desafio.jar

# Expõe a porta 8080 para acessar a aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "desafio.jar"]