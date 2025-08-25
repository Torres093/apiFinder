package grupoExpo.API.Controllers.Empleados;

import grupoExpo.API.Exceptions.Empleados.ExcepcionDatosDuplicadosEmpleado;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Models.DTO.EmpleadosDTO;
import grupoExpo.API.Services.Empleados.EmpleadosService;
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
public class EmpleadosController {

    @Autowired
    private EmpleadosService acceso;

    @GetMapping("/consultarEmpleados")
    public List<EmpleadosDTO> datosEmpleados(){
        return acceso.getAllEmpleados();
    }

    //Insertar Datos
    @PostMapping("/registrarEmpleados")
    public ResponseEntity<?> nuevoEmpleado(@Valid @RequestBody EmpleadosDTO json, HttpServletRequest request){
        try {
            EmpleadosDTO respuesta = acceso.insertarDatos(json);
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
                            "message", "Error no controlado al registrar el empleado",
                            "detail", e.getMessage()
                    ));
        }
    }

    //Actualizar datos
    @PutMapping("actualizarEmpleados/{id}")
    public ResponseEntity<?> modificarEmpleado(
            @PathVariable String id,
            @Valid @RequestBody EmpleadosDTO json,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return  ResponseEntity.badRequest().body(errores);
        }
        try {
            //Creamos un objeto de tipo DTO y se invoca en el metodo "actualizarEmpleado" que esta en el service
            EmpleadosDTO dto = acceso.actualizarEmpleado(id, json);
            //La API retorna una respuesta la cual contendra los datos en formato DTO
            return ResponseEntity.ok(dto);
        }catch (ExcepcionEmpleadoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
        catch (ExcepcionDatosDuplicadosEmpleado e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("Error", "Datos duplicados", "Campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarEmpleados/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable String id){
        try{
            if(!acceso.eliminarEmpleado(id)){
                //Error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Mensaje: error", "Empleado no encontrado")
                        .body(Map.of("Error", "Not found",
                                "Mensaje", "El empleado no fue encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            //Exitoso
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Empleado eliminado exitosamente"
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el empleado",
                    "detail", e.getMessage()
            ));
        }
    }
}
