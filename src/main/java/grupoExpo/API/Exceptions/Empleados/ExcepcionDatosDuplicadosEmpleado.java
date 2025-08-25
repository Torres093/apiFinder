package grupoExpo.API.Exceptions.Empleados;

import lombok.Getter;

public class ExcepcionDatosDuplicadosEmpleado extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public ExcepcionDatosDuplicadosEmpleado(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExcepcionDatosDuplicadosEmpleado(String campoDuplicado){
    this.campoDuplicado = campoDuplicado;
  }
}
