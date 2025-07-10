package grupoExpo.API.Services.Reservas;


import grupoExpo.API.Models.DTO.ReservasDto;
import grupoExpo.API.Repositories.Reservas.ReservasRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservasService {

    @Autowired
    private ReservasRepo repo;

    public List<ReservasDto> getReservas(){
    List <ReservasEnity>
    }
}
