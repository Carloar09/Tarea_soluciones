package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

        @Query("SELECT i FROM Incidencia i WHERE i.empleado.idEmpleado = :empleadoId")
        List<Incidencia> findByEmpleadoId(@Param("empleadoId") Long empleadoId);

        // Contar incidencias entre fechas
        @Query("SELECT COUNT(i) FROM Incidencia i " +
                "WHERE i.fecha BETWEEN :inicio AND :fin")
        long countByFechaBetween(
                @Param("inicio") LocalDate inicio,
                @Param("fin") LocalDate fin);

        // Contar incidencias del mes por sucursal
        @Query("SELECT COUNT(i) FROM Incidencia i " +
                "JOIN i.empleado e JOIN e.contratos c " +
                "WHERE i.fecha BETWEEN :inicio AND :fin " +
                "AND c.sucursal.idSucursal = :sucursalId")
        long countByFechaBetweenAndSucursalId(
                @Param("inicio") LocalDate inicio,
                @Param("fin") LocalDate fin,
                @Param("sucursalId") Long sucursalId);

        // Incidencias GRAVE o MODERADA (por resolver)
        @Query("SELECT COUNT(i) FROM Incidencia i " +
                "WHERE i.severidad IN ('GRAVE', 'MODERADA')")
        long countPorResolver();

        // Incidencias por resolver filtradas por sucursal
        @Query("SELECT COUNT(i) FROM Incidencia i " +
                "JOIN i.empleado e JOIN e.contratos c " +
                "WHERE i.severidad IN ('GRAVE', 'MODERADA') " +
                "AND c.sucursal.idSucursal = :sucursalId")
        long countPorResolverBySucursal(@Param("sucursalId") Long sucursalId);

        // Últimas 10 para actividad reciente del dashboard
        List<Incidencia> findTop10ByOrderByFechaDesc();
}