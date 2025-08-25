package grupoExpo.API.Exceptions.MetodosPago;

public class ExcepcionMetodoPagoNoRegistrado extends RuntimeException {
    public ExcepcionMetodoPagoNoRegistrado(String message) {
        super(message);
    }
}
