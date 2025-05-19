package co.edu.uniandes.dse.asesorando.services;


import java.util.List;
import java.util.Optional;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.EstudianteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComentarioEstudianteService {
    
    @Autowired
	private ComentarioRepository comentarioRepository;

	@Autowired
	private EstudianteRepository estudianteRepository;

	private String exceptionPartString = "Estudiante no encontrado";

	private String exceptionPartString2 = "Comentario no encontrado";


    @Transactional
	public ComentarioEntity addcomentario(Long comentarioId, Long estudianteId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un comentario a un estudiante con id = {0}", estudianteId);
		Optional<EstudianteEntity> estudianteEntity = estudianteRepository.findById(estudianteId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

		if (estudianteEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString);

		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString2);

		
		log.info("Termina proceso de asociarle un comentario al estudiante con id = {0}", estudianteId);
		return comentarioEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de comentarioEntity asociadas a una instancia
	 * de estudiante
	 *
	 * @param estudiantesId Identificador de la instancia de estudiante
	 * @return Colección de instancias de comentarioEntity asociadas a la instancia de
	 *         estudiante
	 */
	@Transactional
	public List<ComentarioEntity> getcomentarios(Long estudianteId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los comentarios del estudiante con id = {0}", estudianteId);
		Optional<EstudianteEntity> estudianteEntity;
        estudianteEntity = estudianteRepository.findById(estudianteId);
		if (estudianteEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString);

		log.info("Termina proceso de consultar todos los comentarios del estudiante con id = {0}", estudianteId);
        return estudianteEntity.get().getComentarios();
	}

	/**
	 * Obtiene una instancia de comentarioEntity asociada a una instancia de estudiante
	 *
	 * @param estudiantesId Identificador de la instancia de estudiante
	 * @param comentariosId   Identificador de la instancia de comentario
	 * @return La entidadd de comentario del estudiante
	 */
	@Transactional
	public ComentarioEntity getcomentario(Long estudianteId, Long comentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el comentario con id = {0} del estudiante con id = " + estudianteId, comentarioId);
		Optional<EstudianteEntity> estudianteEntity = estudianteRepository.findById(estudianteId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

		if (estudianteEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString);

		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString2);

		log.info("Termina proceso de consultar el comentario con id = {0} del estudiante con id = " + estudianteId, comentarioId);
        if (!estudianteEntity.get().getComentarios().contains(comentarioEntity.get()))
            throw new IllegalOperationException("The comentario is not associated to the estudiante");
		
		return comentarioEntity.get();
	}

	/**
	 * Remplaza las instancias de comentario asociadas a una instancia de estudiante
	 *
	 * @param estudianteId Identificador de la instancia de estudiante
	 * @param comentarios    Colección de instancias de comentarioEntity a asociar a instancia
	 *                 de estudiante
	 * @return Nueva colección de comentarioEntity asociada a la instancia de estudiante
	 */
	@Transactional
	public List<ComentarioEntity> addcomentarios(Long estudianteId, List<ComentarioEntity> comentarios) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los comentarios asociados al estudiante con id = {0}", estudianteId);
		Optional<EstudianteEntity> estudianteEntity = estudianteRepository.findById(estudianteId);
		if (estudianteEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString);  

		for (ComentarioEntity comentario : comentarios) {
			Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentario.getId());
			if (comentarioEntity.isEmpty())
				throw new EntityNotFoundException(exceptionPartString2);

		}
		log.info("Finaliza proceso de reemplazar los comentarios asociados al estudiante con id = {0}", estudianteId);
        estudianteEntity.get().setComentarios(comentarios);
        return estudianteEntity.get().getComentarios();
	}

	/**
	 * Desasocia un comentario existente de un estudiante existente
	 *
	 * @param estudiantesId Identificador de la instancia de estudiante
	 * @param comentariosId   Identificador de la instancia de comentario
	 */
	@Transactional
	public void removecomentario(Long estudianteId, Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un comentario del estudiante con id = {0}", estudianteId);
		Optional<EstudianteEntity> estudianteEntity = estudianteRepository.findById(estudianteId);
		if (estudianteEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString);

		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(exceptionPartString2);

        

        estudianteEntity.get().getComentarios().remove(comentarioEntity.get());
        comentarioEntity.get().setEstudiante(null);
		log.info("Finaliza proceso de borrar un comentario del estudiante con id = {0}", estudianteId);
	}

}
