package grupoExpo.API.Services.Eventos;

import grupoExpo.API.Entities.Eventos.EventosEntity;
import grupoExpo.API.Models.DTO.EventosDTO;
import grupoExpo.API.Repositories.Eventos.EventosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventosService {

    @Autowired
    private EventosRepository repo;

    public List<EventosDTO> getAllEventos() {
        List<EventosEntity> eventos = repo.findAll();
        return eventos.stream()
                .map(this::convertirAEventoDTO)
                .collect(Collectors.toList());
    }

    private EventosDTO convertirAEventoDTO(EventosEntity evento) {
        EventosDTO dto = new EventosDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setIdHotel(evento.getHotel().getIdHotel());
        dto.setNombreEvento(evento.getNombreEvento());
        dto.setDescripcionEvento(evento.getDescripcionEvento());
        dto.setFechaEvento(evento.getFechaEvento());
        dto.setCapacidadEvento(evento.getCapacidadEvento());
        dto.setPrecioEvento(evento.getPrecioEvento().doubleValue());
        return dto;
    }
}
