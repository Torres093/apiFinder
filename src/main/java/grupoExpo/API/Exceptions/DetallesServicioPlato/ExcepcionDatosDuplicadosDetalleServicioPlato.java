package grupoExpo.API.Exceptions.DetallesServicioPlato;

import lombok.Getter;

public class ExcepcionDatosDuplicadosDetalleServicioPlato extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosDetalleServicioPlato(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosDetalleServicioPlato(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
