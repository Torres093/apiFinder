package grupoExpo.API.Repositories.TiposMantenimiento;

import grupoExpo.API.Entities.TiposMantenimiento.TiposMantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposMantenimientoRepository  extends JpaRepository<TiposMantenimientoEntity, String> {

    Page<TiposMantenimientoEntity> findAll(Pageable pageable);
}
