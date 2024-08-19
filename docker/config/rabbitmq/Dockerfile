##### Configuração do RabbitMQ

# Usando a imagem oficial do RabbitMQ
FROM rabbitmq:3-management

# Instala o curl
RUN apt-get update && apt-get install -y curl

# Baixa o rabbitmqadmin
RUN curl -O https://raw.githubusercontent.com/rabbitmq/rabbitmq-server/v3.12.x/deps/rabbitmq_management/bin/rabbitmqadmin && \
    chmod +x rabbitmqadmin && \
    mv rabbitmqadmin /usr/local/bin/

# Adiciona o script de inicialização
COPY init.sh /init.sh

# Concede permissão de execução ao script
RUN chmod +x /init.sh

# Define o comando de inicialização
CMD ["sh", "-c", "/init.sh"]
