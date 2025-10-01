package grupoExpo.API.Repositories.Usuarios;

import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, String> {

    Page<UsuariosEntity> findAll(Pageable pageable);

    Optional<UsuariosEntity> findByCorreoUsuario(String correoUsuario);
}
