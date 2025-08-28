package grupoExpo.API.Repositories.Mantenimientos;

import grupoExpo.API.Entities.Mantenimientos.MantenimientosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MantenimientosRepository extends JpaRepository<MantenimientosEntity, String> {
}
