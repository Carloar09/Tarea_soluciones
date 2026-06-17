package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.EmpleadoExpedienteDTO;
import Elicuci.czelada.recursoshumanos.dto.EmpleadoRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.EmpleadoTablaDTO;
import Elicuci.czelada.recursoshumanos.entity.Contrato;
import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.Sucursal;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import Elicuci.czelada.recursoshumanos.repository.ContratoRepository;
import Elicuci.czelada.recursoshumanos.repository.DocumentoRepository;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.SucursalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {
    private final ContratoRepository contratoRepository;
    private final SucursalRepository sucursalRepository;
    private final DocumentoRepository documentoRepository;
    private final EmpleadoRepository empleadoRepository;
    //Meotod para crear empleado nuevo
    @Transactional
    public EmpleadoExpedienteDTO createEmpleado(EmpleadoRequestDTO request) {
        if(empleadoRepository.findByDni(request.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un empleado con el DNI:" +request.getDni());
        }
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new RuntimeException(
                        "Sucursal no encontrada con ID: " + request.getSucursalId()));

        //Crear empleado
        Empleado empleado = new Empleado();
        empleado.setDni(request.getDni());
        empleado.setNombre(request.getNombres());
        empleado.setApellido(request.getApellidos());
        empleado.setTelefono(request.getTelefono());
        empleado.setEmail(request.getEmail());
        empleado.setDireccion(request.getDireccion());
        empleado.setContactoEmergencia(request.getContactoEmergencia());
        empleado.setAvatarURL(request.getAvatarUrl());
        //Se manda por defecto en true
        empleado.setEstadoEmpleado(request.getEstado() != null
                ?request.getEstado()
                : EstadoEmpleado.ACTIVO
        );
        Empleado empleadoGuardado = empleadoRepository.save(empleado);
    // se cre a el contrato inical del empleado
        Contrato contrato = new Contrato();
        contrato.setEmpleado(empleadoGuardado);
        contrato.setSucursal(sucursal);
        contrato.setArea(request.getArea());
        contrato.setCargo(request.getCargo());
        contrato.setFechaIngreso(request.getFechaIngreso());
        contrato.setFechaVencimiento(request.getFechaVencimiento());
        contratoRepository.save(contrato);
        // se devuelve al expedieemte completo del empleado
        return  getEmpleadoExpedienteDni(empleadoGuardado.getDni());
    }
    //Metodo para editar el personal
    @Transactional
    public EmpleadoExpedienteDTO editarEmpleado(Long id, EmpleadoRequestDTO request) {

        // busca el id
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con ID: " + id));

        // 2. Si cambia el DNI, verificamos que el nuevo no exista en otro empleado para evitar conflictos
        if (!empleado.getDni().equals(request.getDni())) {
            empleadoRepository.findByDni(request.getDni()).ifPresent(e -> {
                throw new RuntimeException("El DNI " + request.getDni() + " ya está en uso");
            });
        }


        //Actualkizamos los datos

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

        // 4. Devolvemos el expediente actualizado
        return getEmpleadoExpedienteDni(empleado.getDni());

    }
    // metodo para lista los empleados en base a sus sucursal donde trabaja
    public List<EmpleadoTablaDTO> listarEmpleados(Long sucursalId) {
        List<Empleado> empleados = sucursalId != null
                ? empleadoRepository.findBySucursalId(sucursalId)
                : empleadoRepository.findAll();

        return empleados.stream()
                .map(this::mapToTablaDTO)
                .collect(Collectors.toList());
    }
    private EmpleadoTablaDTO mapToTablaDTO(Empleado emp) {
        Contrato ultimo=null;

        if(emp.getContratos() != null && emp.getContratos().isEmpty()){
            ultimo=emp.getContratos().get(emp.getContratos().size()-1);
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


    public EmpleadoExpedienteDTO getEmpleadoExpedienteDni(String dni) {
        // buscamos al empleado por DNI si no existe se lansa el runtimeexcepetion
        Empleado empleado=empleadoRepository.findByDni(dni)
                .orElseThrow(()-> new RuntimeException("Empleado no encontrado con DNI: "+ dni));

        //lo que hace es que va comparando los elementos y te devuele el segundo
        // en este caso queremos el contrato acutal
        // esta es otra manera de hacerlo
        //List<Contrato> contratos = empleado.getContratos();
        //Contrato contratoActual = contratos.isEmpty()
        //? null
        //: contratos.get(contratos.size() - 1);
        Contrato contratoActual= empleado.getContratos()
                .stream()
                .reduce((first, second)->second)
                .orElse(null);
        //calculamoss los booleanos en la tabla de documento: hjace que cada llamda haga un SLECT exuts
        Long empId =empleado.getIdEmpleado();
        boolean hasDni = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.DNI);
        boolean hasAntencendes = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.ANTECEDENTES);
        boolean hasContrato = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.CONTRATO);
        boolean hasCertificado = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.CERTIFICADO);
        boolean hasBoleta = documentoRepository.existsByEmpleadoIdAndtipoDocumento(empId, TipoDocumento.BOLETA_PAGO);

        // vemos y mapeamos  las inicidencias u convertimos los DTOs para enviar al front
        var incidenciasDTO =empleado.getIncidencias()

                // es como si fuera un for va a recorrer todo
                .stream()
                // EmpleadoExpedienteDTO.IncidenciaDTO.builder() eso es igual
                // que decir IncidenciaDTO dto = new IncidenciaDTO
                .map(inc -> EmpleadoExpedienteDTO.IncidenciaDTO.builder()
                        .id_Incidencia(inc.getIdIncidencia())
                        .tipo(inc.getTipo())
                        .severidad(inc.getSeveridad().name())
                        .fecha(inc.getFecha().toString())
                        .descripcion(inc.getDescripcion())
                        // este es el contustor el objeto dto final depues de todos los llamados
                        // esc omo un new incidenciaDTO(id, tipo , etc)
                        .build())

                // y esto collect y es como si feura un incidenciasDTO.add(dto)
                .collect(Collectors.toList());

                // este es la otra forma de hacer si no usaramos la depencia LOMBOK
                //List<IncidenciaDTO> incidenciasDTO = new ArrayList<>();
                //for (Incidencia inc : empleado.getIncidencias()) {
                //IncidenciaDTO dto = IncidenciaDTO.builder()
                //.id(inc.getId())
                //.tipo(inc.getTipo())
                //.severidad(inc.getSeveridad().name())
                //.fecha(inc.getFecha().toString())
                //.descripcion(inc.getDescripcion())
                //.build();
                //incidenciasDTO.add(dto);
        var documetnosDTO=empleado.getDocumentos()
                .stream()
                .map(doc-> EmpleadoExpedienteDTO.DocumentoDTO.builder()
                        .id_Documento(doc.getIdDocumento())
                        .tipoDocumento(doc.getTipoDocumento().name())
                        .nombreArchivo(doc.getNombreArchivo())
                        .rutaArchivo(doc.getRutaArchivo())
                        .fechaSubida(doc.getFechaSubida().toString())
                        .build())
                .collect(Collectors.toList());

        // vamos a retornar los Dto usando builder
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

                // contrato
                .area(contratoActual !=null ? contratoActual.getArea():null)
                .cargo(contratoActual !=null ? contratoActual.getCargo():null )
                .sucursalNombre(contratoActual != null ? contratoActual.getSucursal().getNombre():null)
                .fechaIngreso(contratoActual !=null ? contratoActual.getFechaIngreso():null)
                .fechaVencimiento(contratoActual != null ? contratoActual.getFechaVencimiento():null)
                //booleanos
                .hasDni(hasDni)
                .hasAntecedentes(hasAntencendes)
                .hasContrato(hasContrato)
                .hasCertificado(hasCertificado)
                .hasBoleta(hasBoleta)

                //listas
                .incidenciaDTO(incidenciasDTO)
                .documentoDTO(documetnosDTO)
                .build();



    }
}
