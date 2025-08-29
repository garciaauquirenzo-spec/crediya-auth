package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioRequest;
import co.com.pragma.api.dto.UsuarioResponse;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;


import java.net.URI;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {
    //caso de uso de dominio (dentro de domain/usecase)
    private final RegistrarUsuarioUseCase useCase;
    //el validador de Bean Validation (jakarta.validation.Validator)
    private final Validator validator;
    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(UsuarioRequest.class)        // (1) leer body JSON -> DTO
                .flatMap(this::validate)                      // (2) validar DTO con Bean Validation
                .flatMap(dto -> useCase.execute(mapToDomain(dto))) // (3) mapear DTO -> dominio y ejecutar caso de uso
                .flatMap(u -> ServerResponse.created(          // (4) construir respuesta 201 Created
                                URI.create("/api/v1/usuarios/" + u.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioResponse(u.getId())))
                .onErrorResume(RegistrarUsuarioUseCase.EmailDuplicadoException.class,
                        ex -> ApiErrors.conflict(ex.getMessage()))  // (5a) manejar error 409 Conflict
                .onErrorResume(IllegalArgumentException.class,
                        ex -> ApiErrors.badRequest(ex.getMessage())); // (5b) manejar error 400 Bad Request
    }

    private Mono<UsuarioRequest> validate(UsuarioRequest dto) {
        Set<ConstraintViolation<UsuarioRequest>> v = validator.validate(dto);
        if (v.isEmpty()) return Mono.just(dto);
        return Mono.error(new IllegalArgumentException(
                v.iterator().next().getPropertyPath() + ": " + v.iterator().next().getMessage()));
    }

    private Usuario mapToDomain(UsuarioRequest r) {
        return Usuario.builder()
                .nombres(r.getNombres())
                .apellidos(r.getApellidos())
                .fechaNacimiento(r.getFechaNacimiento())
                .direccion(r.getDireccion())
                .telefono(r.getTelefono())
                .correoElectronico(r.getCorreoElectronico())
                .salarioBase(r.getSalarioBase())
                .build();
    }
}
