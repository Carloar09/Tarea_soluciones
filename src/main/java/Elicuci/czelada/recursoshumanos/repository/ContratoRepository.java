package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    //Buscar los contratos de un empleado por su id
    List<Contrato> findByEmpleadoId(Long empleadoId);

    // Contratos que vencen entre hoy y los próximos 90 días
    @Query("SELECT c FROM Contrato c WHERE c.fechaVencimiento BETWEEN :hoy AND :limite")
    List<Contrato> findProximosAVencer(
            @Param("hoy") LocalDate hoy,
            @Param("limite") LocalDate limite);

    // Lo mismo filtrado por sucursal
    @Query("SELECT c FROM Contrato c WHERE c.fechaVencimiento BETWEEN :hoy AND :limite " +
            "AND c.sucursal.id = :sucursalId")
    List<Contrato> findProximosAVencerBySucursal(
            @Param("hoy") LocalDate hoy,
            @Param("limite") LocalDate limite,
            @Param("sucursalId") Long sucursalId);
}

