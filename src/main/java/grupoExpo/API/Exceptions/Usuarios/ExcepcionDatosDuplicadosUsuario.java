package grupoExpo.API.Exceptions.Usuarios;

import lombok.Getter;

public class ExcepcionDatosDuplicadosUsuario extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public ExcepcionDatosDuplicadosUsuario(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExcepcionDatosDuplicadosUsuario(String campoDuplicado){
    this.campoDuplicado = campoDuplicado;
  }
}
