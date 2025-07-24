package grupoExpo.API.Controllers.Reseñas;

import grupoExpo.API.Models.DTO.HabitacionesDTO;
import grupoExpo.API.Models.DTO.ReseñasDTO;
import grupoExpo.API.Services.Reseñas.ReseñasService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReseñasController {

    @Autowired
    private ReseñasService acceso;

    @GetMapping("/consultarReseñas")
    public List<ReseñasDTO> datosReseñas(){
        return acceso.getAllReseñas();
    }
    //Insertar Datos
    @PostMapping("/registrarReseñas")
    public ResponseEntity<?> nuevaReseña(@Valid @RequestBody ReseñasDTO json, HttpServletRequest request){
        try {
            ReseñasDTO respuesta = acceso.insertarDatos(json);
            if(respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Inserción fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no pudieron ser registrados"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Success",
                    "data", respuesta
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "Error",
                            "message", "Error no controlado al registrar la reseña",
                            "detail", e.getMessage()
                    ));
        }
    }
}
