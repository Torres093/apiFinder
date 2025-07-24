package grupoExpo.API.Services.Clientes;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoEncontrado;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoRegistrado;
import grupoExpo.API.Models.DTO.ClientesDTO;
import grupoExpo.API.Models.apiResponse.ApiResponse;
import grupoExpo.API.Repositories.Clientes.ClientesRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
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

    public ClientesDTO insertarDatos(ClientesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            ClientesEntity entity = ConvertirAEntity(data);
            ClientesEntity usuarioGuardado = repo.save(entity);
            return convertirAClienteDTO(usuarioGuardado);
        }catch (Exception e){
            log.error("Error al registrar el cliente: " + e.getMessage());
            throw new ExcepcionClienteNoRegistrado("Error al registrar el usuario.");
        }
    }

    /**
     *
     * @param data
     * @return
     */
    private ClientesEntity ConvertirAEntity(ClientesDTO data) {
        ClientesEntity entity = new ClientesEntity();

        entity.setIdUsuario(data.getIdUsuario());
        entity.setNombreCliente(data.getNombreCliente());
        entity.setApellidoCliente(data.getApellidoCliente());
        entity.setDuiCliente(data.getDuiCliente());
        entity.setNacimientoCliente(data.getNacimientoCliente());
        return entity;
    }


    public ClientesDTO actualizarCliente(String id, ClientesDTO json) {
        //1. Verificar la existencia del cliente.
        ClientesEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionClienteNoEncontrado("Cliente no enconrado"));
        //2. Actualizar los campos
        existente.setIdUsuario(json.getIdUsuario());
        existente.setNombreCliente(json.getNombreCliente());
        existente.setApellidoCliente(json.getApellidoCliente());
        existente.setDuiCliente(json.getDuiCliente());
        existente.setNacimientoCliente(json.getNacimientoCliente());
        //3. Guardar los cambios
        ClientesEntity clienteActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAClienteDTO(clienteActualizado);


    }

    public boolean eliminarCliente(String id) {
        try {
            //1. Validar existencia del cliente
            ClientesEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el cliente, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro cliente con ID: " + id + "para eliminar. ", 1);
        }
    }
}
