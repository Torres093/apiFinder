package grupoExpo.API.Exceptions.Mantenimientos;

public class ExcepcionMantenimientoNoRegistrado extends RuntimeException {
    public ExcepcionMantenimientoNoRegistrado(String message) {
        super(message);
    }
}
