package grupoExpo.API.Repositories.CategoriasTipoHabitacion;

import grupoExpo.API.Entities.CategoriasTipoHabitacion.CategoriasTipoHabitacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriasTipoHabitacionRepository extends JpaRepository<CategoriasTipoHabitacionEntity, String> {

    Page<CategoriasTipoHabitacionEntity> findAll(Pageable pageable);
}
