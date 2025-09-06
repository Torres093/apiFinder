package grupoExpo.API.Repositories.Reservas;

import grupoExpo.API.Entities.Reservas.ReservasEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasRepository extends JpaRepository<ReservasEntity, String> {

    Page<ReservasEntity> findAll(Pageable pageable);
}
