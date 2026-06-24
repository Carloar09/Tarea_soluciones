package Elicuci.czelada.recursoshumanos.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice intercepta TODAS las excepciones de todos los controllers
// Así Angular recibe siempre un JSON limpio en vez de un stacktrace enorme
@RestControllerAdvice
public class GlobalExceptionHandler {

    // RuntimeException — El más común
    // Ej: "Empleado no encontrado con DNI: 123"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("mensaje", ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Errores de validación — @Valid falla
    // Ej: campo obligatorio vacío, email inválido
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        // Recogemos todos los campos con error
        Map<String, String> camposConError = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            camposConError.put(campo, mensaje);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("mensaje", "Error de validación en los campos enviados");
        response.put("errores", camposConError);
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("mensaje", "Error interno del servidor: " + ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}