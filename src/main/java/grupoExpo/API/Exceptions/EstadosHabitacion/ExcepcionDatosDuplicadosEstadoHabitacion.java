package grupoExpo.API.Exceptions.EstadosHabitacion;

import lombok.Getter;

public class ExcepcionDatosDuplicadosEstadoHabitacion extends RuntimeException {
    @Getter
    private String campoDuplicado;

    public  ExcepcionDatosDuplicadosEstadoHabitacion(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosEstadoHabitacion(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
