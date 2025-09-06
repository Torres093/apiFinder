package grupoExpo.API.Repositories.EstadosReserva;

import grupoExpo.API.Entities.EstadosReserva.EstadosReservaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosReservaRepository extends JpaRepository<EstadosReservaEntity, String> {

    Page<EstadosReservaEntity> findAll(Pageable pageable);
}
