package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Salida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalidaRepository extends JpaRepository<Salida, Long> {

    // Ordenado por fechaSalida descendente (así se llama el campo en la entidad)
    List<Salida> findAllByOrderByFechaSalidaDesc();

    // Necesita @Query porque idEmpleado no se llama "id"
    @Query("SELECT s FROM Salida s WHERE s.empleado.idEmpleado = :empleadoId")
    List<Salida> findByEmpleadoId(@Param("empleadoId") Long empleadoId);
}