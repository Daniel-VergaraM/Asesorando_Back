package co.edu.uniandes.dse.asesorando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Transactional
    public ComentarioEntity crearComentario(ComentarioEntity comentario) throws IllegalArgumentException {
        log.info("Inicia proceso de creación de comentario");
        if (comentario.getComentario() == null || comentario.getComentario().isEmpty() || comentario.getCalificacion() == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo o vacío");
        }

        if (comentario.getCalificacion() < 0 || comentario.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 5");
        }

        log.info("Termina proceso de creación de comentario");
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public ComentarioEntity leer_comentario(Long comentarioId) throws IllegalArgumentException {
        log.info("Inicia proceso de lectura de comentario con id = {}", comentarioId);
        if (comentarioId == null) {
            throw new IllegalArgumentException("El id del comentario no puede ser nulo");
        }

        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        if (comentarioEntity.isEmpty()) {
            throw new IllegalArgumentException("El comentario no existe");
        }

        log.info("Termina proceso de lectura de comentario con id = {}", comentarioId);
        return comentarioEntity.get();
    }

    @Transactional
    public List<ComentarioEntity> getComentarios() {
        log.info("Inicia proceso de consultar todos los comentarios");
        return comentarioRepository.findAll();
    }

    @Transactional
    public ComentarioEntity actualizar_comentario(Long comentarioId, ComentarioEntity comentario) throws IllegalArgumentException {
        log.info("Inicia proceso de actualización de comentario con id = {}", comentarioId);
        if (comentario.getComentario() == null || comentario.getComentario().isEmpty() || comentario.getCalificacion() == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo o vacío");
        }

        if (comentario.getCalificacion() < 0 || comentario.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 5");
        }

        comentario.setId(comentarioId);
        log.info("Termina proceso de actualización de comentario con id = {}", comentarioId);
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void eliminar_comentario(Long comentarioId) throws IllegalArgumentException {
        log.info("Inicia proceso de eliminación de comentario con id = {}", comentarioId);
        if (comentarioId == null) {
            throw new IllegalArgumentException("El id del comentario no puede ser nulo");
        }

        if (!comentarioRepository.existsById(comentarioId)) {
            throw new IllegalArgumentException("El comentario no existe");
        }

        comentarioRepository.deleteById(comentarioId);
        log.info("Termina proceso de eliminación de comentario con id = {}", comentarioId);
    }
}