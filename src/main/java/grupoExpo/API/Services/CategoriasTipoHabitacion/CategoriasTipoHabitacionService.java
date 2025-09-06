package grupoExpo.API.Services.CategoriasTipoHabitacion;

import grupoExpo.API.Entities.CategoriasTipoHabitacion.CategoriasTipoHabitacionEntity;
import grupoExpo.API.Exceptions.CategoriasTipoHabitacion.ExcepcionCategoriaTipoHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.CategoriasTipoHabitacion.ExcepcionCategoriaTipoHabitacionNoRegistrada;
import grupoExpo.API.Models.DTO.CategoriasTipoHabitacionDTO;
import grupoExpo.API.Repositories.CategoriasTipoHabitacion.CategoriasTipoHabitacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoriasTipoHabitacionService {

    @Autowired
    private CategoriasTipoHabitacionRepository repo;

    public Page<CategoriasTipoHabitacionDTO> getAllCategoriasTipoHabitacion(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<CategoriasTipoHabitacionEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirACategoriaTipoHabitacionDTO);
    }

    private CategoriasTipoHabitacionDTO convertirACategoriaTipoHabitacionDTO(CategoriasTipoHabitacionEntity categoriaTipoHabitacion) {
        CategoriasTipoHabitacionDTO dto = new CategoriasTipoHabitacionDTO();
        dto.setIdCategoriaTipoHabitacion(categoriaTipoHabitacion.getIdCategoriaTipoHabitacion());
        dto.setNombreCategoriaTipoHabitacion(categoriaTipoHabitacion.getNombreCategoriaTipoHabitacion());
        return dto;
    }

    public CategoriasTipoHabitacionDTO insertarDatos(CategoriasTipoHabitacionDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            CategoriasTipoHabitacionEntity entity = ConvertirAEntity(data);
            CategoriasTipoHabitacionEntity categoriaTipoHabitacionGuardado = repo.save(entity);
            return convertirACategoriaTipoHabitacionDTO(categoriaTipoHabitacionGuardado);
        }catch (Exception e){
            log.error("Error al registrar la categoriaTipoHabitacion: " + e.getMessage());
            throw new ExcepcionCategoriaTipoHabitacionNoRegistrada("Error al registrar la categoriaTipoHabitacion.");
        }
    }

    private CategoriasTipoHabitacionEntity ConvertirAEntity(CategoriasTipoHabitacionDTO data) {
        CategoriasTipoHabitacionEntity entity = new CategoriasTipoHabitacionEntity();

        entity.setNombreCategoriaTipoHabitacion(data.getNombreCategoriaTipoHabitacion());
        return entity;
    }

    public CategoriasTipoHabitacionDTO actualizarCategoriaTipoHabitacion(String id, CategoriasTipoHabitacionDTO json) {
        //1. Verificar la existencia de la categoriaTipoHabitacion.
        CategoriasTipoHabitacionEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionCategoriaTipoHabitacionNoEncontrada("CategoriaTipoHabitacion no encontrada"));
        //2. Actualizar los campos
        existente.setNombreCategoriaTipoHabitacion(json.getNombreCategoriaTipoHabitacion());
        //3. Guardar los cambios
        CategoriasTipoHabitacionEntity categoriaTipoHabitacionActualizada = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirACategoriaTipoHabitacionDTO(categoriaTipoHabitacionActualizada);
    }

    public boolean eliminarCategoriaTipoHabitacion(String id) {
        try {
            //1. Validar existencia de la categoriaTipoHabitacion
            CategoriasTipoHabitacionEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar la categoriaTipoHabitacion, si existe retornar true. Si no existe retornar false
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontro la categoriaTipoHabitacion con ID: " + id + " para eliminar. ", 1);
        }
    }
}
