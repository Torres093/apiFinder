package grupoExpo.API.Services.Hotel;

import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.TiposHotel.TiposHotelEntity;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoEncontrado;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoRegistrado;
import grupoExpo.API.Exceptions.TiposHotel.ExcepcionTipoHotelNoEncontrado;
import grupoExpo.API.Models.DTO.HotelDTO;
import grupoExpo.API.Repositories.Hotel.HotelRepository;
import grupoExpo.API.Repositories.TiposHotel.TiposHotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HotelService {

    @Autowired
    private HotelRepository repo;

    @Autowired
    private TiposHotelRepository repoTiposHotel;

    public Page<HotelDTO> getAllHotel(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<HotelEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAHotelDTO);
    }

    private HotelDTO convertirAHotelDTO(HotelEntity hotel) {
        HotelDTO dto = new HotelDTO();

        dto.setIdHotel(hotel.getIdHotel());
        if (hotel.getTipoHotel() != null){
            dto.setNombreTipoHotel(hotel.getTipoHotel().getNombreTipoHotel());
            dto.setIdTipoHotel(hotel.getTipoHotel().getIdTipoHotel());
        }else{
            dto.setNombreTipoHotel("Sin nombre de tipo de hotel asignado");
            dto.setIdTipoHotel(null);
        }
        dto.setNombreHotel(hotel.getNombreHotel());
        dto.setUbicacionHotel(hotel.getUbicacionHotel());
        dto.setCorreoHotel(hotel.getCorreoHotel());
        dto.setFechaCreacionHotel(hotel.getFechaCreacionHotel());
        dto.setTelefonoHotel(hotel.getTelefonoHotel());
        dto.setImagenHotel(hotel.getImagenHotel());
        dto.setNumeroHabitacionesHotel(hotel.getNumeroHabitacionesHotel());
        return dto;
    }

    public HotelDTO insertarDatos(HotelDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            HotelEntity entity = ConvertirAEntity(data);
            HotelEntity hotelGuardado = repo.save(entity);
            return convertirAHotelDTO(hotelGuardado);
        }catch (Exception e){
            log.error("Error al registrar el hotel: " + e.getMessage());
            throw new ExcepcionHotelNoRegistrado("Error al registrar el hotel.");
        }
    }

    private HotelEntity ConvertirAEntity(HotelDTO data) {
        HotelEntity entity = new HotelEntity();

        //Asignando TipoHotel a entity de Hotel
        if (data.getIdTipoHotel() != null){
            TiposHotelEntity tiposHotel = repoTiposHotel.findById(data.getIdTipoHotel())
                    .orElseThrow(()-> new ExcepcionTipoHotelNoEncontrado("ID de tipo de hotel no encontrado"));
            entity.setTipoHotel(tiposHotel);
        }

        //Asignando atributos de DTO a entity
        entity.setNombreHotel(data.getNombreHotel());
        entity.setUbicacionHotel(data.getUbicacionHotel());
        entity.setCorreoHotel(data.getCorreoHotel());
        entity.setFechaCreacionHotel(data.getFechaCreacionHotel());
        entity.setTelefonoHotel(data.getTelefonoHotel());
        entity.setImagenHotel(data.getImagenHotel());
        entity.setNumeroHabitacionesHotel(data.getNumeroHabitacionesHotel());
        return entity;
    }

    public HotelDTO actualizarHotel(String id, HotelDTO json) {
        //1. Verificar la existencia del Hotel.
        HotelEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionHotelNoEncontrado("Hotel no encontrado"));
        //2. Actualizar los campos

        //Asignando TipoHotel a entity de Hotel
        if (json.getIdTipoHotel() != null){
            TiposHotelEntity tiposHotel = repoTiposHotel.findById(json.getIdTipoHotel())
                    .orElseThrow(()-> new ExcepcionTipoHotelNoEncontrado("ID de tipo de hotel no encontrado"));
            existente.setTipoHotel(tiposHotel);
        }

        //Asignando atributos de DTO a entity
        existente.setNombreHotel(json.getNombreHotel());
        existente.setUbicacionHotel(json.getUbicacionHotel());
        existente.setCorreoHotel(json.getCorreoHotel());
        existente.setFechaCreacionHotel(json.getFechaCreacionHotel());
        existente.setTelefonoHotel(json.getTelefonoHotel());
        existente.setImagenHotel(json.getImagenHotel());
        existente.setNumeroHabitacionesHotel(json.getNumeroHabitacionesHotel());
        //3. Guardar los cambios
        HotelEntity hotelActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAHotelDTO(hotelActualizado);
    }

    public boolean eliminarHotel(String id) {
        try {
            //1. Validar existencia del hotel
            HotelEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el hotel, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el hotel con ID: " + id + " para eliminar. ", 1);
        }
    }
}
