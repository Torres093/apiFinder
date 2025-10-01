package grupoExpo.API.Services.Auth;

import grupoExpo.API.Config.Argon2Password;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Repositories.Usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuariosRepository repo;

    public boolean Login(String correo, String password){
        //Crear el objeto de tipo Argon2
        Argon2Password objHash = new Argon2Password();

        //Invocar un metodo que permite buscar el usuario por su correo
        Optional<UsuariosEntity> list = repo.findByCorreoUsuario(correo).stream().findFirst();

        if (list.isPresent()){
            UsuariosEntity usuario = list.get();
            String nombreRolUsuario = usuario.getRol().getNombreRol();

            System.out.println("Usuario encontrado ID: " + usuario.getIdUsuario() +
                    ", email: " + usuario.getCorreoUsuario() +
                    ", rol: " + nombreRolUsuario);

            String HashDB = usuario.getContraseñaUsuario();
            return objHash.VerifyPassword(HashDB, password);
        }
        return false;
    }

    public Optional<UsuariosEntity> obtenerUsuario(String correo) {
        //Buscar usuario completo en la base de datos
        Optional<UsuariosEntity> userOpt = repo.findByCorreoUsuario(correo);
        return (userOpt != null) ? userOpt : null;
    }
}
