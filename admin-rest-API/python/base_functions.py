import requests


def solicita_bearer_token(url_base,realm,client_id,client_secret):
    url = url_base + '/realms/' + realm + '/protocol/openid-connect/token'
    # utilize o body em formato de x-www-form-urlencoded
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }

    payload = {
        'grant_type': 'client_credentials',
        'client_id': client_id,
        'client_secret': client_secret
    }

    response = requests.post(url, data=payload, headers=headers)
    if response.status_code == 200:
        print("Token gerado com sucesso.")
        bearer_token = response.json()['access_token']
        return bearer_token
    else:
        print("Erro ao gerar o token:", response.text)
        print("Status code:", response.status_code)
        return None
    

# ---------------------------- Client Credential ----------------------------

def cria_client_credential(url_base,realm, client_id, bearer_token):

    header = {
        'Authorization': 'Bearer ' + bearer_token
    }


    payload = {
    "clientAuthenticatorType": "client-secret",
    "serviceAccountsEnabled": True,
    "clientId": client_id,
    "protocolMappers": [
            {
                "name": "subdominio-mapper",
                "protocol": "openid-connect",
                "protocolMapper": "oidc-usermodel-realm-role-mapper",
                "consentRequired": False,
                "config": {
                    "id.token.claim": "true",
                    "access.token.claim": "true",
                    "claim.name": "subdomain",
                    "multivalued": "true",
                    "userinfo.token.claim": "true"
                }
            }
        ]
    }

    url = url_base + realm + '/clients'

    response = requests.post(url, json=payload, headers=header)
    if response.status_code == 201:
        print("Recurso criado com sucesso.")
    else:
        print("Erro ao criar client", response.text)


def busca_client_pelo_client_id(url_base, realm, bearer_token, client_id):

    header = {
        'Authorization': 'Bearer ' + bearer_token
    }
    url = url_base + realm + '/clients?clientId=' + client_id

    response = requests.get(url, headers=header)
    
    if response.status_code == 200:
        print("Recurso encontrado com sucesso.")

        # printa client_id 
        print(response.json()[0]['id'])

        # retornar o client_id e o secret
        return response.json()[0]['id'], response.json()[0]['secret'], response.json()[0]['clientId']

    else:
        print("Erro ao buscar o client", response.text)


# ---------------------------- Service Account ----------------------------

def busca_service_account_pelo_clientId(url_base, realm, keycloak_client_id, bearer_token):
    url = url_base + realm + '/clients/' + keycloak_client_id + '/service-account-user'
    headers = {
        'Authorization': 'Bearer ' + bearer_token
    }
    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        print("service account encontrado com sucesso.")

        # printa client_id service_account_id
        print("id do service account do client id criado: " + response.json()['id'])

        return response.json()['id']

    else:
        print("Erro ao buscar service account", response.text)


def associar_realm_role_ao_service_account(url_base, realm, service_account_id, realm_role_name, realm_role_id, bearer_token):
    url = url_base + realm + '/users/' + service_account_id + '/role-mappings/realm'
    payload = [
        {
            "name": realm_role_name,
            "id": realm_role_id
        }
    ]
    headers = {
        'Authorization': 'Bearer ' + bearer_token
    }
    response = requests.post(url, json=payload, headers=headers)
    if response.status_code == 204:
        print("Role associada  ao service account com sucesso.")
        return True
    else:
        print("Erro ao associar a role ao service account", response.text)
        return False

# ---------------------------- Realm Role ----------------------------

def cria_realm_role(url_base, realm, nome_realm_role, bearer_token):
    url = url_base + realm + '/roles'

    payload = {
        "name": nome_realm_role,
        "description": "Realm role criado via API com Python"
    }
    headers = {
        'Authorization': 'Bearer ' + bearer_token
    }
    response = requests.post(url, json=payload, headers=headers)
    if response.status_code == 201:
        print("Realm role criada com sucesso")
        return True
    else:
        print("Erro ao criar o Realm role:", response.text)
        return None
    

def busca_realm_role_pelo_nome(url_base, realm, nome_realm_role, bearer_token):
    url = url_base + realm + '/roles?search=' + nome_realm_role
    headers = {
        'Authorization': 'Bearer ' + bearer_token
    }

    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        print("Realm Role buscada com sucesso")
        print(response.json()[0]['id'])
        return response.json()[0]['id']
    else:
        print("Erro ao buscar Realm Role:", response.text)
        return None



