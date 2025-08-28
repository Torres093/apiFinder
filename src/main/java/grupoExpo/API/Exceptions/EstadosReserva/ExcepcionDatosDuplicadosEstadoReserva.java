package grupoExpo.API.Exceptions.EstadosReserva;

import lombok.Getter;

public class ExcepcionDatosDuplicadosEstadoReserva extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosEstadoReserva(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosEstadoReserva(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
