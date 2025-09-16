package grupoExpo.API.Services.Platos;

import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Platos.PlatosEntity;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoEncontrado;
import grupoExpo.API.Exceptions.Platos.ExcepcionPlatoNoEncontrado;
import grupoExpo.API.Exceptions.Platos.ExcepcionPlatoNoRegistrado;
import grupoExpo.API.Models.DTO.PlatosDTO;
import grupoExpo.API.Repositories.Hotel.HotelRepository;
import grupoExpo.API.Repositories.Platos.PlatosRepository;
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
public class PlatosService {

    @Autowired
    private PlatosRepository repo;

    @Autowired
    private HotelRepository repoHotel;

    public Page<PlatosDTO> getAllPlatos(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<PlatosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAPlatoDTO);
    }

    private PlatosDTO convertirAPlatoDTO(PlatosEntity plato) {
        PlatosDTO dto = new PlatosDTO();
        dto.setIdPlato(plato.getIdPlato());
        if (plato.getHotel() != null){
            dto.setNombreHotel(plato.getHotel().getNombreHotel());
            dto.setIdHotel(plato.getHotel().getIdHotel());
        }else{
            dto.setNombreHotel("Sin nombre de hotel asignado");
            dto.setIdHotel(null);
        }
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
        if (data.getIdHotel() != null){
            HotelEntity hotel = repoHotel.findById(data.getIdHotel())
                    .orElseThrow(()-> new ExcepcionHotelNoEncontrado("ID del hotel no encontrado"));
            entity.setHotel(hotel);
        }

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
        if (json.getIdHotel() != null){
            HotelEntity hotel = repoHotel.findById(json.getIdHotel())
                    .orElseThrow(()-> new ExcepcionHotelNoEncontrado("ID del hotel no encontrado"));
            existente.setHotel(hotel);
        }

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
