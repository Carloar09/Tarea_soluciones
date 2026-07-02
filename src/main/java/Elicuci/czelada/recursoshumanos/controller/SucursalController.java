package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.SucursalStatsDTO;
import Elicuci.czelada.recursoshumanos.entity.Sucursal;
import Elicuci.czelada.recursoshumanos.repository.SucursalRepository;
import Elicuci.czelada.recursoshumanos.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalRepository sucursalRepository;
    private final SucursalService sucursalService;

    // GET /api/v1/sucursales — Ahora devuelve stats incluidas
    @GetMapping
    public ResponseEntity<List<SucursalStatsDTO>> listar() {
        return ResponseEntity.ok(sucursalService.getSucursalesConStats());
    }

    // POST /api/v1/sucursales — Crear sucursal
    @PostMapping
    public ResponseEntity<Sucursal> crear(@RequestBody Sucursal sucursal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sucursalRepository.save(sucursal));
    }

    // DELETE /api/v1/sucursales/{id} — Eliminar sucursal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!sucursalRepository.existsById(id)) {
            throw new RuntimeException("Sucursal no encontrada con ID: " + id);
        }
        sucursalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}