package grupoExpo.API.Repositories.Hotel;

import grupoExpo.API.Entities.Hotel.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, String> {

}
