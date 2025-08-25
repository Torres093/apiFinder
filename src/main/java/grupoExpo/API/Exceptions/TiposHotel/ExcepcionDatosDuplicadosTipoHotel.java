package grupoExpo.API.Exceptions.TiposHotel;

import lombok.Getter;

public class ExcepcionDatosDuplicadosTipoHotel extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosTipoHotel(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosTipoHotel(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}

