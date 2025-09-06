package grupoExpo.API.Repositories.Reseñas;

import grupoExpo.API.Entities.Reseñas.ReseñasEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseñasRepository extends JpaRepository<ReseñasEntity, String> {

    Page<ReseñasEntity> findAll(Pageable pageable);
}
