package grupoExpo.API.Repositories.DetallesServicioEvento;

import grupoExpo.API.Entities.DetallesServicioEvento.DetallesServicioEventoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesServicioEventoRepository extends JpaRepository<DetallesServicioEventoEntity, String> {

    Page<DetallesServicioEventoEntity> findAll(Pageable pageable);
}
