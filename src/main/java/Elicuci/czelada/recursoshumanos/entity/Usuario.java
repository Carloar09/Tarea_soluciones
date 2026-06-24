package Elicuci.czelada.recursoshumanos.entity;

import Elicuci.czelada.recursoshumanos.entity.enums.RolUusuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false,  unique =  true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,length = 100)
    private String nombre;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUusuario rolUusuario;
    @Column(nullable = false)
    private boolean activo=true;

}
