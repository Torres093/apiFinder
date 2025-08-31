package grupoExpo.API.Services.Platos;

import grupoExpo.API.Entities.Eventos.EventosEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Platos.PlatosEntity;
import grupoExpo.API.Exceptions.Eventos.ExcepcionEventoNoEncontrado;
import grupoExpo.API.Exceptions.Eventos.ExcepcionEventoNoRegistrado;
import grupoExpo.API.Exceptions.Platos.ExcepcionPlatoNoEncontrado;
import grupoExpo.API.Exceptions.Platos.ExcepcionPlatoNoRegistrado;
import grupoExpo.API.Models.DTO.PlatosDTO;
import grupoExpo.API.Repositories.Platos.PlatosRepository;
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
public class PlatosService {

    @Autowired
    private PlatosRepository repo;

    public List<PlatosDTO> getAllPlatos() {
        List<PlatosEntity> platos = repo.findAll();
        return platos.stream()
                .map(this::convertirAPlatoDTO)
                .collect(Collectors.toList());
    }

    private PlatosDTO convertirAPlatoDTO(PlatosEntity plato) {
        PlatosDTO dto = new PlatosDTO();
        dto.setIdPlato(plato.getIdPlato());
        dto.setIdHotel(plato.getHotel().getIdHotel());
        dto.setNombrePlato(plato.getNombrePlato());
        dto.setDescripcionPlato(plato.getDescripcionPlato());
        dto.setPrecioPlato(plato.getPrecioPlato().doubleValue());
        return dto;
    }

    public PlatosDTO insertarDatos(PlatosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            PlatosEntity entity = ConvertirAEntity(data);
            PlatosEntity platoGuardado = repo.save(entity);
            return convertirAPlatoDTO(platoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el plato: " + e.getMessage());
            throw new ExcepcionPlatoNoRegistrado("Error al registrar el plato.");
        }
    }

    private PlatosEntity ConvertirAEntity(PlatosDTO data) {
        PlatosEntity entity = new PlatosEntity();

        //Asignando Hotel a entity de Platos
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(data.getIdHotel());
        entity.setHotel(hotel);

        //Asignando atributos de DTO a entity
        entity.setNombrePlato(data.getNombrePlato());
        entity.setDescripcionPlato(data.getDescripcionPlato());
        entity.setPrecioPlato(BigDecimal.valueOf(data.getPrecioPlato()));
        return entity;
    }

    public PlatosDTO actualizarPlato(String id, PlatosDTO json) {
        //1. Verificar la existencia del plato.
        PlatosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionPlatoNoEncontrado("Plato no encontrado"));
        //2. Actualizar los campos

        //Asignando Hotel a entity de Platos
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(json.getIdHotel());
        existente.setHotel(hotel);

        //Asignando atributos de DTO a entity
        existente.setNombrePlato(json.getNombrePlato());
        existente.setDescripcionPlato(json.getDescripcionPlato());
        existente.setPrecioPlato(BigDecimal.valueOf(json.getPrecioPlato()));
        //3. Guardar los cambios
        PlatosEntity platoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAPlatoDTO(platoActualizado);
    }

    public boolean eliminarPlato(String id) {
        try {
            //1. Validar existencia del plato
            PlatosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el plato, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el plato con ID: " + id + " para eliminar. ", 1);
        }
    }
}
