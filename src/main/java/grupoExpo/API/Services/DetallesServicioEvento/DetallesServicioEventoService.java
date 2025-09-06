package grupoExpo.API.Services.DetallesServicioEvento;

import grupoExpo.API.Entities.DetallesServicioEvento.DetallesServicioEventoEntity;
import grupoExpo.API.Entities.Eventos.EventosEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDetalleServicioEventoNoEncontrado;
import grupoExpo.API.Exceptions.DetallesServicioEvento.ExcepcionDetalleServicioEventoNoRegistrado;
import grupoExpo.API.Models.DTO.DetallesServicioEventoDTO;
import grupoExpo.API.Repositories.DetallesServicioEvento.DetallesServicioEventoRepository;
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

    public Page<DetallesServicioEventoDTO> getAllDetallesServicioEvento(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<DetallesServicioEventoEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirADetalleServicioEventoDTO);
    }

    private DetallesServicioEventoDTO convertirADetalleServicioEventoDTO(DetallesServicioEventoEntity detalleServicioEvento) {
        DetallesServicioEventoDTO dto = new DetallesServicioEventoDTO();
        dto.setIdDetalleServicioEvento(detalleServicioEvento.getIdDetalleServicioEvento());
        dto.setIdServicio(detalleServicioEvento.getServicio().getIdServicio());
        dto.setIdEvento(detalleServicioEvento.getEvento().getIdEvento());
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
        ServiciosEntity servicio = new ServiciosEntity();
        servicio.setIdServicio(data.getIdServicio());
        entity.setServicio(servicio);

        //Asignando Evento a entity de DetallesServicioEvento
        EventosEntity evento = new EventosEntity();
        evento.setIdEvento(data.getIdEvento());
        entity.setEvento(evento);
        return entity;
    }

    public DetallesServicioEventoDTO actualizarDetalleServicioEvento(String id, DetallesServicioEventoDTO json) {
        //1. Verificar la existencia del detalleServicioEvento.
        DetallesServicioEventoEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionDetalleServicioEventoNoEncontrado("DetalleServicioEvento no encontrado"));
        //2. Actualizar los campos

        //Asignando Servicio a entity de DetallesServicioEvento
        ServiciosEntity servicio = new ServiciosEntity();
        servicio.setIdServicio(json.getIdServicio());
        existente.setServicio(servicio);

        //Asignando Evento a entity de DetallesServicioEvento
        EventosEntity evento = new EventosEntity();
        evento.setIdEvento(json.getIdEvento());
        existente.setEvento(evento);

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
