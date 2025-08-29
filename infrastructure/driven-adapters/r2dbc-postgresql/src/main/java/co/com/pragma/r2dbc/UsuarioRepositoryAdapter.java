package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioDataRepository repo;

    @Override
    public Mono<Boolean> existsByCorreoElectronico(String correo) {
        return repo.existsByCorreoElectronicoIgnoreCase(correo);
    }

    @Override
    public Mono<Usuario> save(Usuario u) {
        UsuarioData data = mapToData(u);
        return repo.save(data).map(this::mapToDomain);
    }

    private UsuarioData mapToData(Usuario u) {
        return UsuarioData.builder()
                .id(u.getId())
                .nombres(u.getNombres())
                .apellidos(u.getApellidos())
                .fechaNacimiento(u.getFechaNacimiento())
                .direccion(u.getDireccion())
                .telefono(u.getTelefono())
                .correoElectronico(u.getCorreoElectronico())
                .salarioBase(u.getSalarioBase())
                .build();
    }

    private Usuario mapToDomain(UsuarioData d) {
        return Usuario.builder()
                .id(d.getId())
                .nombres(d.getNombres())
                .apellidos(d.getApellidos())
                .fechaNacimiento(d.getFechaNacimiento())
                .direccion(d.getDireccion())
                .telefono(d.getTelefono())
                .correoElectronico(d.getCorreoElectronico())
                .salarioBase(d.getSalarioBase())
                .build();
    }
}
