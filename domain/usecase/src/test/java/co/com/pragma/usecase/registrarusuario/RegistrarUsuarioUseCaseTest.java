package co.com.pragma.usecase.registrarusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseTest {

    @Mock
    UsuarioRepository repository;

    @InjectMocks
    RegistrarUsuarioUseCase useCase;

    @Test
    void registrarUsuario_exitoso() {
        Usuario usuario = Usuario.builder()
                .nombres("Ana")
                .apellidos("Lopez")
                .correoElectronico("ana@example.com")
                .salarioBase(new BigDecimal("2000"))
                .build();

        when(repository.existsByCorreoElectronico("ana@example.com")).thenReturn(Mono.just(false));
        when(repository.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute(usuario))
                .expectNextMatches(u -> u.getCorreoElectronico().equals("ana@example.com"))
                .verifyComplete();
    }

    @Test
    void registrarUsuario_correoDuplicado() {
        Usuario usuario = Usuario.builder()
                .nombres("Ana")
                .apellidos("Lopez")
                .correoElectronico("ana@example.com")
                .salarioBase(new BigDecimal("2000"))
                .build();

        when(repository.existsByCorreoElectronico("ana@example.com")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(usuario))
                .expectError(RegistrarUsuarioUseCase.EmailDuplicadoException.class)
                .verify();
    }
}
