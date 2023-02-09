# Autenticação Request aplicação Spring Boot com Keycloak

projeto exemplo para realização de autenticação de requisições com o Keycloak
utilizando Client Credentials;

## Dependencias
Para utilização do Keycloak na aplicação Java, deve adicionar as dependencias abaixo:

Gradle:
```groovy
//spring security
implementation 'org.springframework.boot:spring-boot-starter-security:3.0.2'

//Oauth2
implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.0.2'
```

Utilizando o Spring Security para autenticar as requisições e Spring boot Oauth2
para autenticação utilizando o Oauth2.

## Configurar o Keycloak
Para realizar o teste, primeiro deve configurar o Keycloak, deve criar um  realm
para a autenticação com Oauth2.

No Keycloak, deve logar com acesso Admin no realm master para criar o realm acessando a seguinte url: http://localhost:8080/, as credenciais padrão é "**admin**"
para login e senha:

![login_admin](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/keycloak_login_admin.png)

Acessando a plataforma com acesso admin, ao clicar no realm, selecione a opção **Create realm**:

![create_realm](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/criar_realm.png)


Com o Realm criado e selecionado, devemos criar o Client, que será utilizado para a autenticação
com o Keycloak:

![create_client](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/criar_client.png)

Com o Client criado, guarde o valor do Client Secret, pois será usado posteriormente:

![client_credentials](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/client_secrets.png)


No momento de criação do Client, deve marcar as caixas abaixo:

![config_client](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/configuracoes_client.png)

## Execução Aplicação Spring boot
Execute a aplicação local com a sua IDE de preferencia. Com a aplicação no ar, devemos gerar o acces_token para
autorizar a requisição, para isso, deve ter em mãos o client_id e o client_secret obtidos durante a criação do client no Keycloak.
Com as credenciais em mão, no Postman deve realizar uma requisição na seguinte url "http://localhost:8080/realms/<nome-realm-criado>/protocol/openid-connect/token": 

![request_access_token](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/request_access_token.png)

Agora deve copiar o Access Token obtido e adiciona-lo no Header da requisição da aplicação Spring Boot exemplo.
Para validar a autenticação com o access_token deve executar no Postman 
realizando uma requisição no endpoint http://localhost:5000/security e adicionando o header "Authorization" :

![authorized_request](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/authorized_request_java_example.png)


## Referencias

- [Spring boot + Keycloak - protegendo suas APIs (parte 1)](https://dev.to/mmacorin/spring-boot-keycloak-protegendo-suas-apis-parte-1-436o)



