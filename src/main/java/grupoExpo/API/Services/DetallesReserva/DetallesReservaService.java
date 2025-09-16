package grupoExpo.API.Services.DetallesReserva;

import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Exceptions.DetallesReserva.ExcepcionDetalleReservaNoEncontrado;
import grupoExpo.API.Exceptions.DetallesReserva.ExcepcionDetalleReservaNoRegistrado;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Models.DTO.DetallesReservaDTO;
import grupoExpo.API.Repositories.DetallesReserva.DetallesReservaRepository;
import grupoExpo.API.Repositories.Habitaciones.HabitacionesRepository;
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
public class DetallesReservaService {

    @Autowired
    private DetallesReservaRepository repo;

    @Autowired
    private HabitacionesRepository repoHabitaciones;

    public Page<DetallesReservaDTO> getAllDetallesReserva(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<DetallesReservaEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirADetalleReservaDTO);
    }

    private DetallesReservaDTO convertirADetalleReservaDTO(DetallesReservaEntity detalleReserva) {
        DetallesReservaDTO dto = new DetallesReservaDTO();
        dto.setIdDetalle(detalleReserva.getIdDetalle());
        dto.setIdReserva(detalleReserva.getReserva().getIdReserva());
        if (detalleReserva.getHabitacion() != null){
            dto.setNumeroHabitacion(detalleReserva.getHabitacion().getNumeroHabitacion());
            dto.setIdHabitacion(detalleReserva.getHabitacion().getIdHabitacion());
        }else{
            dto.setNumeroHabitacion(-1);
            dto.setIdHabitacion(null);
        }
        dto.setNumeroHuespedesDetalle(detalleReserva.getNumeroHuespedesDetalle());
        dto.setFechaYHoraDeLlegadaDetalle(detalleReserva.getFechaYHoraDeLlegadaDetalle());
        dto.setFechaYHoraDeSalidaDetalle(detalleReserva.getFechaYHoraDeSalidaDetalle());
        dto.setPrecioDetalle(detalleReserva.getPrecioDetalle().doubleValue());
        dto.setDescuentoDetalle(detalleReserva.getDescuentoDetalle());
        return dto;
    }

    public DetallesReservaDTO insertarDatos(DetallesReservaDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            DetallesReservaEntity entity = ConvertirAEntity(data);
            DetallesReservaEntity detalleReservaGuardado = repo.save(entity);
            return convertirADetalleReservaDTO(detalleReservaGuardado);
        }catch (Exception e){
            log.error("Error al registrar el detalleReserva: " + e.getMessage());
            throw new ExcepcionDetalleReservaNoRegistrado("Error al registrar el detalleReserva.");
        }
    }

    private DetallesReservaEntity ConvertirAEntity(DetallesReservaDTO data) {
        DetallesReservaEntity entity = new DetallesReservaEntity();

        //Asignando Reserva a entity de DetallesReserva
        ReservasEntity reserva = new ReservasEntity();
        reserva.setIdReserva(data.getIdReserva());
        entity.setReserva(reserva);

        //Asignando Habitacion a entity de DetallesReserva
        if (data.getIdHabitacion() != null){
            HabitacionesEntity habitacion = repoHabitaciones.findById(data.getIdHabitacion())
                    .orElseThrow(()-> new ExcepcionHabitacionNoEncontrada("ID de la habitacion no encontrada"));
            entity.setHabitacion(habitacion);
        }

        //Asignando atributos de DTO a entity
        entity.setNumeroHuespedesDetalle(data.getNumeroHuespedesDetalle());
        entity.setFechaYHoraDeLlegadaDetalle(data.getFechaYHoraDeLlegadaDetalle());
        entity.setFechaYHoraDeSalidaDetalle(data.getFechaYHoraDeSalidaDetalle());
        entity.setPrecioDetalle(BigDecimal.valueOf(data.getPrecioDetalle()));
        entity.setDescuentoDetalle(data.getDescuentoDetalle());
        return entity;
    }

    public DetallesReservaDTO actualizarDetalleReserva(String id, DetallesReservaDTO json) {
        //1. Verificar la existencia del detalleReserva.
        DetallesReservaEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionDetalleReservaNoEncontrado("DetalleReserva no encontrado"));
        //2. Actualizar los campos

        //Asignando Reserva a entity de DetallesReserva
        ReservasEntity reserva = new ReservasEntity();
        reserva.setIdReserva(json.getIdReserva());
        existente.setReserva(reserva);

        //Asignando Habitacion a entity de DetallesReserva
        if (json.getIdHabitacion() != null){
            HabitacionesEntity habitacion = repoHabitaciones.findById(json.getIdHabitacion())
                    .orElseThrow(()-> new ExcepcionHabitacionNoEncontrada("ID de la habitacion no encontrada"));
            existente.setHabitacion(habitacion);
        }

        //Asignando atributos de DTO a entity
        existente.setNumeroHuespedesDetalle(json.getNumeroHuespedesDetalle());
        existente.setFechaYHoraDeLlegadaDetalle(json.getFechaYHoraDeLlegadaDetalle());
        existente.setFechaYHoraDeSalidaDetalle(json.getFechaYHoraDeSalidaDetalle());
        existente.setPrecioDetalle(BigDecimal.valueOf(json.getPrecioDetalle()));
        existente.setDescuentoDetalle(json.getDescuentoDetalle());

        //3. Guardar los cambios
        DetallesReservaEntity detalleReservaActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirADetalleReservaDTO(detalleReservaActualizado);
    }

    public boolean eliminarDetalleReserva(String id) {
        try {
            //1. Validar existencia del detalleReserva
            DetallesReservaEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el detalleReserva, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el detalleReserva con ID: " + id + " para eliminar. ", 1);
        }
    }
}
