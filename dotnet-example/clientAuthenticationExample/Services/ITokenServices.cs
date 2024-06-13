namespace clientAuthenticationExample.Services;

public interface ITokenService
{
    Task<string> GenerateToken();
}