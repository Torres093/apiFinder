package grupoExpo.API.Services.Usuarios;

import grupoExpo.API.Entities.User.UserEntity;
import grupoExpo.API.Models.DTO.UserDTO;
import grupoExpo.API.Repositories.User.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuariosService {
    @Autowired
    UserRepo repository;

    public List<UserDTO> obtenerUsuarios(){
        List<UserEntity> usuarios = repository.findAll();
        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param data
     * @return
     */
    public UserDTO insertarDatos(UserDTO data){
        if(data == null || data.getContraseñaUsuario() == null || data.getContraseñaUsuario().isEmpty()){
            throw new IllegalArgumentException("Usuario o contraseña no validos");
        }try {
            UserEntity entity = convertirAEntity(data);
            UserEntity usuarioGuardado = repository.save(entity);
            return convertirADTO(usuarioGuardado);
        }catch (Exception e){
            log.error("Error al insertar datos de usuario" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param data
     * @return
     */
    private UserEntity convertirAEntity(UserDTO data) {
        UserEntity entity = new UserEntity();

        entity.

    }

    /**
     *
     * @param userEntity
     * @return
     */
    private UserDTO convertirADTO(UserEntity userEntity) {

        UserDTO dto = new UserDTO();

        dto.setIdusuario();

    }




}
