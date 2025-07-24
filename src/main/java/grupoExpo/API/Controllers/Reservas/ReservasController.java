package grupoExpo.API.Controllers.Reservas;

import grupoExpo.API.Exceptions.Habitaciones.ExcepcionDatosDuplicadosHabitacion;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Reservas.ExcepcionDatosDuplicadosReserva;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoEncontrada;
import grupoExpo.API.Models.DTO.HabitacionesDTO;
import grupoExpo.API.Models.DTO.ReservasDTO;
import grupoExpo.API.Services.Reservas.ReservasService;
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
public class ReservasController {

    @Autowired
    private ReservasService acceso;

    @GetMapping("/consultarReservas")
    public List<ReservasDTO> datosReservas(){
        return acceso.getAllReservas();
    }
    //Insertar Datos
    @PostMapping("/registrarReservas")
    public ResponseEntity<?> nuevaReserva(@Valid @RequestBody ReservasDTO json, HttpServletRequest request){
        try {
            ReservasDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar la reserva",
                            "detail", e.getMessage()
                    ));
        }
    }
    //Actualizar datos
    @PutMapping("actualizarReservas/{id}")
    public ResponseEntity<?> modificarReserva(
            @PathVariable String id,
            @Valid @RequestBody ReservasDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarReserva" que esta en el service
            ReservasDTO dto = acceso.actualizarReserva(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionReservaNoEncontrada e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosReserva e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }
    @DeleteMapping("/eliminarReservas/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable String id){
        try{
            if(!acceso.eliminarReserva(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Reserva no encontrada")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "La reserva no fue encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Reserva eliminada exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la reserva",
                    "detail", e.getMessage()
            ));
        }
    }
}
