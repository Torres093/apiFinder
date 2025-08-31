package grupoExpo.API.Repositories.Platos;

import grupoExpo.API.Entities.Platos.PlatosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatosRepository extends JpaRepository<PlatosEntity, String> {
}
