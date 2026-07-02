package Elicuci.czelada.recursoshumanos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SucursalStatsDTO {
    private Long idSucursal;
    private String nombre;
    private String ubicacion;
    private Long encargadoId;
    private long totalEmpleados;
    private long activos;
    private long incidencias;
    private long docsPendientes;
}