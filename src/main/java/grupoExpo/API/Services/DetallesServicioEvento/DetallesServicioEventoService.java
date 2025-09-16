package grupoExpo.API.Services.DetallesServicioEvento;

import grupoExpo.API.Entities.DetallesServicioEvento.DetallesServicioEventoEntity;
import grupoExpo.API.Entities.Eventos.EventosEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDetalleServicioEventoNoEncontrado;
import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDetalleServicioEventoNoRegistrado;
import grupoExpo.API.Exceptions.Eventos.ExcepcionEventoNoEncontrado;
import grupoExpo.API.Exceptions.Servicios.ExcepcionServicioNoEncontrado;
import grupoExpo.API.Models.DTO.DetallesServicioEventoDTO;
import grupoExpo.API.Repositories.DetallesServicioEvento.DetallesServicioEventoRepository;
import grupoExpo.API.Repositories.Eventos.EventosRepository;
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
public class DetallesServicioEventoService {

    @Autowired
    private DetallesServicioEventoRepository repo;

    @Autowired
    private ServiciosRepository repoServicios;

    @Autowired
    private EventosRepository repoEventos;

    public Page<DetallesServicioEventoDTO> getAllDetallesServicioEvento(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<DetallesServicioEventoEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirADetalleServicioEventoDTO);
    }

    private DetallesServicioEventoDTO convertirADetalleServicioEventoDTO(DetallesServicioEventoEntity detalleServicioEvento) {
        DetallesServicioEventoDTO dto = new DetallesServicioEventoDTO();
        dto.setIdDetalleServicioEvento(detalleServicioEvento.getIdDetalleServicioEvento());
        if (detalleServicioEvento.getServicio() != null){
            dto.setNombreServicio(detalleServicioEvento.getServicio().getNombreServicio());
            dto.setIdServicio(detalleServicioEvento.getServicio().getIdServicio());
        }else{
            dto.setNombreServicio("Sin nombre de servicio asignado");
            dto.setIdServicio(null);
        }
        if (detalleServicioEvento.getEvento() != null){
            dto.setNombreEvento(detalleServicioEvento.getEvento().getNombreEvento());
            dto.setIdEvento(detalleServicioEvento.getEvento().getIdEvento());
        }else{
            dto.setNombreEvento("Sin nombre de evento asignado");
            dto.setIdEvento(null);
        }
        return dto;
    }

    public DetallesServicioEventoDTO insertarDatos(DetallesServicioEventoDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            DetallesServicioEventoEntity entity = ConvertirAEntity(data);
            DetallesServicioEventoEntity detalleServicioEventoGuardado = repo.save(entity);
            return convertirADetalleServicioEventoDTO(detalleServicioEventoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el detalleServicioEvento: " + e.getMessage());
            throw new ExcepcionDetalleServicioEventoNoRegistrado("Error al registrar el detalleServicioEvento.");
        }
    }

    private DetallesServicioEventoEntity ConvertirAEntity(DetallesServicioEventoDTO data) {
        DetallesServicioEventoEntity entity = new DetallesServicioEventoEntity();

        //Asignando Servicio a entity de DetallesServicioEvento
        if (data.getIdServicio() != null){
            ServiciosEntity servicio = repoServicios.findById(data.getIdServicio())
                    .orElseThrow(()-> new ExcepcionServicioNoEncontrado("ID del servicio no encontrado"));
            entity.setServicio(servicio);
        }

        //Asignando Evento a entity de DetallesServicioEvento
        if (data.getIdEvento() != null){
            EventosEntity evento = repoEventos.findById(data.getIdEvento())
                    .orElseThrow(()-> new ExcepcionEventoNoEncontrado("ID del evento no encontrado"));
            entity.setEvento(evento);
        }
        return entity;
    }

    public DetallesServicioEventoDTO actualizarDetalleServicioEvento(String id, DetallesServicioEventoDTO json) {
        //1. Verificar la existencia del detalleServicioEvento.
        DetallesServicioEventoEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionDetalleServicioEventoNoEncontrado("DetalleServicioEvento no encontrado"));
        //2. Actualizar los campos

        //Asignando Servicio a entity de DetallesServicioEvento
        if (json.getIdServicio() != null){
            ServiciosEntity servicio = repoServicios.findById(json.getIdServicio())
                    .orElseThrow(()-> new ExcepcionServicioNoEncontrado("ID del servicio no encontrado"));
            existente.setServicio(servicio);
        }

        //Asignando Evento a entity de DetallesServicioEvento
        if (json.getIdEvento() != null){
            EventosEntity evento = repoEventos.findById(json.getIdEvento())
                    .orElseThrow(()-> new ExcepcionEventoNoEncontrado("ID del evento no encontrado"));
            existente.setEvento(evento);
        }

        //3. Guardar los cambios
        DetallesServicioEventoEntity detalleServicioEventoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirADetalleServicioEventoDTO(detalleServicioEventoActualizado);
    }

    public boolean eliminarDetalleServicioEvento(String id) {
        try {
            //1. Validar existencia del detalleServicioEvento
            DetallesServicioEventoEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el detalleServicioEvento, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el detalleServicioEvento con ID: " + id + " para eliminar. ", 1);
        }
    }
}
