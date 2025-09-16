package grupoExpo.API.Services.Reservas;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Entities.EstadosReserva.EstadosReservaEntity;
import grupoExpo.API.Entities.MetodosPago.MetodosPagoEntity;
import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Exceptions.Clientes.ExcepcionClienteNoEncontrado;
import grupoExpo.API.Exceptions.EstadosReserva.ExcepcionEstadoReservaNoEncontrado;
import grupoExpo.API.Exceptions.MetodosPago.ExcepcionMetodoPagoNoEncontrado;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoEncontrada;
import grupoExpo.API.Exceptions.Reservas.ExcepcionReservaNoRegistrada;
import grupoExpo.API.Models.DTO.ReservasDTO;
import grupoExpo.API.Repositories.Clientes.ClientesRepository;
import grupoExpo.API.Repositories.EstadosReserva.EstadosReservaRepository;
import grupoExpo.API.Repositories.MetodosPago.MetodosPagoRepository;
import grupoExpo.API.Repositories.Reservas.ReservasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class ReservasService {

    @Autowired
    private ReservasRepository repo;

    @Autowired
    private ClientesRepository repoClientes;

    @Autowired
    private EstadosReservaRepository repoEstadosReserva;

    @Autowired
    private MetodosPagoRepository repoMetodosPago;

    public Page<ReservasDTO> getAllReservas(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<ReservasEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAReservasDTO);
    }

    private ReservasDTO convertirAReservasDTO(ReservasEntity reservas) {
        ReservasDTO dto = new ReservasDTO();
        dto.setIdReserva(reservas.getIdReserva());
        if (reservas.getCliente() != null){
            dto.setNombreCliente(reservas.getCliente().getNombreCliente());
            dto.setIdCliente(reservas.getCliente().getIdCliente());
        }else{
            dto.setNombreCliente("Sin nombre de cliente asignado");
            dto.setIdCliente(null);
        }
        if (reservas.getEstadoReserva() != null){
            dto.setNombreEstadoReserva(reservas.getEstadoReserva().getNombreEstadoReserva());
            dto.setIdEstadoReserva(reservas.getEstadoReserva().getIdEstadoReserva());
        }else{
            dto.setNombreEstadoReserva("Sin nombre del estado de reserva asignado");
            dto.setIdEstadoReserva(null);
        }
        if (reservas.getMetodoPago() != null){
            dto.setNombreMetodoPago(reservas.getMetodoPago().getNombreMetodoPago());
            dto.setIdMetodoPago(reservas.getMetodoPago().getIdMetodoPago());
        }else{
            dto.setNombreMetodoPago("Sin nombre del metodo de pago asignado");
            dto.setIdMetodoPago(null);
        }
        dto.setFechaReserva(reservas.getFechaReserva());
        dto.setPrecioTotalReserva(reservas.getPrecioTotalReserva().doubleValue());
        return dto;
    }

    public ReservasDTO insertarDatos(ReservasDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            ReservasEntity entity = ConvertirAEntity(data);
            ReservasEntity ReservaGuardada = repo.save(entity);
            return convertirAReservasDTO(ReservaGuardada);
        }catch (Exception e){
            log.error("Error al registrar la reserva: " + e.getMessage());
            throw new ExcepcionReservaNoRegistrada("Error al registrar la reserva.");
        }
    }

    private ReservasEntity ConvertirAEntity(ReservasDTO data) {
        ReservasEntity entity = new ReservasEntity();

        //Asignando Cliente a entity de Reservas
        if (data.getIdCliente() != null){
            ClientesEntity cliente = repoClientes.findById(data.getIdCliente())
                    .orElseThrow(()-> new ExcepcionClienteNoEncontrado("ID del cliente no encontrado"));
            entity.setCliente(cliente);
        }

        //Asignando EstadoReserva a entity de Reservas
        if (data.getIdEstadoReserva() != null){
            EstadosReservaEntity estadoReserva = repoEstadosReserva.findById(data.getIdEstadoReserva())
                    .orElseThrow(()-> new ExcepcionEstadoReservaNoEncontrado("ID del estado de la reserva no encontrado"));
            entity.setEstadoReserva(estadoReserva);
        }

        //Asignando MetodoPago a entity de Reservas
        if (data.getIdMetodoPago() != null){
            MetodosPagoEntity metodoPago = repoMetodosPago.findById(data.getIdMetodoPago())
                    .orElseThrow(()-> new ExcepcionMetodoPagoNoEncontrado("ID del metodo de pago no encontrado"));
            entity.setMetodoPago(metodoPago);
        }

        //Asignando atributos de DTO a entity
        entity.setFechaReserva(data.getFechaReserva());
        entity.setPrecioTotalReserva(BigDecimal.valueOf(data.getPrecioTotalReserva()));
        return entity;
    }

    public ReservasDTO actualizarReserva(String id, ReservasDTO json) {
        //1. Verificar la existencia de la reserva.
        ReservasEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionReservaNoEncontrada("Reserva no encontrada"));
        //2. Actualizar los campos

        //Asignando Cliente a entity de Reservas
        if (json.getIdCliente() != null){
            ClientesEntity cliente = repoClientes.findById(json.getIdCliente())
                    .orElseThrow(()-> new ExcepcionClienteNoEncontrado("ID del cliente no encontrado"));
            existente.setCliente(cliente);
        }

        //Asignando EstadoReserva a entity de Reservas
        if (json.getIdEstadoReserva() != null){
            EstadosReservaEntity estadoReserva = repoEstadosReserva.findById(json.getIdEstadoReserva())
                    .orElseThrow(()-> new ExcepcionEstadoReservaNoEncontrado("ID del estado de la reserva no encontrado"));
            existente.setEstadoReserva(estadoReserva);
        }

        //Asignando MetodoPago a entity de Reservas
        if (json.getIdMetodoPago() != null){
            MetodosPagoEntity metodoPago = repoMetodosPago.findById(json.getIdMetodoPago())
                    .orElseThrow(()-> new ExcepcionMetodoPagoNoEncontrado("ID del metodo de pago no encontrado"));
            existente.setMetodoPago(metodoPago);
        }

        //Asignando atributos de DTO a entity
        existente.setFechaReserva(json.getFechaReserva());
        existente.setPrecioTotalReserva(BigDecimal.valueOf(json.getPrecioTotalReserva()));

        //3. Guardar los cambios
        ReservasEntity reservaActualizada = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAReservasDTO(reservaActualizada);
    }

    public boolean eliminarReserva(String id) {
        try {
            //1. Validar existencia de la reserva
            ReservasEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar la reserva, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro la reserva con ID: " + id + " para eliminar. ", 1);
        }
    }
}
