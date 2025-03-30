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

import co.edu.uniandes.dse.asesorando.dto.ProfesorDTO;
import co.edu.uniandes.dse.asesorando.dto.ProfesorDetailsDTO;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.services.TematicaProfesorService;


@RestController
@RequestMapping("/tematicas")
public class TematicaProfesorController {

	@Autowired
	private TematicaProfesorService tematicaProfesorService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Asocia un profesor existente con un tematica existente
	 *
	 * @param profesorId El ID del profesor que se va a asociar
	 * @param tematicaId   El ID del tematica al cual se le va a asociar el profesor
	 * @return JSON {@link ProfesorDetailDTO} - El profesor asociado.
	 */
	@PostMapping(value = "/{tematicaId}/profesors/{profesorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ProfesorDetailsDTO addProfesor(@PathVariable Long profesorId, @PathVariable Long tematicaId)
			throws EntityNotFoundException {
		ProfesorEntity profesorEntity = tematicaProfesorService.agregarProfesorATematica(tematicaId, profesorId);
		return modelMapper.map(profesorEntity, ProfesorDetailsDTO.class);
	}

	/**
	 * Busca y devuelve el profesor con el ID recibido en la URL, relativo a un tematica.
	 *
	 * @param profesorId El ID del profesor que se busca
	 * @param tematicaId   El ID del tematica del cual se busca el profesor
	 * @return {@link ProfesorDetailDTO} - El profesor encontrado en el tematica.
	 */
	@GetMapping(value = "/{tematicaId}/profesors/{profesorId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ProfesorDetailsDTO getProfesor(@PathVariable Long profesorId, @PathVariable Long tematicaId)
			throws EntityNotFoundException {
		ProfesorEntity profesorEntity = tematicaProfesorService.getProfesorDeTematica(tematicaId, profesorId);
		return modelMapper.map(profesorEntity, ProfesorDetailsDTO.class);
	}

	/**
	 * Actualiza la lista de profesores de un tematica con la lista que se recibe en el
	 * cuerpo.
	 *
	 * @param tematicaId  El ID del tematica al cual se le va a asociar la lista de profesores
	 * @param profesors JSONArray {@link ProfesorDTO} - La lista de profesores que se desea
	 *                guardar.
	 * @return JSONArray {@link ProfesorDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{tematicaId}/profesors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfesorDetailsDTO> addProfesors(@PathVariable Long tematicaId, @RequestBody List<ProfesorDTO> profesors)
			throws EntityNotFoundException {
		List<ProfesorEntity> entities = modelMapper.map(profesors, new TypeToken<List<ProfesorEntity>>() {
		}.getType());
		List<ProfesorEntity> profesorsList = tematicaProfesorService.actualizarTematicaProfesor(tematicaId, entities);
		return modelMapper.map(profesorsList, new TypeToken<List<ProfesorDetailsDTO>>() {
		}.getType());
	}

	/**
	 * Busca y devuelve todos los profesores que existen en un tematica.
	 *
	 * @param tematicasd El ID del tematica del cual se buscan los profesores
	 * @return JSONArray {@link ProfesorDetailDTO} - Los profesores encontrados en el
	 *         tematica. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{tematicaId}/profesors")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ProfesorDetailsDTO> getProfesors(@PathVariable Long tematicaId) throws EntityNotFoundException {
		List<ProfesorEntity> profesorEntity = tematicaProfesorService.getProfesoresDeTematica(tematicaId);
		return modelMapper.map(profesorEntity, new TypeToken<List<ProfesorDetailsDTO>>() {
		}.getType());
	}

	/**
	 * Elimina la conexión entre el profesor y el tematica recibidos en la URL.
	 *
	 * @param tematicaId   El ID del tematica al cual se le va a desasociar el profesor
	 * @param profesorId El ID del profesor que se desasocia
	 */
	@DeleteMapping(value = "/{tematicaId}/profesors/{profesorId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeProfesor(@PathVariable Long profesorId, @PathVariable Long tematicaId)
			throws EntityNotFoundException {
		tematicaProfesorService.eliminarProfesorDeTematica(profesorId, tematicaId);
	}
}