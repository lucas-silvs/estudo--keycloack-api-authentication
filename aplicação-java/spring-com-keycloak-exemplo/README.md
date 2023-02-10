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

## Configuração Keycloak para autenticação Client Credentials
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

### Execução Aplicação Spring boot
Para iniciar com as configurações corretas, precisamos alterar o Spring Profile da aplicação par "clientCredential" para subir com as configurações para autenticar utilizando Client Credentials.

Com a aplicação no ar, devemos gerar o acces_token para
autorizar a requisição, para isso, deve ter em mãos o client_id e o client_secret obtidos durante a criação do client no Keycloak.
Com as credenciais em mão, no Postman deve realizar uma requisição na seguinte url "http://localhost:8080/realms/<nome-realm-criado>/protocol/openid-connect/token": 

![request_access_token](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/request_access_token.png)

Agora deve copiar o Access Token obtido e adiciona-lo no Header da requisição da aplicação Spring Boot exemplo.
Para validar a autenticação com o access_token deve executar no Postman 
realizando uma requisição no endpoint http://localhost:5000/security e adicionando o header "Authorization" :

![authorized_request](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/authorized_request_java_example.png)


## Configuração Keycloak para autenticação username password

Outra forma de realizar autenticação é utilizando credenciais de usuários criados no Keycloak, utilizando Roles para granular o acesso.
Primeiro devemos criar duas roles, user e admin:

![create_role](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/create_role.png)

![create_role_user](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/create_role_user.png)

Com as roles devidamente criadas, precisamos criar um usuário onde iremos associar, é importante salientar que devemos adicionar um email,
que será utilizado para solicitar o acces token posteriormente:

![create_user](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/create_user.png)

Com o usuário, precisamos criar as credenciais que utilizaremos para se autenticar:

![create_credentials](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/create_credentials.png)

Agora é necessário associar a role ao usuário criado:

![attach_role_to_user](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/attach_role_to_user.png)

Pronto, as configurações no Keycloak estão finalizadas.

### Execução aplicação Spring Boot

Para iniciar com as configurações corretas, precisamos alterar o Spring Profile da aplicação par "jwt" para subir com as configurações para tratar o token JWT e validar as roles.

![spring_profile_jwt](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/config_spring_profile_jwt.png)

Primeiro devemos solicitar o acces_token para o Keycloak utilizando o email e a senha do usuário criado:

![request_acces_token_with_username_password](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/request_acces_token_using_client.png)


Com o Token, devemos utilizado para realizar a request na aplicação Java Spring Boot, que irá validar a role do token JWT e irá autorizar o request:

![request_with_role](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/request_with_role.png)

Se tentar realizar o request com um token valido, mas a um endpoint onde é necessário a outra role, será retornado o status code 401:

![unauthorized_role](https://raw.githubusercontent.com/lucas-silvs/estudo--keycloack-api-authentication/main/aplica%C3%A7%C3%A3o-java/spring-com-keycloak-exemplo/images/unauthorized_role_with_valid_token.png)


## Referencias

- [Spring boot + Keycloak - protegendo suas APIs (parte 1)](https://dev.to/mmacorin/spring-boot-keycloak-protegendo-suas-apis-parte-1-436o)

- [Spring boot + Keycloak - protegendo suas APIs (parte 2)](https://dev.to/mmacorin/spring-boot-keycloak-protegendo-suas-apis-parte-2-5bb4)


