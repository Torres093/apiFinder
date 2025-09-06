package grupoExpo.API.Controllers.CheckIns;

import grupoExpo.API.Exceptions.CheckIns.ExcepcionCheckInNoEncontrado;
import grupoExpo.API.Exceptions.CheckIns.ExcepcionDatosDuplicadosCheckIn;
import grupoExpo.API.Models.DTO.CheckInsDTO;
import grupoExpo.API.Services.CheckIns.CheckInsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CheckInsController {

    @Autowired
    private CheckInsService acceso;

    //Paginación con datos
    @GetMapping("/consultarCheckIns")
    private ResponseEntity<Page<CheckInsDTO>> datosCheckIns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        //Parte 1. Se evalúa cuantos registros desea por página el usuario.
        //Teniendo como máximo 50 registros por página
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }

        //Parte 2 Invocando a la función getAll contenido en el Service y guardamos los datos
        //Si no hay datos será nulo, de lo contrario no será nulo
        Page<CheckInsDTO> checkIns = acceso.getAllCheckIns(page, size);
        if (checkIns == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay checkIns registrados"
            ));
        }
        return ResponseEntity.ok(checkIns);
    }

    //Insertar Datos
    @PostMapping("/registrarCheckIns")
    public ResponseEntity<?> nuevoCheckIn(@Valid @RequestBody CheckInsDTO json, HttpServletRequest request){
        try {
            CheckInsDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar checkIn",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarCheckIns/{id}")
    public ResponseEntity<?> modificarCheckIn(
            @PathVariable String id,
            @Valid @RequestBody CheckInsDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarCheckIn" que esta en el service
            CheckInsDTO dto = acceso.actualizarCheckIn(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionCheckInNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosCheckIn e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarCheckIns/{id}")
    public ResponseEntity<?> eliminarCheckIn(@PathVariable String id){
        try{
            if(!acceso.eliminarCheckIn(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "CheckIn no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El checkIn no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "CheckIn eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el checkIn",
                    "detail", e.getMessage()
            ));
        }
    }
}
