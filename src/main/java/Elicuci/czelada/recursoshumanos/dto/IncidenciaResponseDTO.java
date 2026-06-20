package Elicuci.czelada.recursoshumanos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncidenciaResponseDTO {
    private Long idEmpleado;
    private String dniEmpleado;
    private String nombreCompleto;
    private String tipo;
    private String severidad;
    private String fecha;
    private String descripcion;
}
