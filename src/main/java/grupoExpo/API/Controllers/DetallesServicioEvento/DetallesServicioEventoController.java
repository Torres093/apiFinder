package grupoExpo.API.Controllers.DetallesServicioEvento;

import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDatosDuplicadosDetalleServicioEvento;
import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDetalleServicioEventoNoEncontrado;
import grupoExpo.API.Models.DTO.DetallesServicioEventoDTO;
import grupoExpo.API.Services.DetallesServicioEvento.DetallesServicioEventoService;
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
public class DetallesServicioEventoController {

    @Autowired
    private DetallesServicioEventoService acceso;

    @GetMapping("/consultarDetallesServicioEvento")
    public List<DetallesServicioEventoDTO> datosDetallesServicioEvento(){
        return acceso.getAllDetallesServicioEvento();
    }

    //Insertar Datos
    @PostMapping("/registrarDetallesServicioEvento")
    public ResponseEntity<?> nuevoDetalleServicioEvento(@Valid @RequestBody DetallesServicioEventoDTO json, HttpServletRequest request){
        try {
            DetallesServicioEventoDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el detalleServicioEvento",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarDetallesServicioEvento/{id}")
    public ResponseEntity<?> modificarDetalleServicioEvento(
            @PathVariable String id,
            @Valid @RequestBody DetallesServicioEventoDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarDetalleServicioEvento" que esta en el service
            DetallesServicioEventoDTO dto = acceso.actualizarDetalleServicioEvento(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionDetalleServicioEventoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosDetalleServicioEvento e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarDetallesServicioEvento/{id}")
    public ResponseEntity<?> eliminarDetalleServicioEvento(@PathVariable String id){
        try{
            if(!acceso.eliminarDetalleServicioEvento(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "DetalleServicioEvento no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El detalleServicioEvento no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "DetalleServicioEvento eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el detalleServicioEvento",
                    "detail", e.getMessage()
            ));
        }
    }
}
