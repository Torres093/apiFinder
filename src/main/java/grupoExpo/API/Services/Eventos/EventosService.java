package grupoExpo.API.Services.Eventos;

import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import grupoExpo.API.Entities.Eventos.EventosEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.TiposHabitacion.TiposHabitacionEntity;
import grupoExpo.API.Exceptions.Eventos.ExcepcionEventoNoEncontrado;
import grupoExpo.API.Exceptions.Eventos.ExcepcionEventoNoRegistrado;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoRegistrado;
import grupoExpo.API.Models.DTO.EventosDTO;
import grupoExpo.API.Repositories.Eventos.EventosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    public EventosDTO insertarDatos(EventosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            EventosEntity entity = ConvertirAEntity(data);
            EventosEntity eventoGuardado = repo.save(entity);
            return convertirAEventoDTO(eventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el evento: " + e.getMessage());
            throw new ExcepcionEventoNoRegistrado("Error al registrar el evento.");
        }
    }

    private EventosEntity ConvertirAEntity(EventosDTO data) {
        EventosEntity entity = new EventosEntity();

        //Asignando Hotel a entity de Eventos
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(data.getIdHotel());
        entity.setHotel(hotel);

        //Asignando atributos de DTO a entity
        entity.setNombreEvento(data.getNombreEvento());
        entity.setDescripcionEvento(data.getDescripcionEvento());
        entity.setFechaEvento(data.getFechaEvento());
        entity.setCapacidadEvento(data.getCapacidadEvento());
        entity.setPrecioEvento(BigDecimal.valueOf(data.getPrecioEvento()));
        return entity;
    }

    public EventosDTO actualizarEvento(String id, EventosDTO json) {
        //1. Verificar la existencia del evento.
        EventosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionEventoNoEncontrado("Evento no encontrado"));
        //2. Actualizar los campos

        //Asignando Hotel a entity de Eventos
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(json.getIdHotel());
        existente.setHotel(hotel);

        //Asignando atributos de DTO a entity
        existente.setNombreEvento(json.getNombreEvento());
        existente.setDescripcionEvento(json.getDescripcionEvento());
        existente.setFechaEvento(json.getFechaEvento());
        existente.setCapacidadEvento(json.getCapacidadEvento());
        existente.setPrecioEvento(BigDecimal.valueOf(json.getPrecioEvento()));
        //3. Guardar los cambios
        EventosEntity eventoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAEventoDTO(eventoActualizado);
    }

    public boolean eliminarEvento(String id) {
        try {
            //1. Validar existencia del evento
            EventosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el evento, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el evento con ID: " + id + " para eliminar. ", 1);
        }
    }
}
