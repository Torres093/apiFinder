package grupoExpo.API.Services.CheckIns;

import grupoExpo.API.Entities.CheckIns.CheckInsEntity;
import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Exceptions.CheckIns.ExcepcionCheckInNoEncontrado;
import grupoExpo.API.Exceptions.CheckIns.ExcepcionCheckInNoRegistrado;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Models.DTO.CheckInsDTO;
import grupoExpo.API.Repositories.CheckIns.CheckInsRepository;
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
public class CheckInsService {

    @Autowired
    private CheckInsRepository repo;

    @Autowired
    private EmpleadosRepository repoEmpleados;

    public Page<CheckInsDTO> getAllCheckIns(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<CheckInsEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirACheckInDTO);
    }

    private CheckInsDTO convertirACheckInDTO(CheckInsEntity checkIn) {
        CheckInsDTO dto = new CheckInsDTO();
        dto.setIdCheckIn(checkIn.getIdCheckIn());
        dto.setIdDetalle(checkIn.getDetalle().getIdDetalle());
        if (checkIn.getEmpleado() != null){
            dto.setNombreEmpleado(checkIn.getEmpleado().getNombreEmpleado());
            dto.setIdEmpleado(checkIn.getEmpleado().getIdEmpleado());
        }else{
            dto.setNombreEmpleado("Sin nombre de empleado asignado");
            dto.setIdEmpleado(null);
        }
        dto.setFechaYHoraCheckIn(checkIn.getFechaYHoraCheckIn());
        dto.setObservacionCheckIn(checkIn.getObservacionCheckIn());
        return dto;
    }

    public CheckInsDTO insertarDatos(CheckInsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{


            CheckInsEntity entity = ConvertirAEntity(data);
            CheckInsEntity checkInGuardado = repo.save(entity);
            return convertirACheckInDTO(checkInGuardado);
        }catch (Exception e){
            log.error("Error al registrar el checkIn: " + e.getMessage());
            throw new ExcepcionCheckInNoRegistrado("Error al registrar el checkIn.");
        }
    }

    private CheckInsEntity ConvertirAEntity(CheckInsDTO data) {
        CheckInsEntity entity = new CheckInsEntity();

        //Asignando DetalleReserva a entity de CheckIns
        DetallesReservaEntity detalleReserva = new DetallesReservaEntity();
        detalleReserva.setIdDetalle(data.getIdDetalle());
        entity.setDetalle(detalleReserva);

        //Asignando Empleado a entity de CheckIns
        if (data.getIdEmpleado() != null){
            EmpleadosEntity empleado = repoEmpleados.findById(data.getIdEmpleado())
                    .orElseThrow(()-> new ExcepcionEmpleadoNoEncontrado("ID del empleado no encontrado"));
            entity.setEmpleado(empleado);
        }

        //Asignando atributos de DTO a entity
        entity.setFechaYHoraCheckIn(data.getFechaYHoraCheckIn());
        entity.setObservacionCheckIn(data.getObservacionCheckIn());
        return entity;
    }

    public CheckInsDTO actualizarCheckIn(String id, CheckInsDTO json) {
        //1. Verificar la existencia del checkIn.
        CheckInsEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionCheckInNoEncontrado("checkIn no encontrado"));
        //2. Actualizar los campos

        //Asignando DetalleReserva a entity de CheckIns
        DetallesReservaEntity detalleReserva = new DetallesReservaEntity();
        detalleReserva.setIdDetalle(json.getIdDetalle());
        existente.setDetalle(detalleReserva);

        //Asignando Empleado a entity de CheckIns
        if (json.getIdEmpleado() != null){
            EmpleadosEntity empleado = repoEmpleados.findById(json.getIdEmpleado())
                    .orElseThrow(()-> new ExcepcionEmpleadoNoEncontrado("ID del empleado no encontrado"));
            existente.setEmpleado(empleado);
        }else{
            existente.setEmpleado(null);
        }

        //Asignando atributos de DTO a entity
        existente.setFechaYHoraCheckIn(json.getFechaYHoraCheckIn());
        existente.setObservacionCheckIn(json.getObservacionCheckIn());

        //3. Guardar los cambios
        CheckInsEntity checkInActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirACheckInDTO(checkInActualizado);
    }

    public boolean eliminarCheckIn(String id) {
        try {
            //1. Validar existencia del checkIn
            CheckInsEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el checkIn, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el checkIn con ID: " + id + " para eliminar. ", 1);
        }
    }
}
