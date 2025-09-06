package grupoExpo.API.Services.TiposHabitacion;

import grupoExpo.API.Entities.CategoriasTipoHabitacion.CategoriasTipoHabitacionEntity;
import grupoExpo.API.Entities.TiposHabitacion.TiposHabitacionEntity;
import grupoExpo.API.Exceptions.TiposHabitacion.ExcepcionTipoHabitacionNoEncontrado;
import grupoExpo.API.Exceptions.TiposHabitacion.ExcepcionTipoHabitacionNoRegistrado;
import grupoExpo.API.Models.DTO.TiposHabitacionDTO;
import grupoExpo.API.Repositories.TiposHabitacion.TiposHabitacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TiposHabitacionService {

    @Autowired
    private TiposHabitacionRepository repo;

    public Page<TiposHabitacionDTO> getAllTiposHabitacion(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<TiposHabitacionEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirATipoHabitacionDTO);
    }

    private TiposHabitacionDTO convertirATipoHabitacionDTO(TiposHabitacionEntity tipoHabitacion) {
        TiposHabitacionDTO dto = new TiposHabitacionDTO();
        dto.setIdTipoHabitacion(tipoHabitacion.getIdTipoHabitacion());
        dto.setIdCategoriaTipoHabitacion(tipoHabitacion.getCategoriaTipoHabitacion().getIdCategoriaTipoHabitacion());
        dto.setNombreTipoHabitacion(tipoHabitacion.getNombreTipoHabitacion());
        dto.setDescripcionTipoHabitacion(tipoHabitacion.getDescripcionTipoHabitacion());
        return dto;
    }

    public TiposHabitacionDTO insertarDatos(TiposHabitacionDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            TiposHabitacionEntity entity = ConvertirAEntity(data);
            TiposHabitacionEntity tipoHabitacionGuardado = repo.save(entity);
            return convertirATipoHabitacionDTO(tipoHabitacionGuardado);
        }catch (Exception e){
            log.error("Error al registrar el tipoHabitacion: " + e.getMessage());
            throw new ExcepcionTipoHabitacionNoRegistrado("Error al registrar el tipoHabitacion.");
        }
    }

    private TiposHabitacionEntity ConvertirAEntity(TiposHabitacionDTO data) {
        TiposHabitacionEntity entity = new TiposHabitacionEntity();

        //Asignando categoriaTipoHabitacion a entity de TiposHabitacion
        CategoriasTipoHabitacionEntity categoriaTipoHabitacion = new CategoriasTipoHabitacionEntity();
        categoriaTipoHabitacion.setIdCategoriaTipoHabitacion(data.getIdCategoriaTipoHabitacion());
        entity.setCategoriaTipoHabitacion(categoriaTipoHabitacion);

        //Asignando atributos de DTO a entity
        entity.setNombreTipoHabitacion(data.getNombreTipoHabitacion());
        entity.setDescripcionTipoHabitacion(data.getDescripcionTipoHabitacion());
        return entity;
    }

    public TiposHabitacionDTO actualizarTipoHabitacion(String id, TiposHabitacionDTO json) {
        //1. Verificar la existencia del tipoHabitacion.
        TiposHabitacionEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionTipoHabitacionNoEncontrado("TipoHabitacion no encontrado"));
        //2. Actualizar los campos

        //Asignando categoriaTipoHabitacion a entity de TiposHabitacion
        CategoriasTipoHabitacionEntity categoriaTipoHabitacion = new CategoriasTipoHabitacionEntity();
        categoriaTipoHabitacion.setIdCategoriaTipoHabitacion(json.getIdCategoriaTipoHabitacion());
        existente.setCategoriaTipoHabitacion(categoriaTipoHabitacion);

        //Asignando atributos de DTO a entity
        existente.setNombreTipoHabitacion(json.getNombreTipoHabitacion());
        existente.setDescripcionTipoHabitacion(json.getDescripcionTipoHabitacion());

        //3. Guardar los cambios
        TiposHabitacionEntity tipoHabitacionActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirATipoHabitacionDTO(tipoHabitacionActualizado);
    }

    public boolean eliminarTipoHabitacion(String id) {
        try {
            //1. Validar existencia del tipoHabitacion
            TiposHabitacionEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el tipoHabitacion, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el tipoHabitacion con ID: " + id + " para eliminar. ", 1);
        }
    }
}
