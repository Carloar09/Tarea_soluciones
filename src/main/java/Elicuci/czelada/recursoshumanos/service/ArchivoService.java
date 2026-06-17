package Elicuci.czelada.recursoshumanos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ArchivoService {
   @Value("${archivos.ruta-base}")
    private String rutaBase;
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
            "application/pdf",   // PDF
            "image/jpeg",        // JPG
            "image/png"          // PNG
    );

    private static final long Tamanio_Maximo=10*1024*1024;

    public String guardarArchivo(MultipartFile archivo, String dniEmpleado) throws IOException {

        //  Validamos que no esté vacío
        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        // Validamos el tipo de archivo
        String tipoArchivo = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipoArchivo)) {
            throw new RuntimeException(
                    "Formato no permitido. Solo se aceptan PDF, JPG y PNG");
        }

        //  Validamos el tamaño
        if (archivo.getSize() > Tamanio_Maximo) {
            throw new RuntimeException("El archivo supera el límite de 10MB");
        }

        //  Creamos la carpeta del empleado si no existe
        // Estructura: uploads/documentos/DNI_empleado/
        // Ejemplo:    uploads/documentos/47823651/
        Path carpetaEmpleado = Paths.get(rutaBase, dniEmpleado);
        if (!Files.exists(carpetaEmpleado)) {
            Files.createDirectories(carpetaEmpleado);
        }

        // Generamos nombre único para evitar colisiones
        // Ejemplo: a3f9c2d1-dni_copia.pdf
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = obtenerExtension(nombreOriginal);
        String nombreUnico = UUID.randomUUID().toString() + "_" + nombreOriginal;

        //  Guardamos el archivo físicamente
        Path rutaDestino = carpetaEmpleado.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaDestino,
                StandardCopyOption.REPLACE_EXISTING);

        //  Devolvemos la ruta relativa para guardar en BD
        // Ejemplo: "uploads/documentos/47823651/a3f9c2d1_dni_copia.pdf"
        return rutaDestino.toString();
    }
    // Elimina el archivo físico del servidor
    // Se llama cuando el usuario presiona "Eliminar" en el Figma

    public void eliminarArchivo(String rutaArchivo) throws IOException {
        Path ruta = Paths.get(rutaArchivo);
        if (Files.exists(ruta)) {
            Files.delete(ruta);
        }
    }

    // Lee el archivo del servidor para descargarlo
    // Se llama cuando el usuario presiona "Descargar" en el Figma

    public byte[] leerArchivo(String rutaArchivo) throws IOException {
        Path ruta = Paths.get(rutaArchivo);
        if (!Files.exists(ruta)) {
            throw new RuntimeException("Archivo no encontrado en el servidor");
        }
        return Files.readAllBytes(ruta);
    }

    // Helper para obtener la extensión del archivo
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }
}
