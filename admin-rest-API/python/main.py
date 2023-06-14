import base_functions as bf

url_base_token = 'http://0.0.0.0:8080'
realm = 'teste'
client_id = 'teste'
client_secret = '2rYZtAkn69nMLEuRwPoQv67OiYDyby2N'

url_base_admin = 'http://0.0.0.0:8080/admin/realms/'

bearer_token = bf.solicita_bearer_token(url_base_token,realm,client_id,client_secret)

# ---------------------------- Client ----------------------------

client_id_novo = input("Digite o client_id que será criado: ")
bf.cria_client_credential(url_base_admin,realm,client_id_novo, bearer_token)

dados_client_novo = bf.busca_client_pelo_client_id(url_base_admin, realm, bearer_token, client_id_novo)

print("client_id:", dados_client_novo[2])
print("client_secret:", dados_client_novo[1])
print("keycloak_id_client_id:", dados_client_novo[0])
print("-----------------------------\n")

# ---------------------------- Service Account ----------------------------
service_account_id = bf.busca_service_account_pelo_clientId(url_base_admin, realm, dados_client_novo[0], bearer_token)

# ---------------------------- Realm role ----------------------------
nome_realm_role = input("Digite o nome do Realm role: ")

bf.cria_realm_role(url_base_admin, realm, nome_realm_role, bearer_token)

realm_role_id = bf.busca_realm_role_pelo_nome(url_base_admin, realm, nome_realm_role, bearer_token)

bf.associar_realm_role_ao_service_account(url_base_admin, realm, service_account_id, nome_realm_role, realm_role_id, bearer_token)




