package grupoExpo.API.Repositories.DetallesReservaServicio;

import grupoExpo.API.Entities.DetallesReservaServicio.DetallesReservaServicioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesReservaServicioRepository extends JpaRepository<DetallesReservaServicioEntity, String> {

    Page<DetallesReservaServicioEntity> findAll(Pageable pageable);
}
