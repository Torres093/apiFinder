package grupoExpo.API.Services.DetallesReservaServicio;

import grupoExpo.API.Entities.Cargos.CargosEntity;
import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.DetallesReservaServicio.DetallesReservaServicioEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Exceptions.DetallesReserva.ExcepcionDetalleReservaNoEncontrado;
import grupoExpo.API.Exceptions.DetallesReserva.ExcepcionDetalleReservaNoRegistrado;
import grupoExpo.API.Exceptions.DetallesReservaServicio.ExcepcionDetalleReservaServicioNoEncontrado;
import grupoExpo.API.Exceptions.DetallesReservaServicio.ExcepcionDetalleReservaServicioNoRegistrado;
import grupoExpo.API.Models.DTO.DetallesReservaServicioDTO;
import grupoExpo.API.Repositories.DetallesReservaServicio.DetallesReservaServicioRepository;
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
public class DetallesReservaServicioService {

    @Autowired
    private DetallesReservaServicioRepository repo;

    public List<DetallesReservaServicioDTO> getAllDetallesReservaServicio() {
        List<DetallesReservaServicioEntity> detallesReservaServicio = repo.findAll();
        return detallesReservaServicio.stream()
                .map(this::convertirADetalleReservaServicioDTO)
                .collect(Collectors.toList());
    }

    private DetallesReservaServicioDTO convertirADetalleReservaServicioDTO(DetallesReservaServicioEntity detalleReservaServicio) {
        DetallesReservaServicioDTO dto = new DetallesReservaServicioDTO();
        dto.setIdDetalleReservaServicio(detalleReservaServicio.getIdDetalleReservaServicio());
        dto.setIdReserva(detalleReservaServicio.getReserva().getIdReserva());
        dto.setIdServicio(detalleReservaServicio.getServicio().getIdServicio());
        return dto;
    }

    public DetallesReservaServicioDTO insertarDatos(DetallesReservaServicioDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            DetallesReservaServicioEntity entity = ConvertirAEntity(data);
            DetallesReservaServicioEntity detalleReservaServicioGuardado = repo.save(entity);
            return convertirADetalleReservaServicioDTO(detalleReservaServicioGuardado);
        }catch (Exception e){
            log.error("Error al registrar el detalleReservaServicio: " + e.getMessage());
            throw new ExcepcionDetalleReservaServicioNoRegistrado("Error al registrar el detalleReservaServicio.");
        }

    }

    private DetallesReservaServicioEntity ConvertirAEntity(DetallesReservaServicioDTO data) {
        DetallesReservaServicioEntity entity = new DetallesReservaServicioEntity();

        //Asignando Reserva a entity de DetallesReservaServicio
        ReservasEntity reserva = new ReservasEntity();
        reserva.setIdReserva(data.getIdReserva());
        entity.setReserva(reserva);

        //Asignando Servicio a entity de DetallesReservaServicio
        ServiciosEntity servicio = new ServiciosEntity();
        servicio.setIdServicio(data.getIdServicio());
        entity.setServicio(servicio);
        return entity;
    }

    public DetallesReservaServicioDTO actualizarDetalleReservaServicio(String id, DetallesReservaServicioDTO json) {
        //1. Verificar la existencia del detalleReservaServicio.
        DetallesReservaServicioEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionDetalleReservaServicioNoEncontrado("DetalleReservaServicio no encontrado"));
        //2. Actualizar los campos

        //Asignando Reserva a entity de DetallesReservaServicio
        ReservasEntity reserva = new ReservasEntity();
        reserva.setIdReserva(json.getIdReserva());
        existente.setReserva(reserva);

        //Asignando Servicio a entity de DetallesReservaServicio
        ServiciosEntity servicio = new ServiciosEntity();
        servicio.setIdServicio(json.getIdServicio());
        existente.setServicio(servicio);

        //3. Guardar los cambios
        DetallesReservaServicioEntity detalleReservaActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirADetalleReservaServicioDTO(detalleReservaActualizado);
    }

    public boolean eliminarDetalleReservaServicio(String id) {
        try {
            //1. Validar existencia del detalleReservaServicio
            DetallesReservaServicioEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el detalleReservaServicio, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el detalleReservaServicio con ID: " + id + " para eliminar. ", 1);
        }
    }
}
