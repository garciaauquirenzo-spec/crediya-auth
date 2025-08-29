package co.com.pragma.model.usuario.gateways;
import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UsuarioRepository {
    Mono<Boolean> existsByCorreoElectronico(String correo);
    Mono<Usuario> save(Usuario usuario);
}
