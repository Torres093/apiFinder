package grupoExpo.API.Exceptions.Reseñas;

import lombok.Getter;

public class ExcepcionDatosDuplicadosReseña extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public ExcepcionDatosDuplicadosReseña(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExcepcionDatosDuplicadosReseña(String campoDuplicado){
    this.campoDuplicado = campoDuplicado;
  }
}
