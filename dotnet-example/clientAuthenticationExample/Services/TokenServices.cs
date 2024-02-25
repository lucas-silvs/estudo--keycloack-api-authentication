using System.Text;
using Newtonsoft.Json;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Options;


using clientAuthenticationExample.Models;

namespace clientAuthenticationExample.Services;

public class TokenServices : ITokenService
{
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly OidcSettings _keycloakSettings;
    private readonly IMemoryCache _cache;

    public TokenServices(IHttpClientFactory httpClientFactory, IOptions<OidcSettings> keycloakSettings, IMemoryCache cache)
    {
        _httpClientFactory = httpClientFactory;
        _keycloakSettings = keycloakSettings.Value;
        _cache = cache;
    }

    public async Task<string> GenerateToken()
    {
        // Verifique se o token já está em cache
        if (_cache.TryGetValue("Token", out string cachedToken))
        {
            return cachedToken;
        }

        var client = _httpClientFactory.CreateClient();
        try
        {
            var request = new HttpRequestMessage(HttpMethod.Post, _keycloakSettings.TokenUrl);

            request.Content = new StringContent($"grant_type=client_credentials&client_id={_keycloakSettings.ClientId}&client_secret={_keycloakSettings.ClientSecret}", Encoding.UTF8, "application/x-www-form-urlencoded");

            var response = await client.SendAsync(request);

            if (response.IsSuccessStatusCode)
            {
                var jsonContent = await response.Content.ReadAsStringAsync();

                var tokenResponse = JsonConvert.DeserializeObject<TokenResponseModel>(jsonContent);

                // Armazene o token
                string token = tokenResponse.AccessToken;

                // Calcule o tempo de expiração para 80% do tempo do token
                int expiresIn = tokenResponse.ExpiresIn;
                int expireTimeFor80Percent = (int)(expiresIn * 0.8);

                // Armazene o tempo de expiração
                DateTime expireDateTime = DateTime.UtcNow.AddSeconds(expireTimeFor80Percent);

                // Armazene o token em cache
                _cache.Set("Token", token, expireDateTime);

                return token;
            }
            else
            {
                // Handle error
                return "";
            }
        }
        catch (Exception ex)
        {
            // Handle exception
            throw new Exception("Error on generate token");
        }
    }
}