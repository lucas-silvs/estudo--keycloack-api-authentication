using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using clientAuthenticationExample.Models;
using clientAuthenticationExample.Services;

namespace clientAuthenticationExample.Controllers;

public class TesteController : Controller
{
    private readonly ILogger<TesteController> _logger;

    private readonly ITokenService _tokenServices;
    
    private readonly IAuthorizedServerService _authorizedServerService;
    
    public TesteController(ILogger<TesteController> logger, ITokenService tokenServices, IAuthorizedServerService authorizedServerService)
    {
        _logger = logger;
        _tokenServices = tokenServices;
        _authorizedServerService = authorizedServerService;
    }

    [HttpPost("teste-client")]
    public string Index()
    {
        var token = _tokenServices.GenerateToken();
        
        var response = _authorizedServerService.testeToken(token.Result);
        
        return response;
    }
}
