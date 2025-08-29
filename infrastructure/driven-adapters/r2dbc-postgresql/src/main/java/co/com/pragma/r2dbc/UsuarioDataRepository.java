package co.com.pragma.r2dbc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UsuarioDataRepository extends ReactiveCrudRepository<UsuarioData, UUID> {
    Mono<Boolean> existsByCorreoElectronicoIgnoreCase(String correo_electronico);
}
