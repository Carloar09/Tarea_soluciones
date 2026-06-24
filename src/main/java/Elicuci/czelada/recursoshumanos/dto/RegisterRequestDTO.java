package Elicuci.czelada.recursoshumanos.dto;

import Elicuci.czelada.recursoshumanos.entity.enums.RolUusuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private RolUusuario rol;
}