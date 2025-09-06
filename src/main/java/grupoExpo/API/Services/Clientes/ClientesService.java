package grupoExpo.API.Services.Clientes;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoEncontrado;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoRegistrado;
import grupoExpo.API.Models.DTO.ClientesDTO;
import grupoExpo.API.Repositories.Clientes.ClientesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientesService {

    @Autowired
    private ClientesRepository repo;

    public Page<ClientesDTO> getAllClientes(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<ClientesEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAClienteDTO);
    }

    private ClientesDTO convertirAClienteDTO(ClientesEntity cliente){
        ClientesDTO dto = new ClientesDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setIdUsuario(cliente.getUsuario().getIdUsuario());
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
            throw new ExcepcionClienteNoRegistrado("Error al registrar el cliente.");
        }
    }

    private ClientesEntity ConvertirAEntity(ClientesDTO data) {
        ClientesEntity entity = new ClientesEntity();

        //Asignando usuario a entity de Clientes
        UsuariosEntity usuario = new UsuariosEntity();
        usuario.setIdUsuario(data.getIdUsuario());
        entity.setUsuario(usuario);

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

        //Asignando usuario a entity de Clientes
        UsuariosEntity usuario = new UsuariosEntity();
        usuario.setIdUsuario(json.getIdUsuario());
        existente.setUsuario(usuario);

        //Asignando atributos de DTO a entity
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
            throw new EmptyResultDataAccessException("No se encontro el cliente con ID: " + id + " para eliminar. ", 1);
        }
    }
}
