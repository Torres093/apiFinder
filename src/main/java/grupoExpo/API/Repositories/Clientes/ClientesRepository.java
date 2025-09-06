package grupoExpo.API.Repositories.Clientes;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends JpaRepository<ClientesEntity, String> {

    Page<ClientesEntity> findAll(Pageable pageable);
}
