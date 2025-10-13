package grupoExpo.API.Controllers.TestEndpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestRoles {

    @GetMapping("/testClienteOnly")
    @PreAuthorize("hasRole('Cliente')")
    public ResponseEntity<?> clienteEndPoint(){
        return ResponseEntity.ok("Exito: Acceso de cliente");
    }

    @GetMapping("/testAdminOnly")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> adminEndPoint(){
        return ResponseEntity.ok("Exito: Acceso de Administrador");
    }

    @GetMapping("/testEmpleadoOnly")
    @PreAuthorize("hasRole('Empleado')")
    public ResponseEntity<?> EmpleadoEndPoint(){
        return ResponseEntity.ok("Exito: Acceso de Empleado");
    }
}
