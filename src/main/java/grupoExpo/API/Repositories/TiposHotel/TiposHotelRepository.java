package grupoExpo.API.Repositories.TiposHotel;

import grupoExpo.API.Entities.TiposHotel.TiposHotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposHotelRepository extends JpaRepository<TiposHotelEntity, String> {

    Page<TiposHotelEntity> findAll(Pageable pageable);
}
