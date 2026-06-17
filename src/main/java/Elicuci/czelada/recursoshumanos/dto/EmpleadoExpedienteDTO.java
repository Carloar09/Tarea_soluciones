package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.Incidencia;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

//DTO sirve para transferir los obejtos, esto sirve para el frontend
//asi controlamos los datos que exponemos
@Data
@Builder
public class EmpleadoExpedienteDTO {
    private Long id;
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String direccion;
    private String contactoEmergencia;
    private EstadoEmpleado estadoEmpleado;
    private String avatarURl;

    // contrato activo(el mas reciente)
    private String area;
    private String cargo;
    private String sucursalNombre;
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;

    //aqui creamos los checklist para el front esots no se guardan en la BD solo se calcula en el servicio
    private boolean hasDni;
    private boolean hasAntecedentes;
    private boolean hasContrato;
    private boolean hasCertificado;
    private boolean hasBoleta;

    private List<IncidenciaDTO> incidenciaDTO;
    private List<DocumentoDTO> documentoDTO;

    //DTO internos

    @Data
    @Builder
    public static class IncidenciaDTO {
        private Long id_Incidencia;
        private String tipo;
        private String severidad;
        private String fecha;
        private String descripcion;

    }
    @Data
    @Builder
    public static class DocumentoDTO {
        private Long id_Documento;
        private String tipoDocumento;
        private String nombreArchivo;
        private String rutaArchivo;
        private String fechaSubida;
    }

}
