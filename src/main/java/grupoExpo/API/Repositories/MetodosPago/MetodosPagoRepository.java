package grupoExpo.API.Repositories.MetodosPago;

import grupoExpo.API.Entities.MetodosPago.MetodosPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodosPagoRepository extends JpaRepository<MetodosPagoEntity, String> {
}
