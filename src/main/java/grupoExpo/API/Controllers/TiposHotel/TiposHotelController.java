package grupoExpo.API.Controllers.TiposHotel;

import grupoExpo.API.Exceptions.TiposHotel.ExcepcionDatosDuplicadosTipoHotel;
import grupoExpo.API.Exceptions.TiposHotel.ExcepcionTipoHotelNoEncontrado;
import grupoExpo.API.Models.DTO.TiposHotelDTO;
import grupoExpo.API.Services.TiposHotel.TiposHotelService;
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
public class TiposHotelController {

    @Autowired
    private TiposHotelService acceso;

    //Paginación con datos
    @GetMapping("/consultarTiposHotel")
    private ResponseEntity<Page<TiposHotelDTO>> datosTiposHotel(
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
        Page<TiposHotelDTO> tiposHotel = acceso.getAllTiposHotel(page, size);
        if (tiposHotel == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay tipos de hotel registrados"
            ));
        }
        return ResponseEntity.ok(tiposHotel);
    }

    //Insertar Datos
    @PostMapping("/registrarTiposHotel")
    public ResponseEntity<?> nuevoTipoHotel(@Valid @RequestBody TiposHotelDTO json, HttpServletRequest request){
        try {
            TiposHotelDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el tipo de hotel",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarTiposHotel/{id}")
    public ResponseEntity<?> modificarTipoHotel(
            @PathVariable String id,
            @Valid @RequestBody TiposHotelDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarTipoHotel" que esta en el service
            TiposHotelDTO dto = acceso.actualizarTipoHotel(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionTipoHotelNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosTipoHotel e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarTiposHotel/{id}")
    public ResponseEntity<?> eliminarTipoHotel(@PathVariable String id){
        try{
            if(!acceso.eliminarTipoHotel(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Tipo no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El tipo de hotel no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Tipo de hotel eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el tipo de hotel",
                    "detail", e.getMessage()
            ));
        }
    }
}
