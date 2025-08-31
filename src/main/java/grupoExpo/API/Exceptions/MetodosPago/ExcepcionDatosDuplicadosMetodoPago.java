package grupoExpo.API.Exceptions.MetodosPago;

import lombok.Getter;

public class ExcepcionDatosDuplicadosMetodoPago extends RuntimeException {
    
    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosMetodoPago(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosMetodoPago(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
