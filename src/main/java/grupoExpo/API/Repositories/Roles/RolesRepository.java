package grupoExpo.API.Repositories.Roles;

import grupoExpo.API.Entities.Roles.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, String> {
}
