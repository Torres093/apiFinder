package grupoExpo.API.Repositories.Clientes;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Models.DTO.ClientesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientesRepository extends JpaRepository<ClientesEntity, String> {
}
