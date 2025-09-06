package grupoExpo.API.Repositories.DetallesServicioPlato;

import grupoExpo.API.Entities.DetallesServicioPlato.DetallesServicioPlatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesServicioPlatoRepository extends JpaRepository<DetallesServicioPlatoEntity, String> {

    Page<DetallesServicioPlatoEntity> findAll(Pageable pageable);
}
