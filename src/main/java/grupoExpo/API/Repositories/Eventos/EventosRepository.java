package grupoExpo.API.Repositories.Eventos;

import grupoExpo.API.Entities.Eventos.EventosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosRepository extends JpaRepository<EventosEntity, String> {
}
