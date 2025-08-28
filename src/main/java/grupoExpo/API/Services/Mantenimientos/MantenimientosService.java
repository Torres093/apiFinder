package grupoExpo.API.Services.Mantenimientos;

import grupoExpo.API.Entities.Cargos.CargosEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Mantenimientos.MantenimientosEntity;
import grupoExpo.API.Entities.TiposMantenimiento.TiposMantenimientoEntity;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoRegistrado;
import grupoExpo.API.Exceptions.Mantenimientos.ExcepcionMantenimientoNoEncontrado;
import grupoExpo.API.Exceptions.Mantenimientos.ExcepcionMantenimientoNoRegistrado;
import grupoExpo.API.Models.DTO.MantenimientosDTO;
import grupoExpo.API.Repositories.Mantenimientos.MantenimientosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MantenimientosService {

    @Autowired
    private MantenimientosRepository repo;

    public List<MantenimientosDTO> getAllMantenimientos() {
        List<MantenimientosEntity> mantenimientos = repo.findAll();
        return mantenimientos.stream()
                .map(this::convertirAMantenimientoDTO)
                .collect(Collectors.toList());
    }

    private MantenimientosDTO convertirAMantenimientoDTO(MantenimientosEntity mantenimiento) {
        MantenimientosDTO dto = new MantenimientosDTO();
        dto.setIdMantenimiento(mantenimiento.getIdMantenimiento());
        dto.setIdHabitacion(mantenimiento.getHabitacion().getIdHabitacion());
        dto.setIdEmpleado(mantenimiento.getEmpleado().getIdEmpleado());
        dto.setIdTipoMantenimiento(mantenimiento.getTipoMantenimiento().getIdTipoMantenimiento());
        dto.setFechaMantenimiento(mantenimiento.getFechaMantenimiento());
        dto.setHoraInicioMantenimiento(mantenimiento.getHoraInicioMantenimiento());
        dto.setHoraFinMantenimiento(mantenimiento.getHoraFinMantenimiento());
        dto.setObservacionMantenimiento(mantenimiento.getObservacionMantenimiento());
        return dto;
    }

    public MantenimientosDTO insertarDatos(MantenimientosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            MantenimientosEntity entity = ConvertirAEntity(data);
            MantenimientosEntity mantenimientoGuardado = repo.save(entity);
            return convertirAMantenimientoDTO(mantenimientoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el mantenimiento: " + e.getMessage());
            throw new ExcepcionMantenimientoNoRegistrado("Error al registrar el mantenimiento.");
        }
    }

    private MantenimientosEntity ConvertirAEntity(MantenimientosDTO data) {
        MantenimientosEntity entity = new MantenimientosEntity();

        //Asignando habitacion a entity de Mantenimientos
        HabitacionesEntity habitacion = new HabitacionesEntity();
        habitacion.setIdHabitacion(data.getIdHabitacion());
        entity.setHabitacion(habitacion);

        //Asignando Empleado a entity de Mantenimientos
        EmpleadosEntity empleado = new EmpleadosEntity();
        empleado.setIdEmpleado(data.getIdEmpleado());
        entity.setEmpleado(empleado);

        //Asignando TipoMantenimiento a entity de Mantenimientos
        TiposMantenimientoEntity tipoMantenimiento = new TiposMantenimientoEntity();
        tipoMantenimiento.setIdTipoMantenimiento(data.getIdTipoMantenimiento());
        entity.setTipoMantenimiento(tipoMantenimiento);

        //Asignando atributos de DTO a entity
        entity.setFechaMantenimiento(data.getFechaMantenimiento());
        entity.setHoraInicioMantenimiento(data.getHoraInicioMantenimiento());
        entity.setHoraFinMantenimiento(data.getHoraFinMantenimiento());
        entity.setObservacionMantenimiento(data.getObservacionMantenimiento());
        return entity;
    }

    public MantenimientosDTO actualizarMantenimiento(String id, MantenimientosDTO json) {
        //1. Verificar la existencia del mantenimiento.
        MantenimientosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionMantenimientoNoEncontrado("Mantenimiento no encontrado"));

        //2. Actualizar los campos

        //Asignando habitacion a entity de Mantenimientos
        HabitacionesEntity habitacion = new HabitacionesEntity();
        habitacion.setIdHabitacion(json.getIdHabitacion());
        existente.setHabitacion(habitacion);

        //Asignando Empleado a entity de Mantenimientos
        EmpleadosEntity empleado = new EmpleadosEntity();
        empleado.setIdEmpleado(json.getIdEmpleado());
        existente.setEmpleado(empleado);

        //Asignando TipoMantenimiento a entity de Mantenimientos
        TiposMantenimientoEntity tipoMantenimiento = new TiposMantenimientoEntity();
        tipoMantenimiento.setIdTipoMantenimiento(json.getIdTipoMantenimiento());
        existente.setTipoMantenimiento(tipoMantenimiento);

        //Asignando atributos de DTO a entity
        existente.setFechaMantenimiento(json.getFechaMantenimiento());
        existente.setHoraInicioMantenimiento(json.getHoraInicioMantenimiento());
        existente.setHoraFinMantenimiento(json.getHoraFinMantenimiento());
        existente.setObservacionMantenimiento(json.getObservacionMantenimiento());

        //3. Guardar los cambios
        MantenimientosEntity mantenimientoActualizado = repo.save(existente);

        //4. Convertir los datos a DTO y retornarlos
        return convertirAMantenimientoDTO(mantenimientoActualizado);
    }

    public boolean eliminarMantenimiento(String id) {
        try {
            //1. Validar existencia del mantenimiento
            MantenimientosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el mantenimiento, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el mantenimiento con ID: " + id + " para eliminar. ", 1);
        }
    }
}
