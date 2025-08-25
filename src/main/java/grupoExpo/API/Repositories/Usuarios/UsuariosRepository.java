package grupoExpo.API.Repositories.Usuarios;

import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, String> {
}
