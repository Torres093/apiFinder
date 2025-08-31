package grupoExpo.API.Services.CheckIns;

import grupoExpo.API.Entities.CheckIns.CheckInsEntity;
import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Exceptions.CheckIns.ExcepcionCheckInNoEncontrado;
import grupoExpo.API.Exceptions.CheckIns.ExcepcionCheckInNoRegistrado;
import grupoExpo.API.Models.DTO.CheckInsDTO;
import grupoExpo.API.Repositories.CheckIns.CheckInsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckInsService {

    @Autowired
    private CheckInsRepository repo;

    public List<CheckInsDTO> getAllCheckIns() {
        List<CheckInsEntity> checkIns = repo.findAll();
        return checkIns.stream()
                .map(this::convertirACheckInDTO)
                .collect(Collectors.toList());
    }

    private CheckInsDTO convertirACheckInDTO(CheckInsEntity checkIn) {
        CheckInsDTO dto = new CheckInsDTO();
        dto.setIdCheckIn(checkIn.getIdCheckIn());
        dto.setIdDetalle(checkIn.getDetalle().getIdDetalle());
        dto.setIdEmpleado(checkIn.getEmpleado().getIdEmpleado());
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
        EmpleadosEntity empleado = new EmpleadosEntity();
        empleado.setIdEmpleado(data.getIdEmpleado());
        entity.setEmpleado(empleado);

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
        EmpleadosEntity empleado = new EmpleadosEntity();
        empleado.setIdEmpleado(json.getIdEmpleado());
        existente.setEmpleado(empleado);

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
