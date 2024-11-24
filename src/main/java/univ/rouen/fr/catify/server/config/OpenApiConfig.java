package univ.rouen.fr.catify.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Izana",
            email = "izana.khabouri@univ-rouen.fr",
            url = "https://github.com/IzaalalaCoder"
        ),
        description = "Documentation explaining all commands for managing categories.",
        title = "Catify API",
        version = "1.0",
        license = @License(
            name = "",
            url = ""
        ),
        termsOfService = "Terms of service"
    ),
    servers = {
        @Server(
            description = "Local",
            url = "http://localhost:8080/catify"
        )
    }
)
public class OpenApiConfig {

}
