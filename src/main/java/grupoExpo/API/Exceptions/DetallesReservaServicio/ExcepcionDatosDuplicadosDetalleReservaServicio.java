package grupoExpo.API.Exceptions.DetallesReservaServicio;

import lombok.Getter;

public class ExcepcionDatosDuplicadosDetalleReservaServicio extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosDetalleReservaServicio(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosDetalleReservaServicio(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
