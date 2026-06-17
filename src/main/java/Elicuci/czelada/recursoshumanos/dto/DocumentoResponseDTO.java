package Elicuci.czelada.recursoshumanos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentoResponseDTO {

    private Long idDocumento;
    private String tipoDocumento;
    private String nombreArchivo;
    private String rutaArchivo;
    private String fechaSubida;
    private Long incidenciaId;
}
