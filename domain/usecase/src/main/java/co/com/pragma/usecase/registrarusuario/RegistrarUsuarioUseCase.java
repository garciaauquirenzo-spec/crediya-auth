package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import reactor.util.Loggers;
import reactor.util.Logger;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository repository;
    private static final Logger log = Loggers.getLogger(RegistrarUsuarioUseCase.class);

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Mono<Usuario> execute(Usuario u) {
        log.trace("Iniciando registro de usuario con correo {}", u.getCorreoElectronico());
        validar(u);
        return repository
                .existsByCorreoElectronico(u.getCorreoElectronico())
                .doOnNext(exists -> log.trace("Correo {} existente? {}", u.getCorreoElectronico(), exists))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        log.trace("El correo {} ya está registrado", u.getCorreoElectronico());
                        return Mono.error(new EmailDuplicadoException(u.getCorreoElectronico()));
                    }
                    log.trace("Guardando usuario con correo {}", u.getCorreoElectronico());
                    return repository.save(u)
                            .doOnSuccess(saved -> log.trace("Usuario registrado con id {}", saved.getId()));
                });
    }

    private void validar(Usuario u) {
        if (u == null) throw new IllegalArgumentException("Usuario requerido");
        req(u.getNombres(), "nombres");
        req(u.getApellidos(), "apellidos");
        req(u.getCorreoElectronico(), "correo_electronico");
        if (!EMAIL.matcher(u.getCorreoElectronico()).matches())
            throw new IllegalArgumentException("correo_electronico con formato inválido");
        BigDecimal s = u.getSalarioBase();
        if (s == null) throw new IllegalArgumentException("salario_base requerido");
        if (s.compareTo(BigDecimal.ZERO) < 0 || s.compareTo(new BigDecimal("15000000")) > 0)
            throw new IllegalArgumentException("salario_base fuera de rango [0, 15000000]");
    }

    private void req(String v, String nombre) {
        if (v == null || v.isBlank())
            throw new IllegalArgumentException(nombre + " requerido");
    }

    public static class EmailDuplicadoException extends RuntimeException {
        public EmailDuplicadoException(String email) {
            super("El correo ya está registrado: " + email);
        }
    }
}
