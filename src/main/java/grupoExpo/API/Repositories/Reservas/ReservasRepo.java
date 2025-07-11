package grupoExpo.API.Repositories.Reservas;

import grupoExpo.API.Entities.User.ReservasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasRepo extends JpaRepository<ReservasEntity, Long> {
}
