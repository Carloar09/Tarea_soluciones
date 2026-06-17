package Elicuci.czelada.recursoshumanos.entity;

import Elicuci.czelada.recursoshumanos.entity.enums.SeveridadIncidencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="incidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;
 // lo mimso varias incidencias pertenecen a un empleado y a la vez crea el nombre de la tabla
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(nullable = false, length = 100)
    private String tipo;
    // el enumerate guarda los valores del enum con los valores predeteminados
    // y el nulable false se refiere a que sea requerido si o si
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeveridadIncidencia severidad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 1000)
    private String descripcion;

}
