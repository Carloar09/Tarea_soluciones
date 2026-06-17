package Elicuci.czelada.recursoshumanos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="sucursale")
@Data //Genera gett y sett, tostring, equals automaticamente
@NoArgsConstructor
@AllArgsConstructor//constructor para los campos creados
public class Sucursal {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)//incrementa automaticante llave primaria
    private Long idSucursal;
    @Column(nullable=false,length=100)
    private String nombre;
    @Column(nullable=false,length=100)
    private String ubicacion;
    //se guarda el id del encargado
    @Column(name = "encargado_id")
    private Long encargadoid;

}
