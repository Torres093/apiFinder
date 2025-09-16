package grupoExpo.API.Services.Habitaciones;

import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.TiposHabitacion.TiposHabitacionEntity;
import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionEstadoHabitacionNoEncontrado;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoRegistrada;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoEncontrado;
import grupoExpo.API.Exceptions.TiposHabitacion.ExcepcionTipoHabitacionNoEncontrado;
import grupoExpo.API.Models.DTO.HabitacionesDTO;
import grupoExpo.API.Repositories.Habitaciones.HabitacionesRepository;
import grupoExpo.API.Repositories.Hotel.HotelRepository;
import grupoExpo.API.Repositories.TiposHabitacion.TiposHabitacionRepository;
import grupoExpo.API.Repositories.EstadosHabitacion.EstadosHabitacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class HabitacionesService {

    @Autowired
    private HabitacionesRepository repo;

    @Autowired
    private TiposHabitacionRepository repoTipoHabitacion;

    @Autowired
    private HotelRepository repoHotel;

    @Autowired
    private EstadosHabitacionRepository repoEstadoHabitacion;

    public Page<HabitacionesDTO> getAllHabitaciones(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<HabitacionesEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAHabitacionesDTO);
    }

    private HabitacionesDTO convertirAHabitacionesDTO(HabitacionesEntity habitacionesEntity) {
        HabitacionesDTO dto = new HabitacionesDTO();
        dto.setIdHabitacion(habitacionesEntity.getIdHabitacion());
        if (habitacionesEntity.getTipoHabitacion() != null){
            dto.setNombreTipoHabitacion(habitacionesEntity.getTipoHabitacion().getNombreTipoHabitacion());
            dto.setIdTipoHabitacion(habitacionesEntity.getTipoHabitacion().getIdTipoHabitacion());
        }else{
            dto.setNombreTipoHabitacion("Sin tipo de habitacion asignado");
            dto.setIdTipoHabitacion(null);
        }

        if (habitacionesEntity.getHotel() != null){
            dto.setNombreHotel(habitacionesEntity.getHotel().getNombreHotel());
            dto.setIdHotel(habitacionesEntity.getHotel().getIdHotel());
        }else{
            dto.setNombreHotel("Sin nombre de Hotel asignado");
            dto.setIdHotel(null);
        }

        if (habitacionesEntity.getEstadoHabitacion() != null){
            dto.setNombreEstadoHabitacion(habitacionesEntity.getEstadoHabitacion().getNombreEstadoHabitacion());
            dto.setIdEstadoHabitacion(habitacionesEntity.getEstadoHabitacion().getIdEstadoHabitacion());
        }else{
            dto.setNombreEstadoHabitacion("Sin tipo de habitacion asignado");
            dto.setIdEstadoHabitacion(null);
        }

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
        if (data.getIdTipoHabitacion() != null){
            TiposHabitacionEntity tipoHabitacionEntity = repoTipoHabitacion.findById(data.getIdTipoHabitacion())
                    .orElseThrow(()-> new ExcepcionTipoHabitacionNoEncontrado("ID de tipo de habitación no encontrado"));
            entity.setTipoHabitacion(tipoHabitacionEntity);
        }

        //Asignando Hotel a entity de Habitaciones
        if (data.getIdHotel() != null){
            HotelEntity hotel = repoHotel.findById(data.getIdHotel())
                    .orElseThrow(()-> new ExcepcionHotelNoEncontrado("ID del hotel no encontrado"));
            entity.setHotel(hotel);
        }

        //Asignando EstadoHabitacion a entity de Habitaciones
        if (data.getIdEstadoHabitacion() != null){
            EstadosHabitacionEntity estadoHabitacionEntity = repoEstadoHabitacion.findById(data.getIdEstadoHabitacion())
                    .orElseThrow(()-> new ExcepcionEstadoHabitacionNoEncontrado("ID de estado de habitación no encontrado"));
            entity.setEstadoHabitacion(estadoHabitacionEntity);
        }

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
        if (json.getIdTipoHabitacion() != null){
            TiposHabitacionEntity tipoHabitacion = repoTipoHabitacion.findById(json.getIdTipoHabitacion())
                    .orElseThrow(()-> new ExcepcionTipoHabitacionNoEncontrado("ID de tipo de habitación no encontrado"));
            existente.setTipoHabitacion(tipoHabitacion);
        }else {
            existente.setTipoHabitacion(null);
        }

        //Asignando Hotel a entity de Habitaciones
        if (json.getIdHotel() != null){
            HotelEntity hotel = repoHotel.findById(json.getIdHotel())
                    .orElseThrow(()-> new ExcepcionHotelNoEncontrado("ID del hotel no encontrado"));
            existente.setHotel(hotel);
        }

        //Asignando EstadoHabitacion a entity de Habitaciones
        if (json.getIdEstadoHabitacion() != null){
            EstadosHabitacionEntity estadoHabitacion = repoEstadoHabitacion.findById(json.getIdEstadoHabitacion())
                    .orElseThrow(()-> new ExcepcionEstadoHabitacionNoEncontrado("ID de estado de habitación no encontrado"));
            existente.setEstadoHabitacion(estadoHabitacion);
        }else {
            existente.setEstadoHabitacion(null);
        }

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
