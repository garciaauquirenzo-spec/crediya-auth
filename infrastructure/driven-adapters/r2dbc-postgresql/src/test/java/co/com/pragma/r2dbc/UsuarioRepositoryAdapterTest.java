package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    UsuarioRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioDataRepository repository;

    @Mock
    ObjectMapper mapper;


    @Test
    void mustReturnTrueWhenCorreoExists() {
        String email = "ana@example.com";
        when(repository.existsByCorreoElectronicoIgnoreCase(email)).thenReturn(Mono.just(true));

        StepVerifier.create(repositoryAdapter.existsByCorreoElectronico(email))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustSaveAndReturnUsuario() {
        int id = 1;
        Usuario usuario = Usuario.builder()
                .id(id)
                .nombres("Ana")
                .apellidos("Lopez")
                .fechaNacimiento(LocalDate.of(1990, 5, 10))
                .direccion("Av. Primavera 123")
                .telefono("999888777")
                .correoElectronico("ana@example.com")
                .salarioBase(new BigDecimal("2000"))
                .build();

        UsuarioData data = UsuarioData.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .direccion(usuario.getDireccion())
                .telefono(usuario.getTelefono())
                .correoElectronico(usuario.getCorreoElectronico())
                .salarioBase(usuario.getSalarioBase())
                .build();

        when(repository.save(any())).thenReturn(Mono.just(data));

        StepVerifier.create(repositoryAdapter.save(usuario))
                .expectNextMatches(saved ->
                        saved.getId().equals(id) &&
                                saved.getCorreoElectronico().equals("ana@example.com") &&
                                saved.getNombres().equals("Ana"))
                .verifyComplete();
    }
}
