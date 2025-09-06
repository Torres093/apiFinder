package grupoExpo.API.Repositories.Eventos;

import grupoExpo.API.Entities.Eventos.EventosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosRepository extends JpaRepository<EventosEntity, String> {

    Page<EventosEntity> findAll(Pageable pageable);
}
