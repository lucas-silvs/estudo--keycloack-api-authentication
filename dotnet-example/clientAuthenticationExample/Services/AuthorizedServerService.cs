namespace clientAuthenticationExample.Services;

public class AuthorizedServerService : IAuthorizedServerService
{
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly string _authorizedServerUrl;

    
    public AuthorizedServerService(IHttpClientFactory httpClientFactory, IConfiguration configuration)
    {
        _httpClientFactory = httpClientFactory;
        _authorizedServerUrl = configuration["AuthorizedServer:Url"];
    }

    public string testeToken(string token)
    {

        Console.WriteLine(token);

        var client = _httpClientFactory.CreateClient();

        var request = new HttpRequestMessage(HttpMethod.Get, _authorizedServerUrl);

        // Adicione o token ao cabeçalho Authorization
        request.Headers.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);

        // Envie a solicitação
        var response = client.SendAsync(request).Result;

        if (response.IsSuccessStatusCode)
        {
            // Se a solicitação for bem-sucedida, obtenha o conteúdo da resposta
            var responseContent = response.Content.ReadAsStringAsync().Result;
            return responseContent;
        }
        else
        {
            // Se a solicitação falhar, retorne o código de status e a mensagem de erro
            return $"Error: {response.StatusCode}, {response.ReasonPhrase}";
        }
    }
}