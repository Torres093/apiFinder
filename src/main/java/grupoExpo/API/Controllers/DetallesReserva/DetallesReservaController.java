package grupoExpo.API.Controllers.DetallesReserva;

import grupoExpo.API.Exceptions.DetallesReserva.ExcepcionDatosDuplicadosDetalleReserva;
import grupoExpo.API.Exceptions.DetallesReserva.ExcepcionDetalleReservaNoEncontrado;
import grupoExpo.API.Models.DTO.DetallesReservaDTO;
import grupoExpo.API.Services.DetallesReserva.DetallesReservaService;
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
public class DetallesReservaController {

    @Autowired
    private DetallesReservaService acceso;

    //Paginación con datos
    @GetMapping("/consultarDetallesReserva")
    private ResponseEntity<Page<DetallesReservaDTO>> datosDetallesReserva(
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
        Page<DetallesReservaDTO> detallesReserva = acceso.getAllDetallesReserva(page, size);
        if (detallesReserva == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay detalles de la reserva registrados"
            ));
        }
        return ResponseEntity.ok(detallesReserva);
    }

    //Insertar Datos
    @PostMapping("/registrarDetallesReserva")
    public ResponseEntity<?> nuevoDetalleReserva(@Valid @RequestBody DetallesReservaDTO json, HttpServletRequest request){
        try {
            DetallesReservaDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el detalleReserva",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarDetallesReserva/{id}")
    public ResponseEntity<?> modificarDetalleReserva(
            @PathVariable String id,
            @Valid @RequestBody DetallesReservaDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarDetalleReserva" que esta en el service
            DetallesReservaDTO dto = acceso.actualizarDetalleReserva(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionDetalleReservaNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosDetalleReserva e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarDetallesReserva/{id}")
    public ResponseEntity<?> eliminarDetalleReserva(@PathVariable String id){
        try{
            if(!acceso.eliminarDetalleReserva(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "DetalleReserva no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El detalleReserva no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "DetalleReserva eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el detalleReserva",
                    "detail", e.getMessage()
            ));
        }
    }
}
