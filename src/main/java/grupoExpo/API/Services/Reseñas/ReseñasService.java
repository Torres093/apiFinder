package grupoExpo.API.Services.Reseñas;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Entities.EstadosReserva.EstadosReservaEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Entities.Reseñas.ReseñasEntity;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoEncontrada;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoRegistrada;
import grupoExpo.API.Exceptions.Reseñas.ExcepcionReseñaNoEncontrada;
import grupoExpo.API.Exceptions.Reseñas.ExcepcionReseñaNoRegistrada;
import grupoExpo.API.Models.DTO.ReseñasDTO;
import grupoExpo.API.Repositories.Reseñas.ReseñasRepository;
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
public class ReseñasService {

    @Autowired
    private ReseñasRepository repo;

    public List<ReseñasDTO> getAllReseñas() {
        List<ReseñasEntity> reseñas = repo.findAll();
        return reseñas.stream()
                .map(this::convertirAReseñasDTO)
                .collect(Collectors.toList());
    }

    private ReseñasDTO convertirAReseñasDTO(ReseñasEntity reseñas) {
        ReseñasDTO dto = new ReseñasDTO();
        dto.setIdReseña(reseñas.getIdReseña());
        dto.setIdCliente(reseñas.getCliente().getIdCliente());
        dto.setIdHotel(reseñas.getHotel().getIdHotel());
        dto.setComentarioReseña(reseñas.getComentarioReseña());
        dto.setCalificacionReseña(reseñas.getCalificacionReseña());
        return dto;
    }

    public ReseñasDTO insertarDatos(ReseñasDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            ReseñasEntity entity = ConvertirAEntity(data);
            ReseñasEntity ReseñaGuardada = repo.save(entity);
            return convertirAReseñasDTO(ReseñaGuardada);
        }catch (Exception e){
            log.error("Error al registrar la reserva: " + e.getMessage());
            throw new ExcepcionReseñaNoRegistrada("Error al registrar la reseña.");
        }
    }

    private ReseñasEntity ConvertirAEntity(ReseñasDTO data) {
        ReseñasEntity entity = new ReseñasEntity();

        //Asignando Cliente a entity de Reseñas
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(data.getIdCliente());
        entity.setCliente(cliente);

        //Asignando Hotel a entity de Reseñas
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(data.getIdHotel());
        entity.setHotel(hotel);

        //Asignando atributos de DTO a entity
        entity.setComentarioReseña(data.getComentarioReseña());
        entity.setCalificacionReseña(data.getCalificacionReseña());
        return entity;
    }

    public ReseñasDTO actualizarReseña(String id, ReseñasDTO json) {
        //1. Verificar la existencia de la reseña.
        ReseñasEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionReseñaNoEncontrada("Reseña no encontrada"));
        //2. Actualizar los campos

        //Asignando Cliente a entity de Reseñas
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(json.getIdCliente());
        existente.setCliente(cliente);

        //Asignando Hotel a entity de Reseñas
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(json.getIdHotel());
        existente.setHotel(hotel);

        //Asignando atributos de DTO a entity
        existente.setComentarioReseña(json.getComentarioReseña());
        existente.setCalificacionReseña(json.getCalificacionReseña());

        //3. Guardar los cambios
        ReseñasEntity reseñaActualizada = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAReseñasDTO(reseñaActualizada);
    }

    public boolean eliminarReseña(String id) {
        try {
            //1. Validar existencia de la reseña
            ReseñasEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar la reseña, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro la reseña con ID: " + id + " para eliminar. ", 1);
        }
    }
}
