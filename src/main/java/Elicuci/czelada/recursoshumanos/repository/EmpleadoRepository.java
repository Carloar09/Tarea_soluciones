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

    @Query("SELECT DISTINCT e FROM Empleado e JOIN e.contratos c WHERE c.sucursal.id = :sucursalId")
    List<Empleado> findBySucursalId(@Param("sucursalId") Long sucursalId);

    //nos va a servir apra contar empleados por sucursal
    long countBySucursalId(Long sucursalId);

    // nos va  a servir para contar por estado
    long countByEstadoEmpleado(EstadoEmpleado estadoEmpleado);
    @Query("SELECT COUNT(DISTINCT e) FROM Empleado e JOIN e.contratos c " +
            "WHERE e.estadoEmpleado = :estado AND c.sucursal.id = :sucursalId")
    long countByEstadoEmpleadoAndSucursalId(
            @Param("estado") EstadoEmpleado estado,
            @Param("sucursalId") Long sucursalId);

}
