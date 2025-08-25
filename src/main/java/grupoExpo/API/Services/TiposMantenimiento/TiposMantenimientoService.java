package grupoExpo.API.Services.TiposMantenimiento;

import grupoExpo.API.Entities.TiposHotel.TiposHotelEntity;
import grupoExpo.API.Entities.TiposMantenimiento.TiposMantenimientoEntity;
import grupoExpo.API.Exceptions.TiposHotel.ExcepcionTipoHotelNoEncontrado;
import grupoExpo.API.Exceptions.TiposMantenimiento.ExcepcionTipoMantenimientoNoEncontrado;
import grupoExpo.API.Exceptions.TiposMantenimiento.ExcepcionTipoMantenimientoNoRegistrado;
import grupoExpo.API.Models.DTO.TiposMantenimientoDTO;
import grupoExpo.API.Repositories.TiposMantenimiento.TiposMantenimientoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TiposMantenimientoService {

    @Autowired
    private TiposMantenimientoRepository repo;

    public List<TiposMantenimientoDTO> getAllTiposMantenimiento() {
        List<TiposMantenimientoEntity> tiposMantenimiento = repo.findAll();
        return tiposMantenimiento.stream()
                .map(this::convertirATipoMantenimientoDTO)
                .collect(Collectors.toList());
    }

    private TiposMantenimientoDTO convertirATipoMantenimientoDTO(TiposMantenimientoEntity tipoMantenimiento) {
        TiposMantenimientoDTO dto = new TiposMantenimientoDTO();
        dto.setIdTipoMantenimiento(tipoMantenimiento.getIdTipoMantenimiento());
        dto.setNombreTipoMantenimiento(tipoMantenimiento.getNombreTipoMantenimiento());
        return dto;
    }

    public TiposMantenimientoDTO insertarDatos(TiposMantenimientoDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            TiposMantenimientoEntity entity = ConvertirAEntity(data);
            TiposMantenimientoEntity tipoMantenimientoGuardado = repo.save(entity);
            return convertirATipoMantenimientoDTO(tipoMantenimientoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el tipoMantenimiento: " + e.getMessage());
            throw new ExcepcionTipoMantenimientoNoRegistrado("Error al registrar el tipoMantenimiento.");
        }
    }

    private TiposMantenimientoEntity ConvertirAEntity(TiposMantenimientoDTO data) {
        TiposMantenimientoEntity entity = new TiposMantenimientoEntity();

        entity.setNombreTipoMantenimiento(data.getNombreTipoMantenimiento());
        return entity;
    }

    public TiposMantenimientoDTO actualizarTipoMantenimiento(String id, TiposMantenimientoDTO json) {
        //1. Verificar la existencia del tipoMantenimiento.
        TiposMantenimientoEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionTipoMantenimientoNoEncontrado("TipoMantenimiento no encontrado"));
        //2. Actualizar los campos
        existente.setNombreTipoMantenimiento(json.getNombreTipoMantenimiento());
        //3. Guardar los cambios
        TiposMantenimientoEntity tipoMantenimientoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirATipoMantenimientoDTO(tipoMantenimientoActualizado);
    }

    public boolean eliminarTipoMantenimiento(String id) {
        try {
            //1. Validar existencia del tipoMantenimiento
            TiposMantenimientoEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el tipoMantenimiento, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el tipoMantenimiento con ID: " + id + " para eliminar. ", 1);
        }
    }
}
