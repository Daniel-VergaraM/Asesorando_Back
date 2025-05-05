package co.edu.uniandes.dse.asesorando.controllers;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.dto.TematicaDTO;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.services.TematicaService;



@RestController
@RequestMapping("/tematicas")
public class TematicaController {
    
    @Autowired
    private TematicaService tematicaService;
    
    @Autowired
    private ModelMapper modelMapper;



    /**
	 * Busca y devuelve todos las Tematicas  que existen en la aplicacion.
	 *
	 * @return JSONArray {@link TematicaDTO} - Los tematicas encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
    @GetMapping
	@ResponseStatus(code = HttpStatus.OK)
    public List<TematicaDTO> findAll(){

        List<TematicaEntity> tematicas = tematicaService.getTematicas();

        return modelMapper.map(tematicas, new TypeToken<List<TematicaDTO>>() {
		}.getType());    

    }

    
    /**
	 * Busca el tematica con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param tematicaId Identificador de la tematica que se esta buscando. Este debe ser una
	 *               cadena de dígitos.
	 * @return JSON {@link TematicaDTO} - La Tematica buscada
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public TematicaDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		TematicaEntity tematicaEntity = tematicaService.getTematica(id);
		return modelMapper.map(tematicaEntity, TematicaDTO.class);
	}
    

    /**
     * Crea una nueva tematica
     *
     * @param tematica {@link TematicaDTO} - La tematica que se desea guardar.
     * @return JSON {@link TematicaDTO} - La tematica guardada con su id.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
	public TematicaDTO create(@RequestBody TematicaDTO tematicaDTO) throws EntityNotFoundException {
		TematicaEntity tematicaEntity = tematicaService.createTematica(modelMapper.map(tematicaDTO, TematicaEntity.class));
		return modelMapper.map(tematicaEntity, TematicaDTO.class);
	}


    /**
     * Actualiza la tematica con el id asociado recibido en la URL y la informacion
     * enviada como parametro
     *
     * @param tematicaId Identificador de la tematica que se esta buscando. Este debe ser una
     *               cadena de dígitos.
     * @param tematica {@link TematicaDTO} - La tematica que se desea guardar.
     * @return JSON {@link TematicaDTO} - La tematica guardada con su id.
     */
    @PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public TematicaDTO update(@PathVariable Long id, @RequestBody TematicaDTO tematicaDTO)
			throws EntityNotFoundException {
		TematicaEntity tematicaEntity = tematicaService.updateTematica(id, modelMapper.map(tematicaDTO, TematicaEntity.class));
		return modelMapper.map(tematicaEntity, TematicaDTO.class);
	}
    
    /**
    * Elimina la tematica con el id asociado recibido en la URL
    *
    * @param tematicaId Identificador de la tematica que se esta buscando. Este debe ser una
    *               cadena de dígitos.
    */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException {
		tematicaService.deleteTematica(id);
	}
}
