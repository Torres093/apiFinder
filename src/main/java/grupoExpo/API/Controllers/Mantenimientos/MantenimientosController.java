package grupoExpo.API.Controllers.Mantenimientos;

import grupoExpo.API.Exceptions.Mantenimientos.ExcepcionDatosDuplicadosMantenimiento;
import grupoExpo.API.Exceptions.Mantenimientos.ExcepcionMantenimientoNoEncontrado;
import grupoExpo.API.Models.DTO.MantenimientosDTO;
import grupoExpo.API.Services.Mantenimientos.MantenimientosService;
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
public class MantenimientosController {

    @Autowired
    private MantenimientosService acceso;

    @GetMapping("/consultarMantenimientos")
    public List<MantenimientosDTO> datosMantenimientos(){
        return acceso.getAllMantenimientos();
    }

    //Insertar Datos
    @PostMapping("/registrarMantenimientos")
    public ResponseEntity<?> nuevoMantenimiento(@Valid @RequestBody MantenimientosDTO json, HttpServletRequest request){
        try {
            MantenimientosDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el mantenimiento",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarMantenimientos/{id}")
    public ResponseEntity<?> modificarMantenimiento(
            @PathVariable String id,
            @Valid @RequestBody MantenimientosDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarMantenimiento" que esta en el service
            MantenimientosDTO dto = acceso.actualizarMantenimiento(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionMantenimientoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosMantenimiento e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarMantenimientos/{id}")
    public ResponseEntity<?> eliminarMantenimiento(@PathVariable String id){
        try{
            if(!acceso.eliminarMantenimiento(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Mantenimiento no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El mantenimiento no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Mantenimiento eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el mantenimiento",
                    "detail", e.getMessage()
            ));
        }
    }
}
