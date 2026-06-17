package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.enums.TipoSalida;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SalidaRequestDTO {
     //es la peticion de angular
    @NotNull(message = "El DNI del empleado es obligatorio")
    private String dniEmpleado;

    @NotNull(message = "El tipo de salida del empleado es obligatorio")
    private TipoSalida tipoSalida;

    @NotNull(message = "La fecha de salida del empleado es obligatoria")
    private LocalDate fechaSalida;

    private boolean cartaRecibida;
    private boolean devolucionMateriales;
    private boolean liquidacionFirmada;

    private String observaciones;
}
