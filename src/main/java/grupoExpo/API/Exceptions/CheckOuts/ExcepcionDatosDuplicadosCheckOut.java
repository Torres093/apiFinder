package grupoExpo.API.Exceptions.CheckOuts;

import lombok.Getter;

public class ExcepcionDatosDuplicadosCheckOut extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosCheckOut(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosCheckOut(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
