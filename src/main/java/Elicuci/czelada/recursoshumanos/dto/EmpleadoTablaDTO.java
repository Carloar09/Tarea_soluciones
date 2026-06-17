package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder

public class EmpleadoTablaDTO {
    private Long id;
    private String dni;
    private String nombreCompleto;
    private String avatarUrl;
    private EstadoEmpleado estado;

    // Del contrato activo
    private String area;
    private String cargo;
    private String sucursalNombre;
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;
}

