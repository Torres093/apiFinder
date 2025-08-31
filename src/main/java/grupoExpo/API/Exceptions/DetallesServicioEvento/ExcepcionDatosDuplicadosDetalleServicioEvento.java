package grupoExpo.API.Exceptions.DetallesServicioEvento;

import lombok.Getter;

public class ExcepcionDatosDuplicadosDetalleServicioEvento extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosDetalleServicioEvento(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosDetalleServicioEvento(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
