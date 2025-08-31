package grupoExpo.API.Controllers.DetallesServicioPlato;

import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDatosDuplicadosDetalleServicioEvento;
import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDetalleServicioEventoNoEncontrado;
import grupoExpo.API.Exceptions.DetallesServicioPlato.ExcepcionDatosDuplicadosDetalleServicioPlato;
import grupoExpo.API.Exceptions.DetallesServicioPlato.ExcepcionDetalleServicioPlatoNoEncontrado;
import grupoExpo.API.Models.DTO.DetallesServicioEventoDTO;
import grupoExpo.API.Models.DTO.DetallesServicioPlatoDTO;
import grupoExpo.API.Services.DetallesServicioPlato.DetallesServicioPlatoService;
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
public class DetallesServicioPlatoController {

    @Autowired
    private DetallesServicioPlatoService acceso;

    @GetMapping("/consultarDetallesServicioPlato")
    public List<DetallesServicioPlatoDTO> datosDetallesServicioPlato(){
        return acceso.getAllDetallesServicioPlato();
    }

    //Insertar Datos
    @PostMapping("/registrarDetallesServicioPlato")
    public ResponseEntity<?> nuevoDetalleServicioPlato(@Valid @RequestBody DetallesServicioPlatoDTO json, HttpServletRequest request){
        try {
            DetallesServicioPlatoDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el detalleServicioPlato",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarDetallesServicioPlato/{id}")
    public ResponseEntity<?> modificarDetalleServicioPlato(
            @PathVariable String id,
            @Valid @RequestBody DetallesServicioPlatoDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarDetalleServicioPlato" que esta en el service
            DetallesServicioPlatoDTO dto = acceso.actualizarDetalleServicioPlato(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionDetalleServicioPlatoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosDetalleServicioPlato e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarDetallesServicioPlato/{id}")
    public ResponseEntity<?> eliminarDetalleServicioPlato(@PathVariable String id){
        try{
            if(!acceso.eliminarDetalleServicioPlato(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "DetalleServicioPlato no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El detalleServicioPlato no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "DetalleServicioPlato eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el detalleServicioPlato",
                    "detail", e.getMessage()
            ));
        }
    }
}
