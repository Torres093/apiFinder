package grupoExpo.API.Exceptions.Reservas;

import lombok.Getter;

public class ExcepcionDatosDuplicadosReserva extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosReserva(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosReserva(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
