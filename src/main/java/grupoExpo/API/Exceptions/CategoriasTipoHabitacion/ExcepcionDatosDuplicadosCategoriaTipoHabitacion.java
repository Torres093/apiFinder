package grupoExpo.API.Exceptions.CategoriasTipoHabitacion;

import lombok.Getter;

public class ExcepcionDatosDuplicadosCategoriaTipoHabitacion extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosCategoriaTipoHabitacion(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosCategoriaTipoHabitacion(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
