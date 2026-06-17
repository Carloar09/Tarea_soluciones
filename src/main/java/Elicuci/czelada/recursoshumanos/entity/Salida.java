package Elicuci.czelada.recursoshumanos.entity;

import Elicuci.czelada.recursoshumanos.entity.enums.TipoSalida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Table(name = "salidas")
@AllArgsConstructor
@NoArgsConstructor
public class Salida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Salida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_salida",nullable=false)
    private TipoSalida tipoSalida;

    @Column(name="fecha_salida",nullable=false)
    private LocalDate fechaSalida;
    @Column(name="carta_recibida")
    private boolean cartaRecibida;
    @Column(name = "devolucion_materiales")
    private boolean devolucionDeMateriales;
    @Column(name = "liquidacion_firamda")
    private boolean liquidacionFirmada;
    @Column(length = 600)
    private String observaciones;


}
