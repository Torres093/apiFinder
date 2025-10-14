package grupoExpo.API.Services.Usuarios;

import grupoExpo.API.Config.Argon2Password;
import grupoExpo.API.Entities.Roles.RolesEntity;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;;
import grupoExpo.API.Exceptions.Roles.ExcepcionRolNoEncontrado;
import grupoExpo.API.Exceptions.Usuarios.ExcepcionUsuarioNoEncontrado;
import grupoExpo.API.Exceptions.Usuarios.ExcepcionUsuarioNoRegistrado;
import grupoExpo.API.Models.DTO.UsuariosDTO;
import grupoExpo.API.Repositories.Roles.RolesRepository;
import grupoExpo.API.Repositories.Usuarios.UsuariosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository repo;

    @Autowired
    private RolesRepository repoRoles;

    @Autowired
    private Argon2Password argon2;

    public Page<UsuariosDTO> getAllUsuarios(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<UsuariosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAUsuarioDTO);
    }

    private UsuariosDTO convertirAUsuarioDTO(UsuariosEntity usuario) {
        UsuariosDTO dto = new UsuariosDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        if (usuario.getRol() != null){
            dto.setNombreRol(usuario.getRol().getNombreRol());
            dto.setIdRol(usuario.getRol().getIdRol());
        }else{
            dto.setNombreRol("Sin nombre del rol asignado");
            dto.setIdRol(null);
        }
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setCorreoUsuario(usuario.getCorreoUsuario());
        dto.setContraseñaUsuario(usuario.getContraseñaUsuario());
        dto.setSegurityAnswerUsuario(usuario.getSegurityAnswerUsuario());
        dto.setImagenUsuario(usuario.getImagenUsuario());
        dto.setGeneroUsuario(usuario.getGeneroUsuario());
        return dto;
    }

    public UsuariosDTO insertarDatos(UsuariosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            UsuariosEntity entity = ConvertirAEntity(data);
            UsuariosEntity usuarioGuardado = repo.save(entity);
            return convertirAUsuarioDTO(usuarioGuardado);
        }catch (Exception e){
            log.error("Error al registrar el usuario: " + e.getMessage());
            throw new ExcepcionUsuarioNoRegistrado("Error al registrar el usuario.");
        }
    }

    private UsuariosEntity ConvertirAEntity(UsuariosDTO data) {
        UsuariosEntity entity = new UsuariosEntity();

        //Asignando rol a entity de Usuarios
        if (data.getIdRol() != null){
            RolesEntity rol = repoRoles.findById(data.getIdRol())
                    .orElseThrow(()-> new ExcepcionRolNoEncontrado("ID del rol no encontrado"));
            entity.setRol(rol);
        }

        //Asignando atributos de DTO a entity
        entity.setNombreUsuario(data.getNombreUsuario());
        entity.setCorreoUsuario(data.getCorreoUsuario());
        entity.setContraseñaUsuario(argon2.EncryptPassword(data.getContraseñaUsuario()));
        entity.setSegurityAnswerUsuario(data.getSegurityAnswerUsuario());
        entity.setImagenUsuario(data.getImagenUsuario());
        entity.setGeneroUsuario(data.getGeneroUsuario());
        return entity;
    }

    public UsuariosDTO actualizarUsuario(String id, UsuariosDTO json) {
        //1. Verificar la existencia del usuario.
        UsuariosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado"));
        //2. Actualizar los campos

        //Asignando rol a entity de Usuarios
        if (json.getIdRol() != null){
            RolesEntity rol = repoRoles.findById(json.getIdRol())
                    .orElseThrow(()-> new ExcepcionRolNoEncontrado("ID del rol no encontrado"));
            existente.setRol(rol);
        }

        //Asignando atributos de DTO a entity
        existente.setNombreUsuario(json.getNombreUsuario());
        existente.setCorreoUsuario(json.getCorreoUsuario());
        existente.setContraseñaUsuario(argon2.EncryptPassword(json.getContraseñaUsuario()));
        existente.setSegurityAnswerUsuario(json.getSegurityAnswerUsuario());
        existente.setImagenUsuario(json.getImagenUsuario());
        existente.setGeneroUsuario(json.getGeneroUsuario());
        //3. Guardar los cambios
        UsuariosEntity usuarioActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAUsuarioDTO(usuarioActualizado);
    }

    public boolean eliminarUsuario(String id) {
        try {
            //1. Validar existencia del usuario
            UsuariosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el usuario, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el usuario con ID: " + id + " para eliminar. ", 1);
        }
    }
}
