package grupoExpo.API.Exceptions.Usuarios;

public class ExcepcionUsuarioNoEncontrado extends RuntimeException {
    public ExcepcionUsuarioNoEncontrado(String message) {
        super(message);
    }
}
