package grupoExpo.API.Exceptions.Cargos;

import lombok.Getter;

public class ExcepcionDatosDuplicadosCargo extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosCargo(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosCargo(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
