package grupoExpo.API.Repositories.TiposHabitacion;

import grupoExpo.API.Entities.TiposHabitacion.TiposHabitacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposHabitacionRepository extends JpaRepository<TiposHabitacionEntity, String> {

    Page<TiposHabitacionEntity> findAll(Pageable pageable);
}
