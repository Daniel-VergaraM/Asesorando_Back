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

import co.edu.uniandes.dse.asesorando.dto.ComentarioDTO;
import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.ComentarioService;



@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
    
    @Autowired
    private ComentarioService comentarioService;
    
    @Autowired
    private ModelMapper modelMapper;



    /**
	 * Busca y devuelve todos los comentarios que existen en la aplicacion.
	 *
	 * @return JSONArray {@link ComentarioDTO} - Los comentarios encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
    @GetMapping
	@ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioDTO> findAll(){

        List<ComentarioEntity> comentarios = comentarioService.getComentarios();

        return modelMapper.map(comentarios, new TypeToken<List<ComentarioDTO>>() {
		}.getType());    

    }



    /**
	 * Busca el comentario con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param comentarioId Identificador del comentario que se esta buscando. Este debe ser una
	 *               cadena de dígitos.
	 * @return JSON {@link ComentarioDTO} - El comentario buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		ComentarioEntity comentarioEntity = comentarioService.leer_comentario(id);
		return modelMapper.map(comentarioEntity, ComentarioDTO.class);
	}
    


    /**
	 * Crea un nuevo comentario con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param comentario {@link ComentarioDTO} - EL comentario que se desea guardar.
	 * @return JSON {@link ComentarioDTO} - El comentario guardado con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ComentarioDTO create(@RequestBody ComentarioDTO comentarioDTO) throws IllegalOperationException, EntityNotFoundException {
		ComentarioEntity comentarioEntity = comentarioService.crearComentario(modelMapper.map(comentarioDTO, ComentarioEntity.class));
		return modelMapper.map(comentarioEntity, ComentarioDTO.class);
	}


    /**
	 * Actualiza el comentario con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param comentarioId Identificador del comentario que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param comentario   {@link ComentarioDTO} El comentario que se desea guardar.
	 * @return JSON {@link ComentarioDTO} - El comentario guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO update(@PathVariable Long id, @RequestBody ComentarioDTO comentarioDTO)
			throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity comentarioEntity = comentarioService.actualizar_comentario(id, modelMapper.map(comentarioDTO, ComentarioEntity.class));
		return modelMapper.map(comentarioEntity, ComentarioDTO.class);
	}

	/**
	 * Borra el comentario con el id asociado recibido en la URL.
	 *
	 * @param comentarioId Identificador del comentario que se desea borrar. Este debe ser una
	 *               cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		comentarioService.eliminar_comentario(id);
	}


}
