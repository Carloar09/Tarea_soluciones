package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.enums.SeveridadIncidencia;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("tipo")
    private String tipoIncidencia;

    @NotNull(message = "La severidad es obligatoria")
    private SeveridadIncidencia severidad;

    @NotNull(message = "La fecha es obligatoria")
    @JsonProperty("fecha")
    private LocalDate fechaSubida;

    private String descripcion;

}
