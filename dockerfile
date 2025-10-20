# Usa a imagem base do Tomcat 10 com OpenJDK 17
FROM tomcat:10.1.0-jdk17

# Define o diretório onde o Tomcat deploya as aplicações
WORKDIR /usr/local/tomcat/webapps/

# Copia o arquivo WAR da aplicação para o diretório webapps do Tomcat
COPY ./target/siloapi.war ./siloapi.war

#FROM eclipse-temurin:17-jdk-alpine
#
## Crie um diretório para a aplicação
#WORKDIR /app
#
## Copie o arquivo .war para o container
#COPY ./target/sirenev1.war /app/sirenev1.war

# Expor a porta 8080 para acessar o Tomcat
EXPOSE 8080

# Executa o Tomcat na inicialização do container
CMD ["catalina.sh", "run"]
# Execute o .war usando Java
#CMD ["java", "-jar", "/app/sirenev1.war"]
