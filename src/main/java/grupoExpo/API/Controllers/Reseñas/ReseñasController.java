package grupoExpo.API.Controllers.Reseñas;

import grupoExpo.API.Exceptions.Reseñas.ExcepcionDatosDuplicadosReseña;
import grupoExpo.API.Exceptions.Reseñas.ExcepcionReseñaNoEncontrada;
import grupoExpo.API.Models.DTO.ReseñasDTO;
import grupoExpo.API.Services.Reseñas.ReseñasService;
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
public class ReseñasController {

    @Autowired
    private ReseñasService acceso;

    //Paginación con datos
    @GetMapping("/consultarReseñas")
    private ResponseEntity<Page<ReseñasDTO>> datosReseñas(
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
        Page<ReseñasDTO> reseñas = acceso.getAllReseñas(page, size);
        if (reseñas == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay reseñas registradas"
            ));
        }
        return ResponseEntity.ok(reseñas);
    }

    //Insertar Datos
    @PostMapping("/registrarReseñas")
    public ResponseEntity<?> nuevaReseña(@Valid @RequestBody ReseñasDTO json, HttpServletRequest request){
        try {
            ReseñasDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar la reseña",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarReseñas/{id}")
    public ResponseEntity<?> modificarReseña(
            @PathVariable String id,
            @Valid @RequestBody ReseñasDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarReseña" que esta en el service
            ReseñasDTO dto = acceso.actualizarReseña(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionReseñaNoEncontrada e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosReseña e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarReseñas/{id}")
    public ResponseEntity<?> eliminarReseña(@PathVariable String id){
        try{
            if(!acceso.eliminarReseña(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Reseña no encontrada")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "La reseña no fue encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Reseña eliminada exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la reseña",
                    "detail", e.getMessage()
            ));
        }
    }
}
