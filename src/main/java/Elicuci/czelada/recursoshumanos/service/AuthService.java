package Elicuci.czelada.recursoshumanos.service;

import Elicuci.czelada.recursoshumanos.dto.AuthResponseDTO;
import Elicuci.czelada.recursoshumanos.dto.LoginRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.RegisterRequestDTO;
import Elicuci.czelada.recursoshumanos.entity.Usuario;
import Elicuci.czelada.recursoshumanos.repository.UsuarioRepository;
import Elicuci.czelada.recursoshumanos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // ─────────────────────────────────────────────────
    // Registro de nuevo usuario
    // ─────────────────────────────────────────────────
    public AuthResponseDTO register(RegisterRequestDTO request) {

        // Verificar que el email no exista
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Ya existe un usuario con el email: " + request.getEmail());
        }

        // Crear usuario con contraseña encriptada
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRolUusuario(request.getRol());
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        // Generar token
        String token = jwtService.generarToken(usuario.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRolUusuario().name())
                .mensaje("Usuario registrado exitosamente")
                .build();
    }

    // ─────────────────────────────────────────────────
    // Login
    // ─────────────────────────────────────────────────
    public AuthResponseDTO login(LoginRequestDTO request) {

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "Credenciales incorrectas"));

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // Verificar que esté activo
        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario desactivado");
        }

        // Generar token
        String token = jwtService.generarToken(usuario.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRolUusuario().name())
                .mensaje("Login exitoso")
                .build();
    }
}