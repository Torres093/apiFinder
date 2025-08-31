package grupoExpo.API.Exceptions.CheckIns;

public class ExcepcionCheckInNoRegistrado extends RuntimeException {
    public ExcepcionCheckInNoRegistrado(String message) {
        super(message);
    }
}
