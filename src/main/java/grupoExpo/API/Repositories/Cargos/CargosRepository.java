package grupoExpo.API.Repositories.Cargos;

import grupoExpo.API.Entities.Cargos.CargosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargosRepository extends JpaRepository<CargosEntity, String> {

    Page<CargosEntity> findAll(Pageable pageable);
}
