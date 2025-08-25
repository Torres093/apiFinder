package grupoExpo.API.Services.Roles;

import grupoExpo.API.Entities.Reseñas.ReseñasEntity;
import grupoExpo.API.Entities.Roles.RolesEntity;
import grupoExpo.API.Exceptions.Reseñas.ExcepcionReseñaNoEncontrada;
import grupoExpo.API.Exceptions.Reseñas.ExcepcionReseñaNoRegistrada;
import grupoExpo.API.Exceptions.Roles.ExcepcionRolNoEncontrado;
import grupoExpo.API.Exceptions.Roles.ExcepcionRolNoRegistrado;
import grupoExpo.API.Models.DTO.ReseñasDTO;
import grupoExpo.API.Models.DTO.RolesDTO;
import grupoExpo.API.Repositories.Roles.RolesRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RolesService {

    @Autowired
    private RolesRepository repo;

    public List<RolesDTO> getAllRoles(){
        List<RolesEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirARolesDTO)
                .collect(Collectors.toList());
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
