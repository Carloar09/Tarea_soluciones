package Elicuci.czelada.recursoshumanos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SalidaResponseDTO {
    //devuelve los datos del emplkeado
    private Long id_Salida;
    private String nombreCompleto;
    private String dni;
    private String avatarUrl;
    //"" datos de la salida
    private String tipoSalida;
    private LocalDate fechaSalida;
    private String tiempoEmpresa;
    // checklist
    private boolean cartaRecibida;
    private boolean devolucionMateriales;
    private boolean liquidacionFirmada;

    private String observaciones;
}
