package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class UsuarioRequest {

    @Schema(example = "Ana María", description = "Nombres del usuario", required = true)
    @NotBlank
    private String nombres;

    @Schema(example = "López Ramos", description = "Apellidos del usuario", required = true)
    @NotBlank
    private String apellidos;

    @Schema(example = "1990-05-10", description = "Fecha de nacimiento en formato YYYY-MM-DD")
    private LocalDate fechaNacimiento;

    @Schema(example = "Jr. Froilan Orrego S/N", description = "Dirección del usuario")
    private String direccion;

    @Schema(example = "935426697", description = "Telefono del usuario")
    private String telefono;

    @Schema(example = "ana@example.com", description = "Correo electrónico único", required = true)
    @NotBlank @Email private String correoElectronico;

    @Schema(example = "2500.50", description = "Salario base entre 0 y 15,000,000", required = true)
    @NotNull @DecimalMin("0") @DecimalMax("15000000")
    private BigDecimal salarioBase;
}
