package grupoExpo.API.Exceptions.Hotel;

import lombok.Getter;

public class ExcepcionDatosDuplicadosHotel extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosHotel(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosHotel(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
