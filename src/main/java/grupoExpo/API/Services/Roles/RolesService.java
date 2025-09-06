package grupoExpo.API.Services.Roles;

import grupoExpo.API.Entities.Roles.RolesEntity;
import grupoExpo.API.Exceptions.Roles.ExcepcionRolNoEncontrado;
import grupoExpo.API.Exceptions.Roles.ExcepcionRolNoRegistrado;
import grupoExpo.API.Models.DTO.RolesDTO;
import grupoExpo.API.Repositories.Roles.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RolesService {

    @Autowired
    private RolesRepository repo;

    public Page<RolesDTO> getAllRoles(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<RolesEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirARolesDTO);
    }

    private RolesDTO convertirARolesDTO(RolesEntity roles) {
        RolesDTO dto = new RolesDTO();
        dto.setIdRol(roles.getIdRol());
        dto.setNombreRol(roles.getNombreRol());
        return dto;
    }

    public RolesDTO insertarDatos(RolesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            RolesEntity entity = ConvertirAEntity(data);
            RolesEntity RolGuardado = repo.save(entity);
            return convertirARolesDTO(RolGuardado);
        }catch (Exception e){
            log.error("Error al registrar el rol: " + e.getMessage());
            throw new ExcepcionRolNoRegistrado("Error al registrar el rol.");
        }
    }

    private RolesEntity ConvertirAEntity(RolesDTO data) {
        RolesEntity entity = new RolesEntity();

        entity.setNombreRol(data.getNombreRol());
        return entity;
    }

    public RolesDTO actualizarRol(String id, RolesDTO json) {
        //1. Verificar la existencia del rol.
        RolesEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionRolNoEncontrado("Rol no encontrado"));
        //2. Actualizar los campos
        existente.setNombreRol(json.getNombreRol());
        //3. Guardar los cambios
        RolesEntity rolActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirARolesDTO(rolActualizado);
    }

    public boolean eliminarRol(String id) {
        try {
            //1. Validar existencia del rol
            RolesEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el rol, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el rol con ID: " + id + " para eliminar. ", 1);
        }
    }
}
