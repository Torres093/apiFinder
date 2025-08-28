package grupoExpo.API.Exceptions.Eventos;

import lombok.Getter;

public class ExcepcionDatosDuplicadosEvento extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosEvento(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosEvento(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
