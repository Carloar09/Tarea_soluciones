package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.SalidaRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.SalidaResponseDTO;
import Elicuci.czelada.recursoshumanos.entity.Salida;
import Elicuci.czelada.recursoshumanos.service.SalidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salidas")
@RequiredArgsConstructor
public class SalidaController {
    private final SalidaService salidaService;

    // aqui vamos hacer el metodo post para registrar la slida y cambie su estado el empleado

    @PostMapping
    public ResponseEntity<SalidaResponseDTO>registrarSalida(
        @Valid@RequestBody SalidaRequestDTO request){
        SalidaResponseDTO response = salidaService.registrarSalida(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //metodo GET PARA LISTAR todo

    @GetMapping
    public ResponseEntity<List<SalidaResponseDTO>> ListarSalidas(){
        return ResponseEntity.ok(salidaService.listarSalidas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalidaResponseDTO>getDetalle(@PathVariable Long id){
        return ResponseEntity.ok(salidaService.getDetalle(id));
    }


}
