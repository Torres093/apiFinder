package grupoExpo.API.Exceptions.Empleados;

public class ExcepcionEmpleadoNoRegistrado extends RuntimeException {
    public ExcepcionEmpleadoNoRegistrado(String message) {
        super(message);
    }
}
