using System;
using Newtonsoft.Json;

namespace clientAuthenticationExample.Models;

public class TokenResponseModel
{   
    [JsonProperty("access_token")]
    public string AccessToken { get; set; }
    
    [JsonProperty("expires_in")]
    public int ExpiresIn { get; set; }
    
    [JsonProperty("token_type")]
    public string TokenType { get; set; }
}

