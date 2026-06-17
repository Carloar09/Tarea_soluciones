package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.DocumentoResponseDTO;
import Elicuci.czelada.recursoshumanos.entity.Documento;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import Elicuci.czelada.recursoshumanos.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    /**
     * POST /api/v1/documentos/subir
     * Sube un documento al expediente del empleado
     *
     * Angular lo llama con FormData:
     * formData.append('archivo', file)
     * formData.append('tipoDocumento', 'DNI')
     * formData.append('dniEmpleado', '47823651')
     */
    @PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoResponseDTO> subirDocumento(
            @RequestParam("dniEmpleado")   String dniEmpleado,
            @RequestParam("tipoDocumento") TipoDocumento tipoDocumento,
            @RequestParam("archivo")       MultipartFile archivo,
            @RequestParam(value = "incidenciaId", required = false) Long incidenciaId
    ) throws IOException {

        DocumentoResponseDTO response = documentoService.subirDocumento(
                dniEmpleado, tipoDocumento, archivo, incidenciaId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/documentos?dniEmpleado=47823651
     * GET /api/v1/documentos?dniEmpleado=47823651&tipoDocumento=DNI
     * Lista documentos del empleado con filtro opcional por tipo
     */
    @GetMapping
    public ResponseEntity<List<DocumentoResponseDTO>> listarDocumentos(
            @RequestParam String dniEmpleado,
            @RequestParam(required = false) TipoDocumento tipoDocumento
    ) {
        return ResponseEntity.ok(
                documentoService.listarDocumentos(dniEmpleado, tipoDocumento));
    }

    /**
     * DELETE /api/v1/documentos/{id}
     * Elimina el documento de BD y del servidor
     * Botón "Eliminar" en el Figma
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable Long id)
            throws IOException {
        documentoService.eliminarDocumento(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * GET /api/v1/documentos/{id}/descargar
     * Descarga el archivo para el botón "Descargar" del Figma
     * Angular recibe el archivo como blob
     */
    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarDocumento(@PathVariable Long id)
            throws IOException {

        byte[] contenido = documentoService.descargarDocumento(id);
        Documento doc = documentoService.getDocumento(id);

        // Detectamos el tipo de contenido para que el browser sepa qué abrir
        String tipoContenido = detectarTipoContenido(doc.getNombreArchivo());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getNombreArchivo() + "\"")
                .contentType(MediaType.parseMediaType(tipoContenido))
                .body(contenido);
    }

    // Detecta el Content-Type según la extensión del archivo
    private String detectarTipoContenido(String nombreArchivo) {
        if (nombreArchivo == null) return "application/octet-stream";
        String nombre = nombreArchivo.toLowerCase();
        if (nombre.endsWith(".pdf"))  return "application/pdf";
        if (nombre.endsWith(".jpg") ||
                nombre.endsWith(".jpeg")) return "image/jpeg";
        if (nombre.endsWith(".png"))  return "image/png";
        return "application/octet-stream";
    }
}