package grupoExpo.API.Exceptions.Platos;

import lombok.Getter;

public class ExcepcionDatosDuplicadosPlato extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosPlato(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosPlato(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
