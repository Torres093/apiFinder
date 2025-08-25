package grupoExpo.API.Services.Habitaciones;

import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.TiposHabitacion.TiposHabitacionEntity;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoRegistrada;
import grupoExpo.API.Models.DTO.HabitacionesDTO;
import grupoExpo.API.Repositories.Habitaciones.HabitacionesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HabitacionesService {

    @Autowired
    private HabitacionesRepository repo;

    public List<HabitacionesDTO> getAllHabitaciones() {
        List<HabitacionesEntity> habitaciones = repo.findAll();
        return habitaciones.stream()
                .map(this::convertirAHabitacionesDTO)
                .collect(Collectors.toList());
    }

    private HabitacionesDTO convertirAHabitacionesDTO(HabitacionesEntity habitacionesEntity) {
        HabitacionesDTO dto = new HabitacionesDTO();
        dto.setIdHabitacion(habitacionesEntity.getIdHabitacion());
        dto.setIdTipoHabitacion(habitacionesEntity.getTipoHabitacion().getIdTipoHabitacion());
        dto.setIdHotel(habitacionesEntity.getHotel().getIdHotel());
        dto.setIdEstadoHabitacion(habitacionesEntity.getEstadoHabitacion().getIdEstadoHabitacion());
        dto.setNumeroHabitacion(habitacionesEntity.getNumeroHabitacion());
        dto.setDescripcionHabitacion(habitacionesEntity.getDescripcionHabitacion());
        dto.setPrecioHabitacion(habitacionesEntity.getPrecioHabitacion().doubleValue());
        dto.setCapacidadHabitacion(habitacionesEntity.getCapacidadHabitacion());
        return dto;
    }

    public HabitacionesDTO insertarDatos(HabitacionesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            HabitacionesEntity entity = ConvertirAEntity(data);
            HabitacionesEntity HabitacionGuardada = repo.save(entity);
            return convertirAHabitacionesDTO(HabitacionGuardada);
        }catch (Exception e){
            log.error("Error al registrar la habitacion: " + e.getMessage());
            throw new ExcepcionHabitacionNoRegistrada("Error al registrar la habitacion.");
        }
    }

    private HabitacionesEntity ConvertirAEntity(HabitacionesDTO data) {
        HabitacionesEntity entity = new HabitacionesEntity();

        //Asignando TipoHabitacion a entity de Habitaciones
        TiposHabitacionEntity tipoHabitacion = new TiposHabitacionEntity();
        tipoHabitacion.setIdTipoHabitacion(data.getIdTipoHabitacion());
        entity.setTipoHabitacion(tipoHabitacion);

        //Asignando Hotel a entity de Habitaciones
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(data.getIdHotel());
        entity.setHotel(hotel);

        //Asignando EstadoHabitacion a entity de Habitaciones
        EstadosHabitacionEntity estadoHabitacion = new EstadosHabitacionEntity();
        estadoHabitacion.setIdEstadoHabitacion(data.getIdEstadoHabitacion());
        entity.setEstadoHabitacion(estadoHabitacion);

        //Asignando atributos de DTO a entity
        entity.setNumeroHabitacion(data.getNumeroHabitacion());
        entity.setDescripcionHabitacion(data.getDescripcionHabitacion());
        entity.setPrecioHabitacion(BigDecimal.valueOf(data.getPrecioHabitacion()));
        entity.setCapacidadHabitacion(data.getCapacidadHabitacion());
        return entity;
    }

    public HabitacionesDTO actualizarHabitacion(String id, HabitacionesDTO json) {
        //1. Verificar la existencia de la habitacion.
        HabitacionesEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionHabitacionNoEncontrada("Habitacion no encontrada"));
        //2. Actualizar los campos

        //Asignando TipoHabitacion a entity de Habitaciones
        TiposHabitacionEntity tipoHabitacion = new TiposHabitacionEntity();
        tipoHabitacion.setIdTipoHabitacion(json.getIdTipoHabitacion());
        existente.setTipoHabitacion(tipoHabitacion);

        //Asignando Hotel a entity de Habitaciones
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(json.getIdHotel());
        existente.setHotel(hotel);

        //Asignando EstadoHabitacion a entity de Habitaciones
        EstadosHabitacionEntity estadoHabitacion = new EstadosHabitacionEntity();
        estadoHabitacion.setIdEstadoHabitacion(json.getIdEstadoHabitacion());
        existente.setEstadoHabitacion(estadoHabitacion);

        //Asignando atributos de DTO a entity
        existente.setNumeroHabitacion(json.getNumeroHabitacion());
        existente.setDescripcionHabitacion(json.getDescripcionHabitacion());
        existente.setPrecioHabitacion(BigDecimal.valueOf(json.getPrecioHabitacion()));
        existente.setCapacidadHabitacion(json.getCapacidadHabitacion());
        //3. Guardar los cambios
        HabitacionesEntity habitacionActualizada = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAHabitacionesDTO(habitacionActualizada);
    }

    public boolean eliminarHabitacion(String id) {
        try {
            //1. Validar existencia de la habitacion
            HabitacionesEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar la habitacion, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro la habitacion con ID: " + id + " para eliminar. ", 1);
        }
    }
}
