package grupoExpo.API.Exceptions.TiposHabitacion;

import lombok.Getter;

public class ExcepcionDatosDuplicadosTipoHabitacion extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public ExcepcionDatosDuplicadosTipoHabitacion(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExcepcionDatosDuplicadosTipoHabitacion(String campoDuplicado){
    this.campoDuplicado = campoDuplicado;
  }
}
