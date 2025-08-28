package grupoExpo.API.Repositories.DetallesReserva;

import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesReservaRepository extends JpaRepository<DetallesReservaEntity, String> {
}
