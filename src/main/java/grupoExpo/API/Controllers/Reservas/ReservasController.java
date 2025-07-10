package grupoExpo.API.Controllers.Reservas;

import grupoExpo.API.Models.DTO.ReservasDto;
import grupoExpo.API.Services.Reservas.ReservasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/apiReservas")
public class ReservasController {

    @Autowired
    private ReservasService service;

    @GetMapping("/getDataReservas")
    public List<ReservasDto> getReservas(){ return service.getAllReservas();}
}
