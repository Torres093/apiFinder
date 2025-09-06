package grupoExpo.API.Repositories.CheckIns;

import grupoExpo.API.Entities.CheckIns.CheckInsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInsRepository extends JpaRepository<CheckInsEntity, String> {

    Page<CheckInsEntity> findAll(Pageable pageable);
}
