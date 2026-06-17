package Elicuci.czelada.recursoshumanos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrato;
    //el manytoone hace relacion con el empleado como deicr muchos contratos pertencen a un empleado
    //el joincolumn difenie el nombre de la columna de la clave forane en la tabla del contrato
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id",nullable = false)
    private Empleado empleado;
    //lo mismo que arriba se crea la erlacion y un contrato peude estar asociado a una sucursal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id",nullable = false)
    private Sucursal sucursal;
    @Column(nullable=false,length=100)
    private String area;
    @Column(nullable=false,length=100)
    private String cargo;
    @Column(name="fecha_igreso",length=100)
    private LocalDate fechaIngreso;
    //el nulable esta en true porque los conratos son indefinidos puede que tenga o no
    // en pocas palabras no es tan requerido  una ves que acaba
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
}
