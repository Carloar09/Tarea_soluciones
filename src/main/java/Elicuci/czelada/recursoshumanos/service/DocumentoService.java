package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.DocumentoResponseDTO;
import Elicuci.czelada.recursoshumanos.entity.Documento;
import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.Incidencia;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import Elicuci.czelada.recursoshumanos.repository.DocumentoRepository;
import Elicuci.czelada.recursoshumanos.repository.EmpleadoRepository;
import Elicuci.czelada.recursoshumanos.repository.IncidenciaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final IncidenciaRepository incidenciaRepository;
    private final ArchivoService archivoService;

    // Subir documento a un empleado
    // Pantalla "Expediente > Documentos"

    @Transactional
    public DocumentoResponseDTO subirDocumento(
            String dniEmpleado,
            TipoDocumento tipoDocumento,
            MultipartFile archivo,
            Long incidenciaId  // Nullable, solo para memorándums
    ) throws IOException {

        // 1. Buscamos el empleado
        Empleado empleado = empleadoRepository.findByDni(dniEmpleado)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con DNI: " + dniEmpleado));

        // 2. Guardamos el archivo físico y obtenemos la ruta
        String rutaArchivo = archivoService.guardarArchivo(archivo, dniEmpleado);

        // 3. Buscamos la incidencia si viene (solo para MEMORANDUM)
        Incidencia incidencia = null;
        if (incidenciaId != null) {
            incidencia = incidenciaRepository.findById(incidenciaId)
                    .orElseThrow(() -> new RuntimeException(
                            "Incidencia no encontrada con ID: " + incidenciaId));
        }

        // 4. Creamos el registro en BD
        Documento documento = new Documento();
        documento.setEmpleado(empleado);
        documento.setTipoDocumento(tipoDocumento);
        documento.setNombreArchivo(archivo.getOriginalFilename());
        documento.setRutaArchivo(rutaArchivo);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setIncidencia(incidencia); // null si no es memorándum

        Documento guardado = documentoRepository.save(documento);

        return mapToResponseDTO(guardado);
    }


    // Listar documentos de un empleado
    // Con filtro opcional por tipo

    public List<DocumentoResponseDTO> listarDocumentos(
            String dniEmpleado,
            TipoDocumento tipoDocumento  // null = todos
    ) {
        Empleado empleado = empleadoRepository.findByDni(dniEmpleado)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado con DNI: " + dniEmpleado));

        List<Documento> documentos = tipoDocumento != null
                ? documentoRepository.findByEmpleadoIdAndTipoDocumento(
                empleado.getIdEmpleado(), tipoDocumento)
                : documentoRepository.findByEmpleado_IdEmpleado(empleado.getIdEmpleado());

        return documentos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    // Eliminar documento
    // Botón "Eliminar"
    @Transactional
    public void eliminarDocumento(Long documentoId) throws IOException {

        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException(
                        "Documento no encontrado con ID: " + documentoId));

        // 1. Eliminamos el archivo físico del servidor
        archivoService.eliminarArchivo(documento.getRutaArchivo());

        // 2. Eliminamos el registro de la BD
        documentoRepository.delete(documento);
    }

    // Descargar documento
    // Botón "Descargar"

    public byte[] descargarDocumento(Long documentoId) throws IOException {

        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException(
                        "Documento no encontrado con ID: " + documentoId));

        return archivoService.leerArchivo(documento.getRutaArchivo());
    }

    // Obtener el nombre y tipo para el header de descarga

    public Documento getDocumento(Long documentoId) {
        return documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException(
                        "Documento no encontrado con ID: " + documentoId));
    }

    // Mapper privado
    private DocumentoResponseDTO mapToResponseDTO(Documento doc) {
        return DocumentoResponseDTO.builder()
                .idDocumento(doc.getIdDocumento())
                .tipoDocumento(doc.getTipoDocumento().name())
                .nombreArchivo(doc.getNombreArchivo())
                .rutaArchivo(doc.getRutaArchivo())
                .fechaSubida(doc.getFechaSubida().toString())
                .incidenciaId(doc.getIncidencia() != null
                        ? doc.getIncidencia().getIdIncidencia() : null)
                .build();
    }
}
