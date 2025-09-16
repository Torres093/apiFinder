package grupoExpo.API.Services.DetallesServicioPlato;

import grupoExpo.API.Entities.DetallesServicioPlato.DetallesServicioPlatoEntity;
import grupoExpo.API.Entities.Platos.PlatosEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import grupoExpo.API.Exceptions.DetallesServicioPlato.ExcepcionDetalleServicioPlatoNoEncontrado;
import grupoExpo.API.Exceptions.DetallesServicioPlato.ExcepcionDetalleServicioPlatoNoRegistrado;
import grupoExpo.API.Exceptions.Platos.ExcepcionPlatoNoEncontrado;
import grupoExpo.API.Exceptions.Servicios.ExcepcionServicioNoEncontrado;
import grupoExpo.API.Models.DTO.DetallesServicioPlatoDTO;
import grupoExpo.API.Repositories.DetallesServicioPlato.DetallesServicioPlatoRepository;
import grupoExpo.API.Repositories.Platos.PlatosRepository;
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
public class DetallesServicioPlatoService {

    @Autowired
    private DetallesServicioPlatoRepository repo;

    @Autowired
    private ServiciosRepository repoServicios;

    @Autowired
    private PlatosRepository repoPlatos;

    public Page<DetallesServicioPlatoDTO> getAllDetallesServicioPlato(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<DetallesServicioPlatoEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirADetalleServicioPlatoDTO);
    }

    private DetallesServicioPlatoDTO convertirADetalleServicioPlatoDTO(DetallesServicioPlatoEntity detalleServicioPlato) {
        DetallesServicioPlatoDTO dto = new DetallesServicioPlatoDTO();
        dto.setIdDetalleServicioPlato(detalleServicioPlato.getIdDetalleServicioPlato());
        if (detalleServicioPlato.getServicio() != null){
            dto.setNombreServicio(detalleServicioPlato.getServicio().getNombreServicio());
            dto.setIdServicio(detalleServicioPlato.getServicio().getIdServicio());
        }else{
            dto.setNombreServicio("Sin nombre de servicio asignado");
            dto.setIdServicio(null);
        }
        if (detalleServicioPlato.getPlato() != null){
            dto.setNombrePlato(detalleServicioPlato.getPlato().getNombrePlato());
            dto.setIdPlato(detalleServicioPlato.getPlato().getIdPlato());
        }else{
            dto.setNombrePlato("Sin nombre del plato asignado");
            dto.setIdPlato(null);
        }
        return dto;
    }

    public DetallesServicioPlatoDTO insertarDatos(DetallesServicioPlatoDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            DetallesServicioPlatoEntity entity = ConvertirAEntity(data);
            DetallesServicioPlatoEntity detalleServicioPlatoGuardado = repo.save(entity);
            return convertirADetalleServicioPlatoDTO(detalleServicioPlatoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el detalleServicioPlato: " + e.getMessage());
            throw new ExcepcionDetalleServicioPlatoNoRegistrado("Error al registrar el detalleServicioPlato.");
        }
    }

    private DetallesServicioPlatoEntity ConvertirAEntity(DetallesServicioPlatoDTO data) {
        DetallesServicioPlatoEntity entity = new DetallesServicioPlatoEntity();

        //Asignando Servicio a entity de DetallesServicioPlato
        if (data.getIdServicio() != null){
            ServiciosEntity servicio = repoServicios.findById(data.getIdServicio())
                    .orElseThrow(()-> new ExcepcionServicioNoEncontrado("ID del servicio no encontrado"));
            entity.setServicio(servicio);
        }

        //Asignando Plato a entity de DetallesServicioPlato
        if (data.getIdPlato() != null){
            PlatosEntity plato = repoPlatos.findById(data.getIdPlato())
                    .orElseThrow(()-> new ExcepcionPlatoNoEncontrado("ID del plato no encontrado"));
            entity.setPlato(plato);
        }
        return entity;
    }

    public DetallesServicioPlatoDTO actualizarDetalleServicioPlato(String id, DetallesServicioPlatoDTO json) {
        //1. Verificar la existencia del detalleServicioPlato.
        DetallesServicioPlatoEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionDetalleServicioPlatoNoEncontrado("DetalleServicioPlato no encontrado"));
        //2. Actualizar los campos

        //Asignando Servicio a entity de DetallesServicioPlato
        if (json.getIdServicio() != null){
            ServiciosEntity servicio = repoServicios.findById(json.getIdServicio())
                    .orElseThrow(()-> new ExcepcionServicioNoEncontrado("ID del servicio no encontrado"));
            existente.setServicio(servicio);
        }

        //Asignando Plato a entity de DetallesServicioPlato
        if (json.getIdPlato() != null){
            PlatosEntity plato = repoPlatos.findById(json.getIdPlato())
                    .orElseThrow(()-> new ExcepcionPlatoNoEncontrado("ID del plato no encontrado"));
            existente.setPlato(plato);
        }

        //3. Guardar los cambios
        DetallesServicioPlatoEntity detalleServicioPlatoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirADetalleServicioPlatoDTO(detalleServicioPlatoActualizado);
    }

    public boolean eliminarDetalleServicioPlato(String id) {
        try {
            //1. Validar existencia del detalleServicioPlato
            DetallesServicioPlatoEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el detalleServicioPlato, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el detalleServicioPlato con ID: " + id + " para eliminar. ", 1);
        }
    }
}
