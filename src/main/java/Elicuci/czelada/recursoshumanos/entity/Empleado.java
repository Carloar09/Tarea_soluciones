package Elicuci.czelada.recursoshumanos.entity;

import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
//crea la tabla empleados y al dni lo crea como un inidice primario para evitar duplicados
@Table(name="empleados",
        indexes = {
        @Index(name="idx_empleado_dni",columnList = "dni")
        }

)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idEmpleado;
    //dni en la base de datos se crea automaticamente un indice para evitar duplicados
    //gracias al unique que esta actuando com true
    @Column(nullable=false,unique = true ,length=8)// solo tiene el valor de 8
    private String dni;

    @Column(nullable=false,length=100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;
    @Column(length = 150)

    private String email;

    @Column( length = 150)
    private String direccion;

    @Column(length = 11)
    private String telefono;

    @Column(name = "contacto_emergencia", length = 200)
    private String contactoEmergencia;
    //se guarda como un texto en la base de datos para mayor flexibikldad
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private EstadoEmpleado estadoEmpleado;
    @Column(name = "avatar_url", length = 500)
    private String avatarURL;
    //onetomany se refiere a que un empleado puede tener muchos contratos
    //el mappedby indica que el contrato tiene un id osea un fk clave foranea
    //casscadetype es un borrado automaitoc si borras a un empleado se borrara con todos sus contratos
    //fetch lazy hace que no se cagra los contratos hasta que lo solicites o necesites
    //para mejorar rendimiento y no sobrecargarlo
    @OneToMany(mappedBy ="empleado",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Contrato> contratos;
    //aplica lo mismo que arriba el empleado peude tener muchas incidencia
    @OneToMany(mappedBy ="empleado",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Incidencia> incidencias;
    // el empleado puede tener muchos documentos
    @OneToMany(mappedBy ="empleado",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Documento> documentos;

    @OneToMany(mappedBy = "empleado",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Salida> salidas;
}
