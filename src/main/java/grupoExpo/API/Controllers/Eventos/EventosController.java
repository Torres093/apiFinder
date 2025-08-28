package grupoExpo.API.Controllers.Eventos;

import grupoExpo.API.Models.DTO.EventosDTO;
import grupoExpo.API.Services.Eventos.EventosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventosController {

    @Autowired
    private EventosService acceso;

    @GetMapping("/consultarEventos")
    public List<EventosDTO> datosEventos(){
        return acceso.getAllEventos();
    }
}
