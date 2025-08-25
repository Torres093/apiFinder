package grupoExpo.API.Repositories.EstadosHabitacion;

import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosHabitacionRepository extends JpaRepository<EstadosHabitacionEntity, String> {
}
