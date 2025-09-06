package grupoExpo.API.Controllers.EstadosHabitacion;

import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionDatosDuplicadosEstadoHabitacion;
import grupoExpo.API.Exceptions.EstadosHabitacion.ExcepcionEstadoHabitacionNoEncontrado;
import grupoExpo.API.Models.DTO.EstadosHabitacionDTO;
import grupoExpo.API.Services.EstadosHabitacion.EstadosHabitacionService;
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
public class EstadosHabitacionController {

    @Autowired
    private EstadosHabitacionService acceso;

    //Paginación con datos
    @GetMapping("/consultarEstadosHabitacion")
    private ResponseEntity<Page<EstadosHabitacionDTO>> datosEstadosHabitacion(
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
        Page<EstadosHabitacionDTO> estadosHabitacion = acceso.getAllEstadosHabitacion(page, size);
        if (estadosHabitacion == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay estados de habitaciones registradas"
            ));
        }
        return ResponseEntity.ok(estadosHabitacion);
    }

    //Insertar Datos
    @PostMapping("/registrarEstadosHabitacion")
    public ResponseEntity<?> nuevoEstadoHabitacion(@Valid @RequestBody EstadosHabitacionDTO json, HttpServletRequest request){
        try {
            EstadosHabitacionDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el estado de habitación",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarEstadosHabitacion/{id}")
    public ResponseEntity<?> modificarEstadoHabitacion(
            @PathVariable String id,
            @Valid @RequestBody EstadosHabitacionDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de estado habitacion DTO y se invoca en el metodo "actualizarEstadoHabitacion" que esta en el service
            EstadosHabitacionDTO dto = acceso.actualizarEstadoHabitacion(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionEstadoHabitacionNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosEstadoHabitacion e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarEstadosHabitacion/{id}")
    public ResponseEntity<?> eliminarEstadoHabitacion(@PathVariable String id){
        try{
            if(!acceso.eliminarEstadoHabitacion(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Estado no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El estado de habitación no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Estado de habitación  eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el estado de habitación",
                    "detail", e.getMessage()
            ));
        }
    }
}
