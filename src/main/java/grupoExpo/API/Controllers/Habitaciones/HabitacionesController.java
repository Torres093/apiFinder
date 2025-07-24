package grupoExpo.API.Controllers.Habitaciones;

import grupoExpo.API.Exceptions.Habitaciones.ExcepcionDatosDuplicadosHabitacion;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Models.DTO.ClientesDTO;
import grupoExpo.API.Models.DTO.HabitacionesDTO;
import grupoExpo.API.Services.Habitaciones.HabitacionesService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class HabitacionesController {

    @Autowired
    private HabitacionesService acceso;

    @GetMapping("/consultarHabitaciones")
    public List<HabitacionesDTO> datosHabitaciones(){
        return acceso.getAllHabitaciones();
    }
    //Insertar Datos
    @PostMapping("/registrarHabitaciones")
    public ResponseEntity<?> nuevaHabitacion(@Valid @RequestBody HabitacionesDTO json, HttpServletRequest request){
        try {
            HabitacionesDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar la habitacion",
                            "detail", e.getMessage()
                    ));
        }
    }
    //Actualizar datos
    @PutMapping("actualizarHabitaciones/{id}")
    public ResponseEntity<?> modificarHabitacion(
            @PathVariable String id,
            @Valid @RequestBody HabitacionesDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarHabitacion" que esta en el service
            HabitacionesDTO dto = acceso.actualizarHabitacion(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionHabitacionNoEncontrada e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosHabitacion e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }
    @DeleteMapping("/eliminarHabitaciones/{id}")
    public ResponseEntity<?> eliminarHabitacion(@PathVariable String id){
        try{
            if(!acceso.eliminarHabitacion(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Habitacion no encontrada")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "La habitacion no fue encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Habitacion eliminada exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la habitacion",
                    "detail", e.getMessage()
            ));
        }
    }
}
