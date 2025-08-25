package grupoExpo.API.Exceptions.Roles;

import lombok.Getter;

public class ExcepcionDatosDuplicadosRol extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosRol(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosRol(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
