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
        // va a buscar todas las inicidenias del empleado en base al id
        List<Incidencia> findByEmpleadoId(Long empleadoId);


        // para ver las incidencias actuales
        long countByFechaBetween(LocalDate inicio, LocalDate fin);

        // Incidencias del mes filtradas por sucursal
        @Query("SELECT COUNT(i) FROM Incidencia i JOIN i.empleado e JOIN e.contratos c " +
                "WHERE i.fecha BETWEEN :inicio AND :fin AND c.sucursal.id = :sucursalId")
        long countByFechaBetweenAndSucursalId(
                @Param("inicio") LocalDate inicio,
                @Param("fin") LocalDate fin,
                @Param("sucursalId") Long sucursalId);

        // Incidencias GRAVE o MODERADA (por resolver)
        @Query("SELECT COUNT(i) FROM Incidencia i WHERE i.severidad IN ('GRAVE', 'MODERADA')")
        long countPorResolver();

        @Query("SELECT COUNT(i) FROM Incidencia i JOIN i.empleado e JOIN e.contratos c " +
                "WHERE i.severidad IN ('GRAVE', 'MODERADA') AND c.sucursal.id = :sucursalId")

        long countPorResolverBySucursal(@Param("sucursalId") Long sucursalId);

        List<Incidencia> findTop10ByOrderByFechaDesc();
}
