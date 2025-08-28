package grupoExpo.API.Exceptions.EstadosReserva;

public class ExcepcionEstadoReservaNoRegistrado extends RuntimeException {
    public ExcepcionEstadoReservaNoRegistrado(String message) {
        super(message);
    }
}
