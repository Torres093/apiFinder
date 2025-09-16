package grupoExpo.API.Services.Empleados;

import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Entities.Cargos.CargosEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Exceptions.Cargos.ExcepcionCargoNoEncontrado;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoRegistrado;
import grupoExpo.API.Exceptions.Hotel.ExcepcionHotelNoEncontrado;
import grupoExpo.API.Exceptions.Usuarios.ExcepcionUsuarioNoEncontrado;
import grupoExpo.API.Models.DTO.EmpleadosDTO;
import grupoExpo.API.Repositories.Cargos.CargosRepository;
import grupoExpo.API.Repositories.Empleados.EmpleadosRepository;
import grupoExpo.API.Repositories.Hotel.HotelRepository;
import grupoExpo.API.Repositories.Usuarios.UsuariosRepository;
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
public class EmpleadosService {

    @Autowired
    private EmpleadosRepository repo;

    @Autowired
    private UsuariosRepository repoUsuarios;

    @Autowired
    private CargosRepository repoCargos;

    @Autowired
    private HotelRepository repoHotel;

    public Page<EmpleadosDTO> getAllEmpleados(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<EmpleadosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAEmpleadoDTO);
    }

    private EmpleadosDTO convertirAEmpleadoDTO(EmpleadosEntity empleado) {
        EmpleadosDTO dto = new EmpleadosDTO();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        if (empleado.getUsuario() != null){
            dto.setNombreUsuario(empleado.getUsuario().getNombreUsuario());
            dto.setIdUsuario(empleado.getUsuario().getIdUsuario());
        }else{
            dto.setNombreUsuario("Sin nombre de usuario asignado");
            dto.setIdUsuario(null);
        }
        if (empleado.getCargo() != null){
            dto.setNombreCargo(empleado.getCargo().getNombreCargo());
            dto.setIdCargo(empleado.getCargo().getIdCargo());
        }else{
            dto.setNombreCargo("Sin nombre de cargo asignado");
            dto.setIdCargo(null);
        }
        if (empleado.getHotel() != null){
            dto.setNombreHotel(empleado.getHotel().getNombreHotel());
            dto.setIdHotel(empleado.getHotel().getIdHotel());
        }else{
            dto.setNombreHotel("Sin nombre de hotel asignado");
            dto.setIdHotel(null);
        }
        dto.setNombreEmpleado(empleado.getNombreEmpleado());
        dto.setApellidoEmpleado(empleado.getApellidoEmpleado());
        dto.setDireccionEmpleado(empleado.getDireccionEmpleado());
        dto.setNacimientoEmpleado(empleado.getNacimientoEmpleado());
        dto.setTelefonoEmpleado(empleado.getTelefonoEmpleado());
        dto.setSalarioEmpleado(empleado.getSalarioEmpleado().doubleValue());
        dto.setDuiEmpleado(empleado.getDuiEmpleado());
        return dto;
    }

    public EmpleadosDTO insertarDatos(EmpleadosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            EmpleadosEntity entity = ConvertirAEntity(data);
            EmpleadosEntity empleadoGuardado = repo.save(entity);
            return convertirAEmpleadoDTO(empleadoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el empleado: " + e.getMessage());
            throw new ExcepcionEmpleadoNoRegistrado("Error al registrar el empleado.");
        }
    }

    private EmpleadosEntity ConvertirAEntity(EmpleadosDTO data) {
        EmpleadosEntity entity = new EmpleadosEntity();

        //Asignando usuario a entity de Empleados
        if (data.getIdUsuario() != null){
            UsuariosEntity usuario = repoUsuarios.findById(data.getIdUsuario())
                    .orElseThrow(()-> new ExcepcionUsuarioNoEncontrado("ID del usuario no encontrado"));
            entity.setUsuario(usuario);
        }

        //Asignando cargo a entity de Empleados
        if (data.getIdCargo() != null){
            CargosEntity cargo = repoCargos.findById(data.getIdCargo())
                    .orElseThrow(()-> new ExcepcionCargoNoEncontrado("ID del cargo no encontrado"));
            entity.setCargo(cargo);
        }

        //Asignando hotel a entity de Empleados
        if (data.getIdHotel() != null){
            HotelEntity hotel = repoHotel.findById(data.getIdHotel())
                    .orElseThrow(()-> new ExcepcionHotelNoEncontrado("ID del hotel no encontrado"));
            entity.setHotel(hotel);
        }

        //Asignando atributos de DTO a entity
        entity.setNombreEmpleado(data.getNombreEmpleado());
        entity.setApellidoEmpleado(data.getApellidoEmpleado());
        entity.setDireccionEmpleado(data.getDireccionEmpleado());
        entity.setNacimientoEmpleado(data.getNacimientoEmpleado());
        entity.setTelefonoEmpleado(data.getTelefonoEmpleado());
        entity.setSalarioEmpleado(BigDecimal.valueOf(data.getSalarioEmpleado()));
        entity.setDuiEmpleado(data.getDuiEmpleado());
        return entity;
    }

    public EmpleadosDTO actualizarEmpleado(String id, EmpleadosDTO json) {
        //1. Verificar la existencia del empleado.
        EmpleadosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionEmpleadoNoEncontrado("Empleado no encontrado"));

        //2. Actualizar los campos
        //Asignando usuario a entity de Empleados
        if (json.getIdUsuario() != null){
            UsuariosEntity usuario = repoUsuarios.findById(json.getIdUsuario())
                    .orElseThrow(()-> new ExcepcionUsuarioNoEncontrado("ID del usuario no encontrado"));
            existente.setUsuario(usuario);
        }

        //Asignando cargo a entity de Empleados
        if (json.getIdCargo() != null){
            CargosEntity cargo = repoCargos.findById(json.getIdCargo())
                    .orElseThrow(()-> new ExcepcionCargoNoEncontrado("ID del cargo no encontrado"));
            existente.setCargo(cargo);
        }

        //Asignando hotel a entity de Empleados
        if (json.getIdHotel() != null){
            HotelEntity hotel = repoHotel.findById(json.getIdHotel())
                    .orElseThrow(()-> new ExcepcionHotelNoEncontrado("ID del hotel no encontrado"));
            existente.setHotel(hotel);
        }

        //Asignando atributos de DTO a entity
        existente.setNombreEmpleado(json.getNombreEmpleado());
        existente.setApellidoEmpleado(json.getApellidoEmpleado());
        existente.setDireccionEmpleado(json.getDireccionEmpleado());
        existente.setNacimientoEmpleado(json.getNacimientoEmpleado());
        existente.setTelefonoEmpleado(json.getTelefonoEmpleado());
        existente.setSalarioEmpleado(BigDecimal.valueOf(json.getSalarioEmpleado()));
        existente.setDuiEmpleado(json.getDuiEmpleado());

        //3. Guardar los cambios
        EmpleadosEntity empleadoActualizado = repo.save(existente);

        //4. Convertir los datos a DTO y retornarlos
        return convertirAEmpleadoDTO(empleadoActualizado);
    }

    public boolean eliminarEmpleado(String id) {
        try {
            //1. Validar existencia del empleado
            EmpleadosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el empleado, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró el empleado con ID: " + id + " para eliminar. ", 1);
        }
    }
}
