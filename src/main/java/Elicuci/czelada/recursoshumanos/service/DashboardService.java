package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.DashboardDTO;
import Elicuci.czelada.recursoshumanos.entity.Contrato;
import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import Elicuci.czelada.recursoshumanos.repository.ContratoRepository;
import Elicuci.czelada.recursoshumanos.repository.DocumentoRepository;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.IncidenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final EmpleadoRepository empleadoRepository;
    private final ContratoRepository contratoRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final DocumentoRepository documentoRepository;

    public DashboardDTO getDashboardDTO(Long sucursalId) {
        return DashboardDTO.builder()
                .totalEmpleado(calcularTotalEmpleados(sucursalId))
                .activosHoy(calcularActivos(sucursalId))
                .incidenciaEsteMes(calcularIncidenciasEsteMes(sucursalId))
                .incidentesPorResolver(calcularIncidentesPorResolver(sucursalId))
                .documentosPendiente(calcularDocumentosPendientes(sucursalId))
                .proximosContratos(calcularProximosContratos(sucursalId))
                .actividadReciente(calcularActividadReciente(sucursalId))
                .build();
    }
    // para verl el total de empleados
    private long calcularTotalEmpleados(Long sucursalId) {
        if (sucursalId != null) {
            return empleadoRepository.countBySucursalId(sucursalId);
        }
        return empleadoRepository.count();
    }
    //para ver los empleados activos
    private long calcularActivos(Long sucursalId) {
        if (sucursalId != null) {
            return empleadoRepository.countByEstadoEmpleadoAndSucursalId(
                    EstadoEmpleado.ACTIVO, sucursalId);
        }
        return empleadoRepository.countByEstadoEmpleado(EstadoEmpleado.ACTIVO);
    }
    //iniciendias del mes

    private long calcularIncidenciasEsteMes(Long sucursalId) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now();

        if (sucursalId != null) {
            return incidenciaRepository.countByFechaBetweenAndSucursalId(
                    inicioMes, finMes, sucursalId);
        }
        return incidenciaRepository.countByFechaBetween(inicioMes, finMes);
    }
    //Inicidentes por resolver
    private long calcularIncidentesPorResolver(Long sucursalId) {
        if (sucursalId != null) {
            return incidenciaRepository.countPorResolverBySucursal(sucursalId);
        }
        return incidenciaRepository.countPorResolver();
    }
    //para ver los documetnos que faltan subir
    private long calcularDocumentosPendientes(Long sucursalId) {
        List<Empleado> empleados = sucursalId != null
                ? empleadoRepository.findBySucursalId(sucursalId)
                : empleadoRepository.findAll();

        // cuenta empleados que les falta al menos 1 documento base
        return empleados.stream()
                .filter(emp -> {
                    Long id = emp.getIdEmpleado();
                    boolean faltaDni = !documentoRepository
                            .existsByEmpleadoIdAndtipoDocumento(id, TipoDocumento.DNI);
                    boolean faltaContrato = !documentoRepository
                            .existsByEmpleadoIdAndtipoDocumento(id, TipoDocumento.CONTRATO);
                    boolean faltaAntecedentes = !documentoRepository
                            .existsByEmpleadoIdAndtipoDocumento(id, TipoDocumento.ANTECEDENTES);
                    return faltaDni || faltaContrato || faltaAntecedentes;
                })
                .count();
    }
    // muestra contratos por vencer
    private List<DashboardDTO.ContratoVencimientoDTO> calcularProximosContratos(Long sucursalId) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(90); // próximos 90 días

        List<Contrato> contratos = sucursalId != null
                ? contratoRepository.findProximosAVencerBySucursal(hoy, limite, sucursalId)
                : contratoRepository.findProximosAVencer(hoy, limite);

        return contratos.stream()
                .map(c -> {
                    long dias = ChronoUnit.DAYS.between(hoy, c.getFechaVencimiento());
                    Empleado emp = c.getEmpleado();

                    return DashboardDTO.ContratoVencimientoDTO.builder()
                            .empleadoId(emp.getIdEmpleado())
                            .nombreCompleto(emp.getNombre() + " " + emp.getApellido())
                            .dni(emp.getDni())
                            .avatarUrl(emp.getAvatarURL())
                            .sucursalNombre(c.getSucursal().getNombre())
                            .fechaVencimiento(c.getFechaVencimiento())
                            .diasRestantes(dias)
                            // Color según días: Rojo < 30, Naranja < 60, Verde resto
                            .colorAlerta(dias < 30 ? "ROJO" : dias < 60 ? "NARANJA" : "VERDE")
                            .build();
                })
                .sorted((a, b) -> Long.compare(a.getDiasRestantes(), b.getDiasRestantes()))
                .collect(Collectors.toList());
    }
    // ver su actividad reciente
    private List<DashboardDTO.ActividadDTO> calcularActividadReciente(Long sucursalId) {
        // Tomamos las últimas 10 incidencias como actividad reciente
        // (más adelante puedes agregar una tabla "Actividad" dedicada)
        List<Elicuci.czelada.recursoshumanos.entity.Incidencia> recientes =
                incidenciaRepository.findTop10ByOrderByFechaDesc();

        return recientes.stream()
                .map(inc -> {
                    Empleado emp = inc.getEmpleado();
                    String nombre = emp.getNombre() + " " + emp.getApellido();
                    String iniciales = String.valueOf(emp.getNombre().charAt(0))
                            + String.valueOf(emp.getApellido().charAt(0));

                    return DashboardDTO.ActividadDTO.builder()
                            .nombreCompleto(nombre)
                            .avatarUrl(emp.getAvatarURL())
                            .iniciales(iniciales.toUpperCase())
                            .accion(inc.getTipo() + " registrado")
                            .tiempoRelativo(calcularTiempoRelativo(
                                    inc.getFecha().atStartOfDay()))
                            .fechaReal(inc.getFecha().atStartOfDay())
                            .build();
                })
                .collect(Collectors.toList());
    }
    // para calcualr el tiempo actividad
    private String calcularTiempoRelativo(LocalDateTime fecha) {
        LocalDateTime ahora = LocalDateTime.now();
        long horas = ChronoUnit.HOURS.between(fecha, ahora);
        long dias = ChronoUnit.DAYS.between(fecha, ahora);

        if (horas < 1)   return "hace unos minutos";
        if (horas < 24)  return "hace " + horas + "h";
        if (dias == 1)   return "ayer";
        if (dias < 7)    return "hace " + dias + " días";
        return "hace " + (dias / 7) + " semana(s)";
    }

}
