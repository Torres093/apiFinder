package grupoExpo.API.Controllers.Eventos;

import grupoExpo.API.Exceptions.Eventos.ExcepcionDatosDuplicadosEvento;
import grupoExpo.API.Exceptions.Eventos.ExcepcionEventoNoEncontrado;
import grupoExpo.API.Models.DTO.EventosDTO;
import grupoExpo.API.Services.Eventos.EventosService;
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
public class EventosController {

    @Autowired
    private EventosService acceso;

    //Paginación con datos
    @GetMapping("/consultarEventos")
    private ResponseEntity<Page<EventosDTO>> datosEventos(
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
        Page<EventosDTO> eventos = acceso.getAllEventos(page, size);
        if (eventos == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay eventos registrados"
            ));
        }
        return ResponseEntity.ok(eventos);
    }

    //Insertar Datos
    @PostMapping("/registrarEventos")
    public ResponseEntity<?> nuevoEvento(@Valid @RequestBody EventosDTO json, HttpServletRequest request){
        try {
            EventosDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el evento",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarEventos/{id}")
    public ResponseEntity<?> modificarEvento(
            @PathVariable String id,
            @Valid @RequestBody EventosDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarEvento" que esta en el service
            EventosDTO dto = acceso.actualizarEvento(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionEventoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosEvento e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarEventos/{id}")
    public ResponseEntity<?> eliminarEvento(@PathVariable String id){
        try{
            if(!acceso.eliminarEvento(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Evento no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El evento no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Evento eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el evento",
                    "detail", e.getMessage()
            ));
        }
    }
}
