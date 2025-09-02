package grupoExpo.API.Controllers.TiposMantenimiento;

import grupoExpo.API.Exceptions.TiposMantenimiento.ExcepcionDatosDuplicadosTipoMantenimiento;
import grupoExpo.API.Exceptions.TiposMantenimiento.ExcepcionTipoMantenimientoNoEncontrado;
import grupoExpo.API.Models.DTO.TiposMantenimientoDTO;
import grupoExpo.API.Services.TiposMantenimiento.TiposMantenimientoService;
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
@CrossOrigin
public class TiposMantenimientoController {

    @Autowired
    private TiposMantenimientoService acceso;

    @GetMapping("/consultarTiposMantenimiento")
    public List<TiposMantenimientoDTO> datosTiposMantenimiento(){
        return acceso.getAllTiposMantenimiento();
    }

    //Insertar Datos
    @PostMapping("/registrarTiposMantenimiento")
    public ResponseEntity<?> nuevoTipoMantenimiento(@Valid @RequestBody TiposMantenimientoDTO json, HttpServletRequest request){
        try {
            TiposMantenimientoDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el tipoMantenimiento",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarTiposMantenimiento/{id}")
    public ResponseEntity<?> modificarTipoMantenimiento(
            @PathVariable String id,
            @Valid @RequestBody TiposMantenimientoDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarTipoMantenimiento" que esta en el service
            TiposMantenimientoDTO dto = acceso.actualizarTipoMantenimiento(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionTipoMantenimientoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosTipoMantenimiento e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarTiposMantenimiento/{id}")
    public ResponseEntity<?> eliminarTipoMantenimiento(@PathVariable String id){
        try{
            if(!acceso.eliminarTipoMantenimiento(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "TipoMantenimiento no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El tipoMantenimiento no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "TipoMantenimiento eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el tipoMantenimiento",
                    "detail", e.getMessage()
            ));
        }
    }
}
