package grupoExpo.API.Exceptions.CheckIns;

import lombok.Getter;

public class ExcepcionDatosDuplicadosCheckIn extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public ExcepcionDatosDuplicadosCheckIn(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExcepcionDatosDuplicadosCheckIn(String campoDuplicado){
    this.campoDuplicado = campoDuplicado;
  }
}
