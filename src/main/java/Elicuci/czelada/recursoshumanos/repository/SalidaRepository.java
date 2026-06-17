package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Salida;
import Elicuci.czelada.recursoshumanos.entity.enums.EstadoEmpleado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalidaRepository extends CrudRepository<Salida, Long> {
    // para mostrar la lsita de salidas de los empleados
    List<Salida> findAllByOrderBySalidaDesc();
    //se guarda la lista para ver que empleados han salido
    List<Salida> findByEmpleadoId(Long empleadoId);
}
