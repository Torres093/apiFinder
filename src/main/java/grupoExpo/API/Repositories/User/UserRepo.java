package grupoExpo.API.Repositories.User;

import grupoExpo.API.Entities.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
}
