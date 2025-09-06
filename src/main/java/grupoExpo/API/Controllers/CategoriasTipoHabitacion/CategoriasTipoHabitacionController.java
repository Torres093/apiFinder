package grupoExpo.API.Controllers.CategoriasTipoHabitacion;

import grupoExpo.API.Exceptions.CategoriasTipoHabitacion.ExcepcionCategoriaTipoHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.CategoriasTipoHabitacion.ExcepcionDatosDuplicadosCategoriaTipoHabitacion;
import grupoExpo.API.Models.DTO.CategoriasTipoHabitacionDTO;
import grupoExpo.API.Services.CategoriasTipoHabitacion.CategoriasTipoHabitacionService;
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
public class CategoriasTipoHabitacionController {

    @Autowired
    private CategoriasTipoHabitacionService acceso;

    //Paginación con datos
    @GetMapping("/consultarCategoriasTipoHabitacion")
    private ResponseEntity<Page<CategoriasTipoHabitacionDTO>> datosCategoriasTipoHabitacion(
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
        Page<CategoriasTipoHabitacionDTO> categoriasTipoHabitacion = acceso.getAllCategoriasTipoHabitacion(page, size);
        if (categoriasTipoHabitacion == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay categorias de tipos de habitaciones registradas"
            ));
        }
        return ResponseEntity.ok(categoriasTipoHabitacion);
    }

    //Insertar Datos
    @PostMapping("/registrarCategoriasTipoHabitacion")
    public ResponseEntity<?> nuevaCategoriaTipoHabitacion(@Valid @RequestBody CategoriasTipoHabitacionDTO json, HttpServletRequest request){
        try {
            CategoriasTipoHabitacionDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar categoriaTipoHabitacion",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarCategoriasTipoHabitacion/{id}")
    public ResponseEntity<?> modificarCategoriaTipoHabitacion(
            @PathVariable String id,
            @Valid @RequestBody CategoriasTipoHabitacionDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarCategoriaTipoHabitacion" que esta en el service
            CategoriasTipoHabitacionDTO dto = acceso.actualizarCategoriaTipoHabitacion(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionCategoriaTipoHabitacionNoEncontrada e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosCategoriaTipoHabitacion e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarCategoriasTipoHabitacion/{id}")
    public ResponseEntity<?> eliminarCategoriaTipoHabitacion(@PathVariable String id){
        try{
            if(!acceso.eliminarCategoriaTipoHabitacion(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "CategoriaTipoHabitacion no encontrada")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "La categoriaTipoHabitacion no fue encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "CategoriaTipoHabitacion eliminada exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la categoriaTipoHabitacion",
                    "detail", e.getMessage()
            ));
        }
    }
}
