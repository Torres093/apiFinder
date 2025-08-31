package grupoExpo.API.Repositories.DetallesServicioPlato;

import grupoExpo.API.Entities.DetallesServicioPlato.DetallesServicioPlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesServicioPlatoRepository extends JpaRepository<DetallesServicioPlatoEntity, String> {
}
