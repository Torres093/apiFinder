package grupoExpo.API.Exceptions.Mantenimientos;

import lombok.Getter;

public class ExcepcionDatosDuplicadosMantenimiento extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosMantenimiento(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosMantenimiento(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
