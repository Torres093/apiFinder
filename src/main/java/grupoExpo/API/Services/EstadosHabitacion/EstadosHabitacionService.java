package grupoExpo.API.Services.EstadosHabitacion;

import grupoExpo.API.Models.DTO.EstadosHabitacionDTO;
import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import grupoExpo.API.Repositories.EstadosHabitacion.EstadosHabitacionRepository;
import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionEstadoHabitacionNoEncontrado;
import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionEstadoHabitacionNoRegistrado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EstadosHabitacionService {

    @Autowired
    private EstadosHabitacionRepository repo;

    public Page<EstadosHabitacionDTO> getAllEstadosHabitacion(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<EstadosHabitacionEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAEstadosHabitacionDTO);
    }

    private EstadosHabitacionDTO convertirAEstadosHabitacionDTO(EstadosHabitacionEntity estadoHabitacion) {
        EstadosHabitacionDTO dto = new EstadosHabitacionDTO();
        dto.setIdEstadoHabitacion(estadoHabitacion.getIdEstadoHabitacion());
        dto.setNombreEstadoHabitacion(estadoHabitacion.getNombreEstadoHabitacion());
        return dto;
    }

    public EstadosHabitacionDTO insertarDatos(EstadosHabitacionDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            EstadosHabitacionEntity entity = ConvertirAEntity(data);
            EstadosHabitacionEntity estadoHabitacionGuardado = repo.save(entity);
            return convertirAEstadosHabitacionDTO(estadoHabitacionGuardado);
        }catch (Exception e){
            log.error("Error al registrar el estado de habitacion: " + e.getMessage());
            throw new ExcepcionEstadoHabitacionNoRegistrado("Error al registrar el estado de habitacion.");
        }
    }

    private EstadosHabitacionEntity ConvertirAEntity(EstadosHabitacionDTO data) {
        EstadosHabitacionEntity entity = new EstadosHabitacionEntity();

        entity.setNombreEstadoHabitacion(data.getNombreEstadoHabitacion());
        return entity;
    }

    public EstadosHabitacionDTO actualizarEstadoHabitacion(String id, EstadosHabitacionDTO json) {
        //1. Verificar la existencia del estado de habitacion.
        EstadosHabitacionEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionEstadoHabitacionNoEncontrado("Estado de habitacion no encontrado"));
        //2. Actualizar los campos
        existente.setNombreEstadoHabitacion(json.getNombreEstadoHabitacion());
        //3. Guardar los cambios
        EstadosHabitacionEntity estadoHabitacionActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAEstadosHabitacionDTO(estadoHabitacionActualizado);
    }

    public boolean eliminarEstadoHabitacion(String id) {
        try {
            //1. Validar existencia del estado de habitacion
            EstadosHabitacionEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el estado de habitacion, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el estado de habitacion con ID: " + id + " para eliminar. ", 1);
        }
    }
}
