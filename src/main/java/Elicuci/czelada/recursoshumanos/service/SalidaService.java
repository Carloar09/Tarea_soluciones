package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.SalidaRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.SalidaResponseDTO;
import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.Salida;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.SalidaRepository;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalidaService {
    private final SalidaRepository salidaRepository;
    private final EmpleadoRepository empleadoRepository;

    // cuando un empleado salga aqui va a registrarlo y va a cambiar su estado
    @Transactional
    public SalidaResponseDTO registrarSalida(SalidaRequestDTO salidaRequestDTO) {
        // esta pidioendo el dni si no encuentra slae empleaedo no econtro su dni y madna al front
        Empleado empleado = empleadoRepository.findByDni(salidaRequestDTO.getDniEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con DNI" + salidaRequestDTO.getDniEmpleado()));

        if (empleado.getEstadoEmpleado() == EstadoEmpleado.EX_EMPLEADO) {
            throw new RuntimeException("Este empleado ya tiene una salida registrada");
        }
        //cera la entidad
        Salida salida = new Salida();
        salida.setEmpleado(empleado);
        salida.setTipoSalida(salidaRequestDTO.getTipoSalida());
        salida.setFechaSalida(salidaRequestDTO.getFechaSalida());
        salida.setCartaRecibida(salidaRequestDTO.isCartaRecibida());
        salida.setDevolucionDeMateriales(salidaRequestDTO.isDevolucionMateriales());
        salida.setLiquidacionFirmada(salidaRequestDTO.isLiquidacionFirmada());
        salida.setObservaciones(salidaRequestDTO.getObservaciones());
        //guardamos los datos de la saldia
        Salida salidaGuardada = salidaRepository.save(salida);
        return  mapToResponseDTO(salidaGuardada);
    }
    //listamos todas las salidad
    public List<SalidaResponseDTO> listarSalidas() {
        return salidaRepository.findAllByOrderByFechaSalidaDesc()
                .stream()
                //aqui donde conveirte los datoas en dto
                //la otra manera seria (salida->mapToResponseDTO(Salida))
                // es como salida1= dto1
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    public SalidaResponseDTO getDetalle(Long id) {
        Salida salida=salidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salida no encontrada con Id:"+ id));
        return mapToResponseDTO(salida);
    }

    private SalidaResponseDTO mapToResponseDTO(Salida salida) {
        Empleado emp=salida.getEmpleado();
        return SalidaResponseDTO.builder()
                .id_Salida(salida.getId_Salida())
                .nombreCompleto(emp.getNombre()+ " " + emp.getApellido())
                .dni(emp.getDni())
                .avatarUrl(emp.getAvatarURL())
                .tipoSalida(salida.getTipoSalida().name())
                .fechaSalida(salida.getFechaSalida())
                .tiempoEmpresa(calularTiempo(emp, salida.getFechaSalida()))
                .cartaRecibida(salida.isCartaRecibida())
                .devolucionMateriales(salida.isDevolucionDeMateriales())
                .liquidacionFirmada(salida.isLiquidacionFirmada())
                .observaciones(salida.getObservaciones())
                .build();
    }
    private String calularTiempo(Empleado emp, LocalDate fechaSalida) {
        if(emp.getContratos()==null || emp.getContratos().isEmpty()){
            return "sin contratos";
        }
        //tomamos el contrato para saber cuando ingreso
        LocalDate fechaIngreso=emp.getContratos().get(0).getFechaIngreso();
        Period periodo =Period.between(fechaIngreso, fechaSalida);

        int años = periodo.getYears();
        int meses =periodo.getMonths();

        if(años > 0 && meses>0){
            return años + " años" + meses + " meses";
        } else if (años>0) {
            return años + " años";
        }else {
            return meses + "meses";
        }
    }
}

