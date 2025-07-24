package grupoExpo.API.Exceptions.Habitaciones;

import lombok.Getter;

public class ExcepcionDatosDuplicadosHabitacion extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosHabitacion(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosHabitacion(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
