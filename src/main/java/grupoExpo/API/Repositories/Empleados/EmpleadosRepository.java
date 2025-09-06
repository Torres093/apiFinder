package grupoExpo.API.Repositories.Empleados;

import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadosRepository extends JpaRepository<EmpleadosEntity, String> {

    Page<EmpleadosEntity> findAll(Pageable pageable);
}
