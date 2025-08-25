package grupoExpo.API.Services.Usuarios;

import grupoExpo.API.Entities.Roles.RolesEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Exceptions.Servicios.ExcepcionServicioNoEncontrado;
import grupoExpo.API.Exceptions.Servicios.ExcepcionServicioNoRegistrado;
import grupoExpo.API.Exceptions.Usuarios.ExcepcionUsuarioNoEncontrado;
import grupoExpo.API.Exceptions.Usuarios.ExcepcionUsuarioNoRegistrado;
import grupoExpo.API.Models.DTO.ServiciosDTO;
import grupoExpo.API.Models.DTO.UsuariosDTO;
import grupoExpo.API.Repositories.Usuarios.UsuariosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository repo;

    public List<UsuariosDTO> getAllUsuarios() {
        List<UsuariosEntity> usuarios = repo.findAll();
        return usuarios.stream()
                .map(this::convertirAUsuarioDTO)
                .collect(Collectors.toList());
    }

    private UsuariosDTO convertirAUsuarioDTO(UsuariosEntity usuario) {
        UsuariosDTO dto = new UsuariosDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdRol(usuario.getRol().getIdRol());
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
        RolesEntity rol = new RolesEntity();
        rol.setIdRol(data.getIdRol()); // Esto es un String, asumiendo que es el ID del rol
        entity.setRol(rol);

        //Asignando atributos de DTO a entity
        entity.setNombreUsuario(data.getNombreUsuario());
        entity.setCorreoUsuario(data.getCorreoUsuario());
        entity.setContraseñaUsuario(data.getContraseñaUsuario());
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
        RolesEntity rol = new RolesEntity();
        rol.setIdRol(json.getIdRol()); // Esto es un String, asumiendo que es el ID del rol
        existente.setRol(rol);

        //Asignando atributos de DTO a entity
        existente.setNombreUsuario(json.getNombreUsuario());
        existente.setCorreoUsuario(json.getCorreoUsuario());
        existente.setContraseñaUsuario(json.getContraseñaUsuario());
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
