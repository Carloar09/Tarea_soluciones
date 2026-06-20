package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.EmpleadoExpedienteDTO;
import Elicuci.czelada.recursoshumanos.dto.EmpleadoRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.EmpleadoTablaDTO;
import Elicuci.czelada.recursoshumanos.entity.Contrato;
import Elicuci.czelada.recursoshumanos.entity.Documento;
import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.Incidencia;
import Elicuci.czelada.recursoshumanos.entity.Sucursal;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import Elicuci.czelada.recursoshumanos.repository.ContratoRepository;
import Elicuci.czelada.recursoshumanos.repository.DocumentoRepository;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.SucursalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final ContratoRepository contratoRepository;
    private final SucursalRepository sucursalRepository;
    private final DocumentoRepository documentoRepository;
    private final EmpleadoRepository empleadoRepository;

    // CREAR EMPLEADO

    @Transactional
    public EmpleadoExpedienteDTO createEmpleado(EmpleadoRequestDTO request) {

        if (empleadoRepository.findByDni(request.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un empleado con el DNI: " + request.getDni());
        }

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RuntimeException(
                        "Sucursal no encontrada con ID: " + request.getSucursalId()));

        Empleado empleado = new Empleado();
        empleado.setDni(request.getDni());
        empleado.setNombre(request.getNombres());
        empleado.setApellido(request.getApellidos());
        empleado.setTelefono(request.getTelefono());
        empleado.setEmail(request.getEmail());
        empleado.setDireccion(request.getDireccion());
        empleado.setContactoEmergencia(request.getContactoEmergencia());
        empleado.setAvatarURL(request.getAvatarUrl());
        empleado.setEstadoEmpleado(request.getEstado() != null
                ? request.getEstado()
                : EstadoEmpleado.ACTIVO);

        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        Contrato contrato = new Contrato();
        contrato.setEmpleado(empleadoGuardado);
        contrato.setSucursal(sucursal);
        contrato.setArea(request.getArea());
        contrato.setCargo(request.getCargo());
        contrato.setFechaIngreso(request.getFechaIngreso());
        contrato.setFechaVencimiento(request.getFechaVencimiento());
        contratoRepository.save(contrato);

        return getEmpleadoExpedienteDni(empleadoGuardado.getDni());
    }

    // EDITAR EMPLEADO

    @Transactional
    public EmpleadoExpedienteDTO editarEmpleado(Long id, EmpleadoRequestDTO request) {

        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con ID: " + id));

        if (!empleado.getDni().equals(request.getDni())) {
            empleadoRepository.findByDni(request.getDni()).ifPresent(e -> {
                throw new RuntimeException("El DNI " + request.getDni() + " ya está en uso");
            });
        }

        empleado.setDni(request.getDni());
        empleado.setNombre(request.getNombres());
        empleado.setApellido(request.getApellidos());
        empleado.setTelefono(request.getTelefono());
        empleado.setEmail(request.getEmail());
        empleado.setDireccion(request.getDireccion());
        empleado.setContactoEmergencia(request.getContactoEmergencia());
        empleado.setAvatarURL(request.getAvatarUrl());
        if (request.getEstado() != null) {
            empleado.setEstadoEmpleado(request.getEstado());
        }

        empleadoRepository.save(empleado);
        return getEmpleadoExpedienteDni(empleado.getDni());
    }


    // LISTAR EMPLEADOS

    public List<EmpleadoTablaDTO> listarEmpleados(Long sucursalId) {
        List<Empleado> empleados = sucursalId != null
                ? empleadoRepository.findBySucursalId(sucursalId)
                : empleadoRepository.findAll();

        return empleados.stream()
                .map(this::mapToTablaDTO)
                .collect(Collectors.toList());
    }

    // GET EXPEDIENTE POR DNI

    public EmpleadoExpedienteDTO getEmpleadoExpedienteDni(String dni) {

        Empleado empleado = empleadoRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con DNI: " + dni));

        // en contratoscheck
        Contrato contratoActual = null;
        if (empleado.getContratos() != null && !empleado.getContratos().isEmpty()) {
            contratoActual = empleado.getContratos()
                    .stream()
                    .reduce((first, second) -> second)
                    .orElse(null);
        }

        // Booleanos de documentos
        Long empId = empleado.getIdEmpleado();
        boolean hasDni         = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.DNI);
        boolean hasAntencendes = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.ANTECEDENTES);
        boolean hasContrato    = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.CONTRATO);
        boolean hasCertificado = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.CERTIFICADO);
        boolean hasBoleta      = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.BOLETA_PAGO);

        //  en incidencias con tipo explícito
        List<Incidencia> listaIncidencias = empleado.getIncidencias() != null
                ? empleado.getIncidencias()
                : new ArrayList<>();

        var incidenciasDTO = listaIncidencias.stream()
                .map(inc -> EmpleadoExpedienteDTO.IncidenciaDTO.builder()
                        .id_Incidencia(inc.getIdIncidencia())
                        .tipo(inc.getTipo())
                        .severidad(inc.getSeveridad().name())
                        .fecha(inc.getFecha().toString())
                        .descripcion(inc.getDescripcion())
                        .build())
                .collect(Collectors.toList());

        // check en documentos con tipo explícito
        List<Documento> listaDocumentos = empleado.getDocumentos() != null
                ? empleado.getDocumentos()
                : new ArrayList<>();

        var documetnosDTO = listaDocumentos.stream()
                .map(doc -> EmpleadoExpedienteDTO.DocumentoDTO.builder()
                        .id_Documento(doc.getIdDocumento())
                        .tipoDocumento(doc.getTipoDocumento().name())
                        .nombreArchivo(doc.getNombreArchivo())
                        .rutaArchivo(doc.getRutaArchivo())
                        .fechaSubida(doc.getFechaSubida().toString())
                        .build())
                .collect(Collectors.toList());

        return EmpleadoExpedienteDTO.builder()
                .id(empleado.getIdEmpleado())
                .dni(empleado.getDni())
                .nombre(empleado.getNombre())
                .apellidos(empleado.getApellido())
                .telefono(empleado.getTelefono())
                .email(empleado.getEmail())
                .direccion(empleado.getDireccion())
                .contactoEmergencia(empleado.getContactoEmergencia())
                .estadoEmpleado(empleado.getEstadoEmpleado())
                .avatarURl(empleado.getAvatarURL())
                .area(contratoActual != null ? contratoActual.getArea() : null)
                .cargo(contratoActual != null ? contratoActual.getCargo() : null)
                .sucursalNombre(contratoActual != null ? contratoActual.getSucursal().getNombre() : null)
                .fechaIngreso(contratoActual != null ? contratoActual.getFechaIngreso() : null)
                .fechaVencimiento(contratoActual != null ? contratoActual.getFechaVencimiento() : null)
                .hasDni(hasDni)
                .hasAntecedentes(hasAntencendes)
                .hasContrato(hasContrato)
                .hasCertificado(hasCertificado)
                .hasBoleta(hasBoleta)
                .incidenciaDTO(incidenciasDTO)
                .documentoDTO(documetnosDTO)
                .build();
    }


    // MAPPER PRIVADO para tabla
    private EmpleadoTablaDTO mapToTablaDTO(Empleado emp) {
        Contrato ultimo = null;

        if (emp.getContratos() != null && !emp.getContratos().isEmpty()) {
            ultimo = emp.getContratos().get(emp.getContratos().size() - 1);
        }

        return EmpleadoTablaDTO.builder()
                .id(emp.getIdEmpleado())
                .dni(emp.getDni())
                .nombreCompleto(emp.getNombre() + " " + emp.getApellido())
                .avatarUrl(emp.getAvatarURL())
                .estado(emp.getEstadoEmpleado())
                .area(ultimo != null ? ultimo.getArea() : null)
                .cargo(ultimo != null ? ultimo.getCargo() : null)
                .sucursalNombre(ultimo != null ? ultimo.getSucursal().getNombre() : null)
                .fechaIngreso(ultimo != null ? ultimo.getFechaIngreso() : null)
                .fechaVencimiento(ultimo != null ? ultimo.getFechaVencimiento() : null)
                .build();
    }
}