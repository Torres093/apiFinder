package grupoExpo.API.Exceptions.DetallesReserva;

import lombok.Getter;

public class ExcepcionDatosDuplicadosDetalleReserva extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public  ExcepcionDatosDuplicadosDetalleReserva(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExcepcionDatosDuplicadosDetalleReserva(String campoDuplicado){
    this.campoDuplicado = campoDuplicado;
  }
}
