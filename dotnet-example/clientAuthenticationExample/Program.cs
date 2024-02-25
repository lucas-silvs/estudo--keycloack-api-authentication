using clientAuthenticationExample.Services;
using clientAuthenticationExample.Models;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddControllers();

// Adicione as configurações do Keycloak ao contêiner DI
var oidcSettings = new OidcSettings();
builder.Configuration.GetSection("oidcProvider").Bind(oidcSettings);
builder.Services.AddSingleton(oidcSettings);


// Registre o serviço KeycloakTokenService
builder.Services.AddHttpClient<ITokenService, TokenServices>();
builder.Services.AddHttpClient<IAuthorizedServerService, AuthorizedServerService>();

builder.Services.Configure<OidcSettings>(builder.Configuration.GetSection("oidcProvider"));
builder.Services.AddTransient<ITokenService, TokenServices>();

builder.Services.AddTransient<IAuthorizedServerService, AuthorizedServerService>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseSwagger();
app.UseSwaggerUI();

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();