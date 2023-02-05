# estudo--keycloack-api-authentication
Estudo do Keycloak para autenticação de requisições entre api e serviço. O estudo será realizado para configurar o Keycloak para realizar a autenticação entre api e serviços, seguindo o modelo client credential. 

## O que é o Keycloak
O keycloak é uma ferramenta para autenticação e authorização, possibilidando a configuração de permissões. O Keycloak conta com diversas funcionalidades comuns no mercado para autenticação e autorização, Como OIDP, Oauth2.

Abaixo está descrito alguns conceitos que são abordados no Keycloak:

### Realms
Realms funcionam como repositórios de usuários, onde pode ser separado os usuários, clientes, regrad e permissões que serão autenticadas nesse Realm.

### Clients
Client são a a abstração das aplicações que devemos autenticar com o Keycloak.

### Users

Users são usuários de acesso o painel de administrador do Realm do Keycloak, podendo adicionar permissões de acessos especificos  para o usuário.

### User Groups
Grupo de usuários é uma opção para gerenciamento de permissões de usuários, assim aplicando permissões a um grupo de usuário, facilitando o gerenciamento.

### Roles
Roles são permissões que podem ser associadas a usuarios ou grupo de usuários.

### Identity Provider
Identity Provider permite  o Keycloak realizar o login em outras plataformas, como Google, Github, Facebook. etc.

## Execução banco MySQL Docker
Foi disponibilizado um banco Mysql para ser utilizado pelo Keycloak, assim persistindo os dados de credenciais após o desligamento da ferramenta.

Para executar o banco local, execute o comando abaixo:

```console
docker compose up
```

## Iniciar Keycloak

Para iniciar Keycloak, deve executar o comando abaixo na pasta do keycloak:

```console
bin/kc.sh start-dev
```

