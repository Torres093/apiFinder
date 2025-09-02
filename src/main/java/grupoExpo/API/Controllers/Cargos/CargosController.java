package grupoExpo.API.Controllers.Cargos;

import grupoExpo.API.Exceptions.Cargos.ExcepcionCargoNoEncontrado;
import grupoExpo.API.Exceptions.Cargos.ExcepcionDatosDuplicadosCargo;
import grupoExpo.API.Models.DTO.CargosDTO;
import grupoExpo.API.Services.Cargos.CargosService;
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
public class CargosController {

    @Autowired
    private CargosService acceso;

    @GetMapping("/consultarCargos")
    private List<CargosDTO> datosCargos(){
        return acceso.getAllCargos();
    }

    //Insertar Datos
    @PostMapping("/registrarCargos")
    public ResponseEntity<?> nuevoCargo(@Valid @RequestBody CargosDTO json, HttpServletRequest request){
        try {
            CargosDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar cargo",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarCargos/{id}")
    public ResponseEntity<?> modificarCargo(
            @PathVariable String id,
            @Valid @RequestBody CargosDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarCargo" que esta en el service
            CargosDTO dto = acceso.actualizarCargo(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionCargoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosCargo e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarCargos/{id}")
    public ResponseEntity<?> eliminarCargo(@PathVariable String id){
        try{
            if(!acceso.eliminarCargo(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Cargo no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El cargo no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Cargo eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el cargo",
                    "detail", e.getMessage()
            ));
        }
    }
}
