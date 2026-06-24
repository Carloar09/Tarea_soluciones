package Elicuci.czelada.recursoshumanos.dto;

import lombok.Builder;
import lombok.Data;

// Lo que devuelve el backend al Angular después de login/register
@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private String nombre;
    private String email;
    private String rol;
    private String mensaje;
}