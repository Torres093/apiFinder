package grupoExpo.API.Models.apiResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
public class ApiResponse<DTO> {
    private String estado;
    private String mensaje;
    private DTO data;

    public ApiResponse() {}

    public ApiResponse(String estado, String mensaje, DTO data) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.data = data;
    }
}
