package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.enums.SeveridadIncidencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncidenciaRequestDTO {
    @NotBlank(message = "El DNI del empleado es obligatorio")
    private String dniEmpleado;

    @NotBlank(message = "El tipo de incidencia es obligatorio")
    private String tipoIncidencia;

    @NotNull(message = "La severidad es obligatoria")
    private SeveridadIncidencia severidad;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaSubida;

    private String descripcion;

}
