package Elicuci.czelada.recursoshumanos.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@Builder
public class DashboardDTO {
    // para mostrar en las tarjetas
    private long totalEmpleado;
    private long activosHoy;
    private long incidenciaEsteMes;
    private long incidentesPorResolver;
    private long documentosPendiente;

    private List<ContratoVencimientoDTO>proximosContratos;

    private List<ActividadDTO>actividadReciente;

    @Data
    @Builder
    public static class ContratoVencimientoDTO {
        private Long empleadoId;
        private String nombreCompleto;
        private String dni;
        private String avatarUrl;
        private String sucursalNombre;
        private java.time.LocalDate fechaVencimiento;
        private long diasRestantes;
        private String colorAlerta;
    }

    @Data
    @Builder
    public static class ActividadDTO {
        private String nombreCompleto;
        private String avatarUrl;
        private String iniciales;//del perfil del empleado
        private String accion;
        private String  tiempoRelativo;
        private java.time.LocalDateTime fechaReal;
    }
}
