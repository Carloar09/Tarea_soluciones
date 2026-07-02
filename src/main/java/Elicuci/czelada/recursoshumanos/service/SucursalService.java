package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.SucursalStatsDTO;
import Elicuci.czelada.recursoshumanos.entity.Sucursal;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import Elicuci.czelada.recursoshumanos.repository.DocumentoRepository;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.IncidenciaRepository;
import Elicuci.czelada.recursoshumanos.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SucursalService {

    private final SucursalRepository sucursalRepository;
    private final EmpleadoRepository empleadoRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final DocumentoRepository documentoRepository;

    public List<SucursalStatsDTO> getSucursalesConStats() {
        return sucursalRepository.findAll()
                .stream()
                .map(this::mapToStatsDTO)
                .collect(Collectors.toList());
    }

    private SucursalStatsDTO mapToStatsDTO(Sucursal s) {

        Long sucursalId = s.getIdSucursal();

        // Total empleados en esta sucursal
        long total = empleadoRepository.countBySucursalId(sucursalId);

        // Empleados activos
        long activos = empleadoRepository.countByEstadoEmpleadoAndSucursalId(
                EstadoEmpleado.ACTIVO, sucursalId);

        // Incidencias
        long incidencias = incidenciaRepository.countPorResolverBySucursal(sucursalId);

        // Documentos pendientes — empleados con docs faltantes
        List<Elicuci.czelada.recursoshumanos.entity.Empleado> empleados =
                empleadoRepository.findBySucursalId(sucursalId);

        long docsPendientes = empleados.stream()
                .filter(emp -> {
                    Long id = emp.getIdEmpleado();
                    return !documentoRepository.existsByEmpleadoIdAndtipoDocumento(id, TipoDocumento.DNI)
                            || !documentoRepository.existsByEmpleadoIdAndtipoDocumento(id, TipoDocumento.CONTRATO)
                            || !documentoRepository.existsByEmpleadoIdAndtipoDocumento(id, TipoDocumento.ANTECEDENTES);
                })
                .count();

        return SucursalStatsDTO.builder()
                .idSucursal(sucursalId)
                .nombre(s.getNombre())
                .ubicacion(s.getUbicacion())
                .encargadoId(s.getEncargadoid())
                .totalEmpleados(total)
                .activos(activos)
                .incidencias(incidencias)
                .docsPendientes(docsPendientes)
                .build();
    }
}