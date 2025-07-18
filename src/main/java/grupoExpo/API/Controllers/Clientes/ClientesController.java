package grupoExpo.API.Controllers.Clientes;

import grupoExpo.API.Models.DTO.ClientesDTO;
import grupoExpo.API.Models.apiResponse.ApiResponse;
import grupoExpo.API.Services.Clientes.ClientesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientesController {

    @Autowired
    private ClientesService acceso;

    @GetMapping("/clientes")
    public List<ClientesDTO> datosClientes(){
        return acceso.getAllClientes();
    }

    @PostMapping("/cliente")
    public /*ApiResponse<Void>*/ void InsertarCliente(@Valid ClientesDTO cliente)
    {
        //return acceso.insertarCliente(cliente);
        acceso.InsertarCliente(cliente);
    }

}
