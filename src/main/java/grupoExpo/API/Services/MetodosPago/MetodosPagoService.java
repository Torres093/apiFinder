package grupoExpo.API.Services.MetodosPago;

import grupoExpo.API.Models.DTO.MetodosPagoDTO;
import grupoExpo.API.Entities.MetodosPago.MetodosPagoEntity;
import grupoExpo.API.Repositories.MetodosPago.MetodosPagoRepository;
import grupoExpo.API.Exceptions.MetodosPago.ExcepcionMetodoPagoNoEncontrado;
import grupoExpo.API.Exceptions.MetodosPago.ExcepcionMetodoPagoNoRegistrado;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MetodosPagoService {

    @Autowired
    private MetodosPagoRepository repo;

    public List<MetodosPagoDTO> getAllMetodosPago(){
        List<MetodosPagoEntity> metodosPago = repo.findAll();
        return metodosPago.stream()
                .map(this::convertirAMetodosPagoDTO)
                .collect(Collectors.toList());
    }

    private MetodosPagoDTO convertirAMetodosPagoDTO(MetodosPagoEntity metodoPago) {
        MetodosPagoDTO dto = new MetodosPagoDTO();
        dto.setIdMetodoPago(metodoPago.getIdMetodoPago());
        dto.setNombreMetodoPago(metodoPago.getNombreMetodoPago());
        return dto;
    }

    public MetodosPagoDTO insertarDatos(MetodosPagoDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{

            MetodosPagoEntity entity = ConvertirAEntity(data);
            MetodosPagoEntity metodoPagoGuardado = repo.save(entity);
            return convertirAMetodosPagoDTO(metodoPagoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el método de pago: " + e.getMessage());
            throw new ExcepcionMetodoPagoNoRegistrado("Error al registrar el método de pago.");
        }
    }

    private MetodosPagoEntity ConvertirAEntity(MetodosPagoDTO data) {
        MetodosPagoEntity entity = new MetodosPagoEntity();

        entity.setNombreMetodoPago(data.getNombreMetodoPago());
        return entity;
    }

    public MetodosPagoDTO actualizarMetodoPago(String id, MetodosPagoDTO json) {
        //1. Verificar la existencia deL método de pago.
        MetodosPagoEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionMetodoPagoNoEncontrado("Método de pago no encontrado"));
        //2. Actualizar los campos
        existente.setNombreMetodoPago(json.getNombreMetodoPago());
        //3. Guardar los cambios
        MetodosPagoEntity metodoPagoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirAMetodosPagoDTO(metodoPagoActualizado);
    }

    public boolean eliminarMetodoPago(String id) {
        try {
            //1. Validar existencia del método de pago
            MetodosPagoEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el metodo de pago, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el método de pago con ID: " + id + " para eliminar. ", 1);
        }
    }
}
