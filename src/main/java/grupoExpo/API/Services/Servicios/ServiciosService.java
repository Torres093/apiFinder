package grupoExpo.API.Services.Servicios;

import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import grupoExpo.API.Exceptions.Servicios.ExcepcionServicioNoEncontrado;
import grupoExpo.API.Exceptions.Servicios.ExcepcionServicioNoRegistrado;
import grupoExpo.API.Models.DTO.ServiciosDTO;
import grupoExpo.API.Repositories.Servicios.ServiciosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiciosService {

    @Autowired
    private ServiciosRepository repo;

    public Page<ServiciosDTO> getAllServicios(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<ServiciosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirAServicioDTO);
    }

    private ServiciosDTO convertirAServicioDTO(ServiciosEntity servicio) {
        ServiciosDTO dto = new ServiciosDTO();
        dto.setIdServicio(servicio.getIdServicio());
        dto.setNombreServicio(servicio.getNombreServicio());
        dto.setDescripcionServicio(servicio.getDescripcionServicio());
        return dto;
    }

    public ServiciosDTO insertarDatos(ServiciosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            ServiciosEntity entity = ConvertirAEntity(data);
            ServiciosEntity servicioGuardado = repo.save(entity);
            return convertirAServicioDTO(servicioGuardado);
        }catch (Exception e){
            log.error("Error al registrar el servicio: " + e.getMessage());
            throw new ExcepcionServicioNoRegistrado("Error al registrar el servicio.");
        }
    }

    private ServiciosEntity ConvertirAEntity(ServiciosDTO data) {
        ServiciosEntity entity = new ServiciosEntity();

        entity.setNombreServicio(data.getNombreServicio());
        entity.setDescripcionServicio(data.getDescripcionServicio());
        return entity;
    }

    public ServiciosDTO actualizarServicio(String id, ServiciosDTO json) {
        //1. Verificar la existencia del servicio.
        ServiciosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionServicioNoEncontrado("Servicio no encontrado"));
        //2. Actualizar los campos
        existente.setNombreServicio(json.getNombreServicio());
        existente.setDescripcionServicio(json.getDescripcionServicio());
        //3. Guardar los cambios
        ServiciosEntity servicioActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAServicioDTO(servicioActualizado);
    }

    public boolean eliminarServicio(String id) {
        try {
            //1. Validar existencia del servicio
            ServiciosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el servicio, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el servicio con ID: " + id + " para eliminar. ", 1);
        }
    }
}
