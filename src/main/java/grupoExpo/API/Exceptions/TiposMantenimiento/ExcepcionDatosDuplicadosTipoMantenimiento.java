package grupoExpo.API.Exceptions.TiposMantenimiento;

import lombok.Getter;

public class ExcepcionDatosDuplicadosTipoMantenimiento extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosTipoMantenimiento(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosTipoMantenimiento(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
