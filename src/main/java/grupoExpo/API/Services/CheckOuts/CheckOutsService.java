package grupoExpo.API.Services.CheckOuts;

import grupoExpo.API.Entities.CheckOuts.CheckOutsEntity;
import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Exceptions.CheckOuts.ExcepcionCheckOutNoEncontrado;
import grupoExpo.API.Exceptions.CheckOuts.ExcepcionCheckOutNoRegistrado;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Models.DTO.CheckOutsDTO;
import grupoExpo.API.Repositories.CheckOuts.CheckOutsRepository;
import grupoExpo.API.Repositories.Empleados.EmpleadosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CheckOutsService {

    @Autowired
    private CheckOutsRepository repo;

    @Autowired
    private EmpleadosRepository repoEmpleados;

    public Page<CheckOutsDTO> getAllCheckOuts(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<CheckOutsEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirACheckOutDTO);
    }

    private CheckOutsDTO convertirACheckOutDTO(CheckOutsEntity checkOut) {
        CheckOutsDTO dto = new CheckOutsDTO();
        dto.setIdCheckOut(checkOut.getIdCheckOut());
        dto.setIdDetalle(checkOut.getDetalle().getIdDetalle());
        if (checkOut.getEmpleado() != null){
            dto.setNombreEmpleado(checkOut.getEmpleado().getNombreEmpleado());
            dto.setIdEmpleado(checkOut.getEmpleado().getIdEmpleado());
        }else{
            dto.setNombreEmpleado("Sin nombre de empleado asignado");
            dto.setIdEmpleado(null);
        }
        dto.setFechaYHoraCheckOut(checkOut.getFechaYHoraCheckOut());
        dto.setObservacionCheckOut(checkOut.getObservacionCheckOut());
        return dto;
    }

    public CheckOutsDTO insertarDatos(CheckOutsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{


            CheckOutsEntity entity = ConvertirAEntity(data);
            CheckOutsEntity checkOutGuardado = repo.save(entity);
            return convertirACheckOutDTO(checkOutGuardado);
        }catch (Exception e){
            log.error("Error al registrar el checkOut: " + e.getMessage());
            throw new ExcepcionCheckOutNoRegistrado("Error al registrar el checkOut.");
        }
    }

    private CheckOutsEntity ConvertirAEntity(CheckOutsDTO data) {
        CheckOutsEntity entity = new CheckOutsEntity();

        //Asignando DetalleReserva a entity de CheckOuts
        DetallesReservaEntity detalleReserva = new DetallesReservaEntity();
        detalleReserva.setIdDetalle(data.getIdDetalle());
        entity.setDetalle(detalleReserva);

        //Asignando Empleado a entity de CheckOuts
        if (data.getIdEmpleado() != null){
            EmpleadosEntity empleado = repoEmpleados.findById(data.getIdEmpleado())
                    .orElseThrow(()-> new ExcepcionEmpleadoNoEncontrado("ID del empleado no encontrado"));
            entity.setEmpleado(empleado);
        }

        //Asignando atributos de DTO a entity
        entity.setFechaYHoraCheckOut(data.getFechaYHoraCheckOut());
        entity.setObservacionCheckOut(data.getObservacionCheckOut());
        return entity;
    }

    public CheckOutsDTO actualizarCheckOut(String id, CheckOutsDTO json) {
        //1. Verificar la existencia del checkOut.
        CheckOutsEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionCheckOutNoEncontrado("checkOut no encontrado"));
        //2. Actualizar los campos

        //Asignando DetalleReserva a entity de CheckOuts
        DetallesReservaEntity detalleReserva = new DetallesReservaEntity();
        detalleReserva.setIdDetalle(json.getIdDetalle());
        existente.setDetalle(detalleReserva);

        //Asignando Empleado a entity de CheckOuts
        if (json.getIdEmpleado() != null){
            EmpleadosEntity empleado = repoEmpleados.findById(json.getIdEmpleado())
                    .orElseThrow(()-> new ExcepcionEmpleadoNoEncontrado("ID del empleado no encontrado"));
            existente.setEmpleado(empleado);
        }else{
            existente.setEmpleado(null);
        }

        //Asignando atributos de DTO a entity
        existente.setFechaYHoraCheckOut(json.getFechaYHoraCheckOut());
        existente.setObservacionCheckOut(json.getObservacionCheckOut());

        //3. Guardar los cambios
        CheckOutsEntity checkOutActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirACheckOutDTO(checkOutActualizado);
    }

    public boolean eliminarCheckOut(String id) {
        try {
            //1. Validar existencia del checkOut
            CheckOutsEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el checkOut, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el checkOut con ID: " + id + " para eliminar. ", 1);
        }
    }
}
