package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.IncidenciaRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.IncidenciaResponseDTO;
import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.Incidencia;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.IncidenciaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidenciaService {
    private final IncidenciaRepository incidenciaRepository;
    private final EmpleadoRepository empleadoRepository;

    //Registro incidencia

    @Transactional
    public IncidenciaResponseDTO registrarIncidencia(IncidenciaRequestDTO incidenciaRequestDTO) {
        Empleado empleado =empleadoRepository.findByDni(incidenciaRequestDTO.getDniEmpleado())
                .orElseThrow(()->new RuntimeException("Empleado no encontrado con DNI: "+ incidenciaRequestDTO.getDniEmpleado()));
        //creamos la entidad
        Incidencia incidencia = new Incidencia();
        incidencia.setEmpleado(empleado);
        incidencia.setTipo(incidenciaRequestDTO.getTipoIncidencia());
        incidencia.setSeveridad(incidenciaRequestDTO.getSeveridad());
        incidencia.setFecha(incidenciaRequestDTO.getFechaSubida());
        incidencia.setDescripcion(incidenciaRequestDTO.getDescripcion());

        // se guarda
        Incidencia guardada= incidenciaRepository.save(incidencia);
        return mapToResponseDTO(guardada);
    }
    // listamos las incidencias
    public List<IncidenciaResponseDTO> listarIncidencias() {
        return incidenciaRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    //metodo para lsitar las incidenicas ded un empleado en especidifico
    public List<IncidenciaResponseDTO> listarPorEmpleado(String dniEmpleado) {

        Empleado empleado = empleadoRepository.findByDni(dniEmpleado)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con DNI: " + dniEmpleado));

        return incidenciaRepository.findByEmpleadoId(empleado.getIdEmpleado())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    //mapper privado
    private IncidenciaResponseDTO mapToResponseDTO(Incidencia inc) {
        Empleado emp = inc.getEmpleado();
        return IncidenciaResponseDTO.builder()
                .idEmpleado(inc.getIdIncidencia())
                .dniEmpleado(emp.getDni())
                .nombreCompleto(emp.getNombre() + " " + emp.getApellido())
                .tipo(inc.getTipo())
                .severidad(inc.getSeveridad().name())
                .fecha(inc.getFecha().toString())
                .descripcion(inc.getDescripcion())
                .build();
    }
}
