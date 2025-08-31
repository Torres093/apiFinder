package grupoExpo.API.Repositories.CheckOuts;

import grupoExpo.API.Entities.CheckOuts.CheckOutsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckOutsRepository extends JpaRepository<CheckOutsEntity, String> {
}
