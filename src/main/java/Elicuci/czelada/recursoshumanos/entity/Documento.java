package Elicuci.czelada.recursoshumanos.entity;

import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="documentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "TipoDocumento_id",nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "ruta_archivo",nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    //  el FK el nullable esta en true porque indica que no es tan requerido se va en el enum
    //solo sesubira archivos de incicendia cuadno pase la incidencia
    @JoinColumn(name = "incidencia_id", nullable = true)
    private Incidencia incidencia;
}
