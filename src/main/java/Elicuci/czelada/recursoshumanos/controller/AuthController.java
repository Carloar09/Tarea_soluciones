package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.AuthResponseDTO;
import Elicuci.czelada.recursoshumanos.dto.LoginRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.RegisterRequestDTO;
import Elicuci.czelada.recursoshumanos.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/register
     * Registra un nuevo usuario del sistema
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    /**
     * POST /api/v1/auth/login
     * Inicia sesión y devuelve el JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}