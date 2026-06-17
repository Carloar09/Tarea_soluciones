package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Empleado;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByDni(String dni);

    // ── Contar empleados de una sucursal ────────────────
    // Spring no puede hacer esto automático porque sucursalId
    // está en Contrato, no en Empleado. Usamos @Query
    @Query("SELECT COUNT(DISTINCT e) FROM Empleado e " +
            "JOIN e.contratos c WHERE c.sucursal.idSucursal = :sucursalId")
    long countBySucursalId(@Param("sucursalId") Long sucursalId);

    // ── Contar por estado ───────────────────────────────
    long countByEstadoEmpleado(EstadoEmpleado estado);

    // ── Contar por estado Y sucursal ────────────────────
    @Query("SELECT COUNT(DISTINCT e) FROM Empleado e " +
            "JOIN e.contratos c " +
            "WHERE e.estadoEmpleado = :estado " +
            "AND c.sucursal.idSucursal = :sucursalId")
    long countByEstadoEmpleadoAndSucursalId(
            @Param("estado") EstadoEmpleado estado,
            @Param("sucursalId") Long sucursalId);

    // ── Listar empleados de una sucursal ────────────────
    @Query("SELECT DISTINCT e FROM Empleado e " +
            "JOIN e.contratos c WHERE c.sucursal.idSucursal = :sucursalId")
    List<Empleado> findBySucursalId(@Param("sucursalId") Long sucursalId);
}