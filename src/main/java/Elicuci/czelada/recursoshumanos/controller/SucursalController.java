package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.entity.Sucursal;
import Elicuci.czelada.recursoshumanos.repository.SucursalRepository;
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

    // GET /api/v1/sucursales — Lista todas
    @GetMapping
    public ResponseEntity<List<Sucursal>> listar() {
        return ResponseEntity.ok(sucursalRepository.findAll());
    }

    // POST /api/v1/sucursales — Crea una sucursal
    @PostMapping
    public ResponseEntity<Sucursal> crear(@RequestBody Sucursal sucursal) {
        Sucursal guardada = sucursalRepository.save(sucursal);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
}