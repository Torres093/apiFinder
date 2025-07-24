package grupoExpo.API.Repositories.Habitaciones;

import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HabitacionesRepository extends JpaRepository<HabitacionesEntity, String> {
}
