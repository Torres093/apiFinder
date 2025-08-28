package grupoExpo.API.Controllers.EstadosReserva;

import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionDatosDuplicadosEstadoHabitacion;
import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionEstadoHabitacionNoEncontrado;
import grupoExpo.API.Exceptions.EstadosReserva.ExcepcionDatosDuplicadosEstadoReserva;
import grupoExpo.API.Exceptions.EstadosReserva.ExcepcionEstadoReservaNoEncontrado;
import grupoExpo.API.Models.DTO.EstadosHabitacionDTO;
import grupoExpo.API.Models.DTO.EstadosReservaDTO;
import grupoExpo.API.Services.EstadosReserva.EstadosReservaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EstadosReservaController {

    @Autowired
    private EstadosReservaService acceso;

    @GetMapping("/consultarEstadosReserva")
    public List<EstadosReservaDTO> datosEstadosReserva(){
        return acceso.getAllEstadosReserva();
    }

    //Insertar Datos
    @PostMapping("/registrarEstadosReserva")
    public ResponseEntity<?> nuevoEstadoReserva(@Valid @RequestBody EstadosReservaDTO json, HttpServletRequest request){
        try {
            EstadosReservaDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el estadoReserva",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarEstadosReserva/{id}")
    public ResponseEntity<?> modificarEstadoReserva(
            @PathVariable String id,
            @Valid @RequestBody EstadosReservaDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarEstadoReserva" que esta en el service
            EstadosReservaDTO dto = acceso.actualizarEstadoReserva(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionEstadoReservaNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosEstadoReserva e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarEstadosReserva/{id}")
    public ResponseEntity<?> eliminarEstadoReserva(@PathVariable String id){
        try{
            if(!acceso.eliminarEstadoReserva(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "EstadoReserva no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El estadoReserva no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "EstadoReserva eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el estadoReserva",
                    "detail", e.getMessage()
            ));
        }
    }
}
