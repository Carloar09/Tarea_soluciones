package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.IncidenciaRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.IncidenciaResponseDTO;
import Elicuci.czelada.recursoshumanos.service.IncidenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidencias")
@RequiredArgsConstructor
public class IncidenciaController {
    private final IncidenciaService incidenciaService;

    /**
     * POST /api/v1/incidencias
     * Registra una incidencia
     * Botón "Registrar incidencia" del Figma
     */
    @PostMapping
    public ResponseEntity<IncidenciaResponseDTO> registrar(
            @Valid @RequestBody IncidenciaRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(incidenciaService.registrarIncidencia(request));
    }

    /**
     * GET /api/v1/incidencias
     * Lista todas las incidencias
     * Pantalla "Incidencias" del Figma
     */
    @GetMapping
    public ResponseEntity<List<IncidenciaResponseDTO>> listar() {
        return ResponseEntity.ok(incidenciaService.listarIncidencias());
    }

    /**
     * GET /api/v1/incidencias/empleado/{dni}
     * Lista incidencias de un empleado específico
     * Pestaña "Historial de incidentes" del expediente en el Figma
     */
    @GetMapping("/empleado/{dni}")
    public ResponseEntity<List<IncidenciaResponseDTO>> listarPorEmpleado(
            @PathVariable String dni) {
        return ResponseEntity.ok(incidenciaService.listarPorEmpleado(dni));
    }
}
