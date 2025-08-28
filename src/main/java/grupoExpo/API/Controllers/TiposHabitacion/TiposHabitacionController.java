package grupoExpo.API.Controllers.TiposHabitacion;

import grupoExpo.API.Exceptions.TiposHabitacion.ExcepcionDatosDuplicadosTipoHabitacion;
import grupoExpo.API.Exceptions.TiposHabitacion.ExcepcionTipoHabitacionNoEncontrado;
import grupoExpo.API.Models.DTO.TiposHabitacionDTO;
import grupoExpo.API.Services.TiposHabitacion.TiposHabitacionService;
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
public class TiposHabitacionController {

    @Autowired
    private TiposHabitacionService acceso;

    @CrossOrigin
    @GetMapping("/consultarTiposHabitacion")
    public List<TiposHabitacionDTO> datosTiposHabitacion(){
       return acceso.getAllTiposHabitacion();
    }

    //Insertar Datos
    @PostMapping("/registrarTiposHabitacion")
    public ResponseEntity<?> nuevoTipoHabitacion(@Valid @RequestBody TiposHabitacionDTO json, HttpServletRequest request){
        try {
            TiposHabitacionDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar tipoHabitacion",
                            "detail", e.getMessage()
                    ));
        }
    }
    //Actualizar datos
    @PutMapping("actualizarTiposHabitacion/{id}")
    public ResponseEntity<?> modificarTipoHabitacion(
            @PathVariable String id,
            @Valid @RequestBody TiposHabitacionDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarTipoHabitacion" que esta en el service
            TiposHabitacionDTO dto = acceso.actualizarTipoHabitacion(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionTipoHabitacionNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosTipoHabitacion e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarTiposHabitacion/{id}")
    public ResponseEntity<?> eliminarTipoHabitacion(@PathVariable String id){
        try{
            if(!acceso.eliminarTipoHabitacion(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "TipoHabitacion no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El tipoHabitacion no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "TipoHabitacion eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el tipoHabitacion",
                    "detail", e.getMessage()
            ));
        }
    }
}
