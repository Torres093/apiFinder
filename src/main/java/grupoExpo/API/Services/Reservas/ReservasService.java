package grupoExpo.API.Services.Reservas;


import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoRegistrada;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoEncontrada;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoRegistrada;
import grupoExpo.API.Models.DTO.ReservasDTO;
import grupoExpo.API.Repositories.Reservas.ReservasRepository;
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
public class ReservasService {

    @Autowired
    private ReservasRepository repo;

    public List<ReservasDTO> getAllReservas(){
        List <ReservasEntity> reservas = repo.findAll();
        return reservas.stream()
                .map(this :: convertirAReservasDTO)
                .collect(Collectors.toList());
    }

    private ReservasDTO convertirAReservasDTO(ReservasEntity reservas) {
        ReservasDTO dto = new ReservasDTO();
        dto.setIdReserva(reservas.getIdReserva());
        dto.setIdCliente(reservas.getIdCliente());
        dto.setIdEstadoReserva(reservas.getIdEstadoReserva());
        dto.setIdMetodoPago(reservas.getIdMetodoPago());
        dto.setFechaReserva(reservas.getFechaReserva());
        dto.setPrecioTotalReserva(reservas.getPrecioTotalReserva().doubleValue());
        return dto;
    }

    public ReservasDTO insertarDatos(ReservasDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            ReservasEntity entity = ConvertirAEntity(data);
            ReservasEntity ReservaGuardada = repo.save(entity);
            return convertirAReservasDTO(ReservaGuardada);
        }catch (Exception e){
            log.error("Error al registrar la reserva: " + e.getMessage());
            throw new ExcepcionReservaNoRegistrada("Error al registrar la reserva.");
        }
    }

    private ReservasEntity ConvertirAEntity(ReservasDTO data) {
        ReservasEntity entity = new ReservasEntity();

        entity.setIdCliente(data.getIdCliente());
        entity.setIdEstadoReserva(data.getIdEstadoReserva());
        entity.setIdMetodoPago(data.getIdMetodoPago());
        entity.setFechaReserva(data.getFechaReserva());
        entity.setPrecioTotalReserva(BigDecimal.valueOf(data.getPrecioTotalReserva()));
        return entity;
    }

    public ReservasDTO actualizarReserva(String id, ReservasDTO json) {
        //1. Verificar la existencia de la reserva.
        ReservasEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionReservaNoEncontrada("Reserva no encontrada"));
        //2. Actualizar los campos
        existente.setIdCliente(json.getIdCliente());
        existente.setIdEstadoReserva(json.getIdEstadoReserva());
        existente.setIdMetodoPago(json.getIdMetodoPago());
        existente.setFechaReserva(json.getFechaReserva());
        existente.setPrecioTotalReserva(BigDecimal.valueOf(json.getPrecioTotalReserva()));
        //3. Guardar los cambios
        ReservasEntity reservaActualizada = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAReservasDTO(reservaActualizada);
    }

    public boolean eliminarReserva(String id) {
        try {
            //1. Validar existencia de la reserva
            ReservasEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar la reserva, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro la reserva con ID: " + id + "para eliminar. ", 1);
        }
    }
}
