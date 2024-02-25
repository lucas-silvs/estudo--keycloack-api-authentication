namespace clientAuthenticationExample.Models;

public class OidcSettings
{
    public string TokenUrl { get; set; }
    public string ClientId { get; set; }
    public string ClientSecret { get; set; }
    public string GrantType { get; set; }
}