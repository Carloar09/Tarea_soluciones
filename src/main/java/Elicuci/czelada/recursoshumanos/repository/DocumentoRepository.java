package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Documento;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query("SELECT d FROM Documento d WHERE d.empleado.idEmpleado = :empleadoId")
    List<Documento> findByEmpleadoId(@Param("empleadoId") Long empleadoId);

    // Para los booleanos del expediente
    @Query("SELECT COUNT(d) > 0 FROM Documento d " +
            "WHERE d.empleado.idEmpleado = :empleadoId " +
            "AND d.tipoDocumento = :tipo")
    boolean existsByEmpleadoIdAndtipoDocumento(
            @Param("empleadoId") Long empleadoId,
            @Param("tipo") TipoDocumento tipo);

    // Filtrar por tipo (dropdown del Figma)
    @Query("SELECT d FROM Documento d " +
            "WHERE d.empleado.idEmpleado = :empleadoId " +
            "AND d.tipoDocumento = :tipo")
    List<Documento> findByEmpleadoIdAndTipoDocumento(
            @Param("empleadoId") Long empleadoId,
            @Param("tipo") TipoDocumento tipo);
}