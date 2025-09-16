package grupoExpo.API.Services.Mantenimientos;

import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Mantenimientos.MantenimientosEntity;
import grupoExpo.API.Entities.TiposMantenimiento.TiposMantenimientoEntity;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Exceptions.Habitaciones.ExcepcionHabitacionNoEncontrada;
import grupoExpo.API.Exceptions.Mantenimientos.ExcepcionMantenimientoNoEncontrado;
import grupoExpo.API.Exceptions.Mantenimientos.ExcepcionMantenimientoNoRegistrado;
import grupoExpo.API.Exceptions.TiposMantenimiento.ExcepcionTipoMantenimientoNoEncontrado;
import grupoExpo.API.Models.DTO.MantenimientosDTO;
import grupoExpo.API.Repositories.Empleados.EmpleadosRepository;
import grupoExpo.API.Repositories.Habitaciones.HabitacionesRepository;
import grupoExpo.API.Repositories.Mantenimientos.MantenimientosRepository;
import grupoExpo.API.Repositories.TiposMantenimiento.TiposMantenimientoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MantenimientosService {

    @Autowired
    private MantenimientosRepository repo;

    @Autowired
    private HabitacionesRepository repoHabitaciones;

    @Autowired
    private EmpleadosRepository repoEmpleados;

    @Autowired
    private TiposMantenimientoRepository repoTiposMantenimiento;

    public Page<MantenimientosDTO> getAllMantenimientos(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<MantenimientosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAMantenimientoDTO);
    }

    private MantenimientosDTO convertirAMantenimientoDTO(MantenimientosEntity mantenimiento) {
        MantenimientosDTO dto = new MantenimientosDTO();
        dto.setIdMantenimiento(mantenimiento.getIdMantenimiento());
        if (mantenimiento.getHabitacion() != null){
            dto.setNumeroHabitacion(mantenimiento.getHabitacion().getNumeroHabitacion());
            dto.setIdHabitacion(mantenimiento.getHabitacion().getIdHabitacion());
        }else{
            dto.setNumeroHabitacion(-1);
            dto.setIdHabitacion(null);
        }
        if (mantenimiento.getEmpleado() != null){
            dto.setNombreEmpleado(mantenimiento.getEmpleado().getNombreEmpleado());
            dto.setIdEmpleado(mantenimiento.getEmpleado().getIdEmpleado());
        }else{
            dto.setNombreEmpleado("Sin nombre de empleado asignado");
            dto.setIdEmpleado(null);
        }
        if (mantenimiento.getTipoMantenimiento() != null){
            dto.setNombreTipoMantenimiento(mantenimiento.getTipoMantenimiento().getNombreTipoMantenimiento());
            dto.setIdTipoMantenimiento(mantenimiento.getTipoMantenimiento().getIdTipoMantenimiento());
        }else{
            dto.setNombreTipoMantenimiento("Sin nombre de tipo de mantenimiento asignado");
            dto.setIdTipoMantenimiento(null);
        }
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
        if (data.getIdHabitacion() != null){
            HabitacionesEntity habitacion = repoHabitaciones.findById(data.getIdHabitacion())
                    .orElseThrow(()-> new ExcepcionHabitacionNoEncontrada("ID de habitacion no encontrada"));
            entity.setHabitacion(habitacion);
        }

        //Asignando Empleado a entity de Mantenimientos
        if (data.getIdEmpleado() != null){
            EmpleadosEntity empleado = repoEmpleados.findById(data.getIdEmpleado())
                    .orElseThrow(()-> new ExcepcionEmpleadoNoEncontrado("ID de empleado no encontrado"));
            entity.setEmpleado(empleado);
        }

        //Asignando TipoMantenimiento a entity de Mantenimientos
        if (data.getIdTipoMantenimiento() != null){
            TiposMantenimientoEntity tipoMantenimiento = repoTiposMantenimiento.findById(data.getIdTipoMantenimiento())
                    .orElseThrow(()-> new ExcepcionTipoMantenimientoNoEncontrado("ID del tipo de mantenimiento no encontrado"));
            entity.setTipoMantenimiento(tipoMantenimiento);
        }

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
        if (json.getIdHabitacion() != null){
            HabitacionesEntity habitacion = repoHabitaciones.findById(json.getIdHabitacion())
                    .orElseThrow(()-> new ExcepcionHabitacionNoEncontrada("ID de habitacion no encontrada"));
            existente.setHabitacion(habitacion);
        }

        //Asignando Empleado a entity de Mantenimientos
        if (json.getIdEmpleado() != null){
            EmpleadosEntity empleado = repoEmpleados.findById(json.getIdEmpleado())
                    .orElseThrow(()-> new ExcepcionEmpleadoNoEncontrado("ID de empleado no encontrado"));
            existente.setEmpleado(empleado);
        }

        //Asignando TipoMantenimiento a entity de Mantenimientos
        if (json.getIdTipoMantenimiento() != null){
            TiposMantenimientoEntity tipoMantenimiento = repoTiposMantenimiento.findById(json.getIdTipoMantenimiento())
                    .orElseThrow(()-> new ExcepcionTipoMantenimientoNoEncontrado("ID del tipo de mantenimiento no encontrado"));
            existente.setTipoMantenimiento(tipoMantenimiento);
        }

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
