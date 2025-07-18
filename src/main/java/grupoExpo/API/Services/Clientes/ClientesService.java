package grupoExpo.API.Services.Clientes;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Models.DTO.ClientesDTO;
import grupoExpo.API.Models.apiResponse.ApiResponse;
import grupoExpo.API.Repositories.Clientes.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientesService {

    @Autowired
    private ClientesRepository repo;

    public List<ClientesDTO> getAllClientes(){
        List<ClientesEntity> clientes = repo.findAll();
        return clientes.stream()
                .map(this::convertirAClienteDTO)
                .collect(Collectors.toList());
    }

    public /*ApiResponse<Void>*/ void InsertarCliente(ClientesDTO cliente){
        var entityCliente = this.convertirAClientesEntity(cliente);
        repo.save(entityCliente);
        /*ApiResponse<Void> apiResponse = new ApiResponse<Void>(
                "Éxito",
                "Cliente creado correctamente",
                null
        );
        return apiResponse;*/
    }

    private ClientesDTO convertirAClienteDTO(ClientesEntity cliente){
        ClientesDTO dto = new ClientesDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setIdUsuario(cliente.getIdUsuario());
        dto.setNombreCliente(cliente.getNombreCliente());
        dto.setApellidoCliente(cliente.getApellidoCliente());
        dto.setDuiCliente(cliente.getDuiCliente());
        dto.setNacimientoCliente(cliente.getNacimientoCliente());
        return dto;
    }

    private ClientesEntity convertirAClientesEntity(ClientesDTO cliente){
        ClientesEntity entity = new ClientesEntity();
        entity.setIdCliente(cliente.getIdCliente());
        entity.setIdUsuario(cliente.getIdUsuario());
        entity.setNombreCliente(cliente.getNombreCliente());
        entity.setApellidoCliente(cliente.getApellidoCliente());
        entity.setDuiCliente(cliente.getDuiCliente());
        entity.setNacimientoCliente(cliente.getNacimientoCliente());
        return entity;
    }

}
