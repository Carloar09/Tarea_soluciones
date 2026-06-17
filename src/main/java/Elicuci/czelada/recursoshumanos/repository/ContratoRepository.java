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

    // Usamos @Query porque el campo se llama idEmpleado, no id
    @Query("SELECT c FROM Contrato c WHERE c.empleado.idEmpleado = :empleadoId")
    List<Contrato> findByEmpleadoId(@Param("empleadoId") Long empleadoId);

    // Contratos próximos a vencer (todos)
    @Query("SELECT c FROM Contrato c " +
            "WHERE c.fechaVencimiento BETWEEN :hoy AND :limite")
    List<Contrato> findProximosAVencer(
            @Param("hoy") LocalDate hoy,
            @Param("limite") LocalDate limite);

    // Contratos próximos a vencer filtrados por sucursal
    @Query("SELECT c FROM Contrato c " +
            "WHERE c.fechaVencimiento BETWEEN :hoy AND :limite " +
            "AND c.sucursal.idSucursal = :sucursalId")
    List<Contrato> findProximosAVencerBySucursal(
            @Param("hoy") LocalDate hoy,
            @Param("limite") LocalDate limite,
            @Param("sucursalId") Long sucursalId);
}