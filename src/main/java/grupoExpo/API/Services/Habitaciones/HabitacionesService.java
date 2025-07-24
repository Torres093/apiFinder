package grupoExpo.API.Services.Habitaciones;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoEncontrado;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoRegistrado;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoRegistrada;
import grupoExpo.API.Models.DTO.ClientesDTO;
import grupoExpo.API.Models.DTO.HabitacionesDTO;
import grupoExpo.API.Repositories.Habitaciones.HabitacionesRepository;
import jakarta.validation.Valid;
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
        dto.setIdTipoHabitacion(habitacionesEntity.getIdTipoHabitacion());
        dto.setIdHotel(habitacionesEntity.getIdHotel());
        dto.setIdEstadoHabitacion(habitacionesEntity.getIdEstadoHabitacion());
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

        entity.setIdTipoHabitacion(data.getIdTipoHabitacion());
        entity.setIdHotel(data.getIdHotel());
        entity.setIdEstadoHabitacion(data.getIdEstadoHabitacion());
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
        existente.setIdTipoHabitacion(json.getIdTipoHabitacion());
        existente.setIdHotel(json.getIdHotel());
        existente.setIdEstadoHabitacion(json.getIdEstadoHabitacion());
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
            throw new EmptyResultDataAccessException("No se encontro la habitacion con ID: " + id + "para eliminar. ", 1);
        }
    }
}
