package grupoExpo.API.Services.EstadosReserva;

import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import grupoExpo.API.Entities.EstadosReserva.EstadosReservaEntity;
import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionEstadoHabitacionNoEncontrado;
import grupoExpo.API.Exceptions.EstadosReserva.ExcepcionEstadoReservaNoEncontrado;
import grupoExpo.API.Exceptions.EstadosReserva.ExcepcionEstadoReservaNoRegistrado;
import grupoExpo.API.Models.DTO.EstadosReservaDTO;
import grupoExpo.API.Repositories.EstadosReserva.EstadosReservaRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EstadosReservaService {

    @Autowired
    private EstadosReservaRepository repo;

    public List<EstadosReservaDTO> getAllEstadosReserva() {
        List<EstadosReservaEntity> estadosReserva = repo.findAll();
        return estadosReserva.stream()
                .map(this::convertirAEstadoReservaDTO)
                .collect(Collectors.toList());
    }

    private EstadosReservaDTO convertirAEstadoReservaDTO(EstadosReservaEntity estadoReserva) {
        EstadosReservaDTO dto = new EstadosReservaDTO();
        dto.setIdEstadoReserva(estadoReserva.getIdEstadoReserva());
        dto.setNombreEstadoReserva(estadoReserva.getNombreEstadoReserva());
        return dto;
    }

    public EstadosReservaDTO insertarDatos(EstadosReservaDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            EstadosReservaEntity entity = ConvertirAEntity(data);
            EstadosReservaEntity estadoReservaGuardado = repo.save(entity);
            return convertirAEstadoReservaDTO(estadoReservaGuardado);
        }catch (Exception e){
            log.error("Error al registrar el estadoReserva: " + e.getMessage());
            throw new ExcepcionEstadoReservaNoRegistrado("Error al registrar el estadoReserva.");
        }
    }

    private EstadosReservaEntity ConvertirAEntity(EstadosReservaDTO data) {
        EstadosReservaEntity entity = new EstadosReservaEntity();

        entity.setNombreEstadoReserva(data.getNombreEstadoReserva());
        return entity;
    }

    public EstadosReservaDTO actualizarEstadoReserva(String id, EstadosReservaDTO json) {
        //1. Verificar la existencia del estadoReserva.
        EstadosReservaEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionEstadoReservaNoEncontrado("EstadoReserva no encontrado"));
        //2. Actualizar los campos
        existente.setNombreEstadoReserva(json.getNombreEstadoReserva());
        //3. Guardar los cambios
        EstadosReservaEntity estadoReservaActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAEstadoReservaDTO(estadoReservaActualizado);
    }

    public boolean eliminarEstadoReserva(String id) {
        try {
            //1. Validar existencia del estadoReserva
            EstadosReservaEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el estadoReserva, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el estadoReserva con ID: " + id + " para eliminar. ", 1);
        }
    }
}
