package Elicuci.czelada.recursoshumanos.repository;

import Elicuci.czelada.recursoshumanos.entity.Documento;
import Elicuci.czelada.recursoshumanos.entity.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    boolean existsByEmpleadoIdAndtipoDocumento(Long empleadoId, TipoDocumento tipoDocumento);
    List<Documento> findByEmpleadoIdAndTipoDocumento(
            Long empleadoId,
            TipoDocumento tipoDocumento);

}
