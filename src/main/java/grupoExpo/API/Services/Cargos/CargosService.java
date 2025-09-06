package grupoExpo.API.Services.Cargos;

import grupoExpo.API.Entities.Cargos.CargosEntity;
import grupoExpo.API.Exceptions.Cargos.ExcepcionCargoNoEncontrado;
import grupoExpo.API.Exceptions.Cargos.ExcepcionCargoNoRegistrado;
import grupoExpo.API.Models.DTO.CargosDTO;
import grupoExpo.API.Repositories.Cargos.CargosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CargosService {

    @Autowired
    private CargosRepository repo;

    public Page<CargosDTO> getAllCargos(int page, int size){
        Pageable pageable = PageRequest.of(page, size); //Creación de la página.
        Page<CargosEntity> pageEntity = repo.findAll(pageable); //Inserción de la búsqueda con los registros en la página
        return pageEntity.map(this::convertirACargoDTO);
    }

    private CargosDTO convertirACargoDTO(CargosEntity cargo) {
        CargosDTO dto = new CargosDTO();
        dto.setIdCargo(cargo.getIdCargo());
        dto.setNombreCargo(cargo.getNombreCargo());
        return dto;
    }

    public CargosDTO insertarDatos(CargosDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No se puede enviar valores nulos");
        }
        try{
            CargosEntity entity = ConvertirAEntity(data);
            CargosEntity cargoGuardado = repo.save(entity);
            return convertirACargoDTO(cargoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el cargo: " + e.getMessage());
            throw new ExcepcionCargoNoRegistrado("Error al registrar el cargo.");
        }
    }

    private CargosEntity ConvertirAEntity(CargosDTO data) {
        CargosEntity entity = new CargosEntity();

        entity.setNombreCargo(data.getNombreCargo());
        return entity;
    }

    public CargosDTO actualizarCargo(String id, CargosDTO json) {
        //1. Verificar la existencia del cargo.
        CargosEntity existente = repo.findById(id).orElseThrow(() -> new ExcepcionCargoNoEncontrado("Cargo no encontrado"));
        //2. Actualizar los campos
        existente.setNombreCargo(json.getNombreCargo());
        //3. Guardar los cambios
        CargosEntity cargoActualizado = repo.save(existente);
        //4. Convertir los datos a DTO y retornarlos
        return convertirACargoDTO(cargoActualizado);
    }

    public boolean eliminarCargo(String id) {
        try {
            //1. Validar existencia del cargo
            CargosEntity existente = repo.findById(id).orElse(null);
            //2. Eliminar el cargo, si existe retornar true. Si no existe retornar false
            if(existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el cargo con ID: " + id + " para eliminar. ", 1);
        }
    }
}
