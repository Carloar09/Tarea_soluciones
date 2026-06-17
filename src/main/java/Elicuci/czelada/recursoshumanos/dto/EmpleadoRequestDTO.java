package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpleadoRequestDTO {

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @Size(max = 11)
    private String telefono;

    @Email(message = "El email no tiene formato válido")
    @Size(max = 150)
    private String email;

    @Size(max = 150)
    private String direccion;

    @Size(max = 150)
    private String contactoEmergencia;


    private EstadoEmpleado estado;

    private String AvatarUrl;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;

    @NotBlank(message = "El área es obligatoria")
    private String area;

    @NotBlank(message = "El cargo es obligatorio")
    private String cargo;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private java.time.LocalDate fechaIngreso;

    private java.time.LocalDate fechaVencimiento;
}
