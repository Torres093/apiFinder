package grupoExpo.API.Controllers.MetodosPago;

import grupoExpo.API.Models.DTO.MetodosPagoDTO;
import grupoExpo.API.Services.MetodosPago.MetodosPagoService;
import grupoExpo.API.Exceptions.MetodosPago.ExcepcionDatosDuplicadosMetodoPago;
import grupoExpo.API.Exceptions.MetodosPago.ExcepcionMetodoPagoNoEncontrado;
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
public class MetodosPagoController {

    @Autowired
    private MetodosPagoService acceso;

    @GetMapping("/consultarMetodosPago")
    public List<MetodosPagoDTO> datosMetodosPago(){
        return acceso.getAllMetodosPago();
    }

    //Insertar Datos
    @PostMapping("/registrarMetodosPago")
    public ResponseEntity<?> nuevoMetodoPago(@Valid @RequestBody MetodosPagoDTO json, HttpServletRequest request){
        try {
            MetodosPagoDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el método de pago",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarMetodosPago/{id}")
    public ResponseEntity<?> modificarMetodoPago(
            @PathVariable String id,
            @Valid @RequestBody MetodosPagoDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de metodoPagoDTO y se invoca en el metodo "actualizarMetodoPago" que esta en el service
            MetodosPagoDTO dto = acceso.actualizarMetodoPago(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionMetodoPagoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosMetodoPago e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarMetodosPago/{id}")
    public ResponseEntity<?> eliminarMetodoPago(@PathVariable String id){
        try{
            if(!acceso.eliminarMetodoPago(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Método de pago no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El método de pago no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Método de pago eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el método de pago",
                    "detail", e.getMessage()
            ));
        }
    }
}
