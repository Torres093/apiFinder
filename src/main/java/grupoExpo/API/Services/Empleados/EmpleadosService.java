package grupoExpo.API.Services.Empleados;

import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Entities.Cargos.CargosEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoEncontrado;
import grupoExpo.API.Exceptions.Empleados.ExcepcionEmpleadoNoRegistrado;
import grupoExpo.API.Models.DTO.EmpleadosDTO;
import grupoExpo.API.Repositories.Empleados.EmpleadosRepository;
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

    public Page<EmpleadosDTO> getAllEmpleados(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<EmpleadosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAEmpleadoDTO);
    }

    private EmpleadosDTO convertirAEmpleadoDTO(EmpleadosEntity empleado) {
        EmpleadosDTO dto = new EmpleadosDTO();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setIdUsuario(empleado.getUsuario().getIdUsuario());
        dto.setIdCargo(empleado.getCargo().getIdCargo());
        dto.setIdHotel(empleado.getHotel().getIdHotel());
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
        UsuariosEntity usuario = new UsuariosEntity();
        usuario.setIdUsuario(data.getIdUsuario());
        entity.setUsuario(usuario);

        //Asignando cargo a entity de Empleados
        CargosEntity cargo = new CargosEntity();
        cargo.setIdCargo(data.getIdCargo());
        entity.setCargo(cargo);

        //Asignando hotel a entity de Empleados
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(data.getIdHotel());
        entity.setHotel(hotel);

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
        UsuariosEntity usuario = new UsuariosEntity();
        usuario.setIdUsuario(json.getIdUsuario());
        existente.setUsuario(usuario);

        //Asignando cargo a entity de Empleados
        CargosEntity cargo = new CargosEntity();
        cargo.setIdCargo(json.getIdCargo());
        existente.setCargo(cargo);

        //Asignando hotel a entity de Empleados
        HotelEntity hotel = new HotelEntity();
        hotel.setIdHotel(json.getIdHotel());
        existente.setHotel(hotel);

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
