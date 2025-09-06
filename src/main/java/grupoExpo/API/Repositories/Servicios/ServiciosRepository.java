package grupoExpo.API.Repositories.Servicios;

import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiciosRepository extends JpaRepository<ServiciosEntity, String> {

    Page<ServiciosEntity> findAll(Pageable pageable);
}
