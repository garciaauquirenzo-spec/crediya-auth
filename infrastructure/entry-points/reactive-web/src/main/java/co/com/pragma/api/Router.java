package co.com.pragma.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "crear",
                    operation = @Operation(
                            operationId = "crearUsuario",
                            summary = "Crear un nuevo usuario",
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado"),
                                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                                    @ApiResponse(responseCode = "409", description = "Correo duplicado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionUsuarios(UsuarioHandler handler) {
        return route(POST("/api/v1/usuarios").and(accept(MediaType.APPLICATION_JSON)), handler::crear);
    }
}
