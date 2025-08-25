package grupoExpo.API.Exceptions.Usuarios;

public class ExcepcionUsuarioNoRegistrado extends RuntimeException {
    public ExcepcionUsuarioNoRegistrado(String message) {
        super(message);
    }
}
