package grupoExpo.API.Exceptions.Servicios;

import lombok.Getter;

public class ExcepcionDatosDuplicadosServicio extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosServicio(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosServicio(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
