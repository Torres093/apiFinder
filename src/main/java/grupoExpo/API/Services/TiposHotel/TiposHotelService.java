package grupoExpo.API.Services.TiposHotel;

import grupoExpo.API.Models.DTO.TiposHotelDTO;
import grupoExpo.API.Entities.TiposHotel.TiposHotelEntity;
import grupoExpo.API.Repositories.TiposHotel.TiposHotelRepository;
import grupoExpo.API.Exceptions.TiposHotel.ExcepcionTipoHotelNoEncontrado;
import grupoExpo.API.Exceptions.TiposHotel.ExcepcionTipoHotelNoRegistrado;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TiposHotelService {

    @Autowired
    private TiposHotelRepository repo;

    public List<TiposHotelDTO> getAllTiposHotel(){
        List<TiposHotelEntity> tiposHotel = repo.findAll();
        return tiposHotel.stream()
                .map(this::convertirATiposHotelDTO)
                .collect(Collectors.toList());
    }

    private TiposHotelDTO convertirATiposHotelDTO(TiposHotelEntity tipoHotel) {
        TiposHotelDTO dto = new TiposHotelDTO();
        dto.setIdTipoHotel(tipoHotel.getIdTipoHotel());
        dto.setNombreTipoHotel(tipoHotel.getNombreTipoHotel());
        return dto;
    }

    public TiposHotelDTO insertarDatos(TiposHotelDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            TiposHotelEntity entity = ConvertirAEntity(data);
            TiposHotelEntity tipoHotelGuardado = repo.save(entity);
            return convertirATiposHotelDTO(tipoHotelGuardado);
        }catch (Exception e){
            log.error("Error al registrar el tipo hotel: " + e.getMessage());
            throw new ExcepcionTipoHotelNoRegistrado("Error al registrar el tipo de hotel.");
        }
    }

    private TiposHotelEntity ConvertirAEntity(TiposHotelDTO data) {
        TiposHotelEntity entity = new TiposHotelEntity();

        entity.setNombreTipoHotel(data.getNombreTipoHotel());
        return entity;
    }

    public TiposHotelDTO actualizarTipoHotel(String id, TiposHotelDTO json) {
        //1. Verificar la existencia deL tipo de hotel.
        TiposHotelEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionTipoHotelNoEncontrado("Tipo de hotel no encontrado"));
        //2. Actualizar los campos
        existente.setNombreTipoHotel(json.getNombreTipoHotel());
        //3. Guardar los cambios
        TiposHotelEntity tipoHotelActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirATiposHotelDTO(tipoHotelActualizado);
    }

    public boolean eliminarTipoHotel(String id) {
        try {
            //1. Validar existencia del tipo hotel
            TiposHotelEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el tipo de hotel, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el tipo de hotel con ID: " + id + " para eliminar. ", 1);
        }
    }
}
