package Elicuci.czelada.recursoshumanos.controller;

import Elicuci.czelada.recursoshumanos.dto.EmpleadoExpedienteDTO;
import Elicuci.czelada.recursoshumanos.dto.EmpleadoRequestDTO;
import Elicuci.czelada.recursoshumanos.dto.EmpleadoTablaDTO;
import Elicuci.czelada.recursoshumanos.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
public class EmpleadoController {
    private final EmpleadoService empleadoService;
    // responde a todos los epedientes
    @GetMapping("/expediente/{dni}")
    public ResponseEntity<EmpleadoExpedienteDTO>getEmpleado(@PathVariable String dni) {
        EmpleadoExpedienteDTO expedienteDTO=empleadoService.getEmpleadoExpedienteDni(dni);
        return ResponseEntity.ok(expedienteDTO);
    }
    //Crear Empleado
    @PostMapping
    public ResponseEntity<EmpleadoExpedienteDTO>crearEmpleado(
            @Valid @RequestBody EmpleadoRequestDTO requestDTO){
        EmpleadoExpedienteDTO response=empleadoService.createEmpleado(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    //Editar Empleado
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoExpedienteDTO> editarEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequestDTO request) {
        EmpleadoExpedienteDTO response = empleadoService.editarEmpleado(id, request);
        return ResponseEntity.ok(response);
    }
    // lista los empleado por el fitro de la sucursal
    @GetMapping
    public ResponseEntity<List<EmpleadoTablaDTO>> listarEmpleados(
            @RequestParam(required = false) Long sucursalId) {
        return ResponseEntity.ok(empleadoService.listarEmpleados(sucursalId));
    }

}
