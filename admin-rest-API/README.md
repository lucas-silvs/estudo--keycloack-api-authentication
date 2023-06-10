# Criação de recursos via Admin REST API
Foi desenvolvida um Collection Postman com exemplos de criação de recursos no Keycloak de forma programática, utilizando o Admin Rest API, para isso, foi criada manualmente com o usuário **Admin** um client credential com acesso de `Realm Admin` somente de um Realm de teste, e utilizado o Token desse usuário para a criação dos recursos.
Abaixo está a lista de recursos que está sendo criada via Rest API neste exemplo:

- Criação
    - Client (Com Service Account) 
    - Client Scope
    - Realm Role
    - Secret Client
- Atribuição
    - Role ao Service Account
    - Default Scope ao Client
- Atualização
    - Client
    - Secret Client
- Busca
    - Client
    - Client Scope
    - Secret Client
    - Service Account atribuida a um Client



## Referencias:

[Keycloak 20.0.3 Admin Rest API](https://www.keycloak.org/docs-api/20.0.3/rest-api/#_overview)