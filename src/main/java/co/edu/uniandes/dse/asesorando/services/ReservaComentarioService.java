package co.edu.uniandes.dse.asesorando.services;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class ReservaComentarioService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private String exceptionPartString = "La reserva no existe";

    @Transactional
    public ComentarioEntity crearComentario(Long reservaId, ComentarioEntity nuevoComentario) throws EntityNotFoundException {
        // Encuentra la reserva por su ID
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));

        // Asocia el comentario a la reserva
        nuevoComentario.setReserva(reserva);
        return comentarioRepository.save(nuevoComentario);  // Guarda el comentario
    }

    @Transactional
    public ComentarioEntity obtenerComentarioPorReserva(Long reservaId) throws EntityNotFoundException {
        // Obtiene el comentario asociado a la reserva
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));

        return reserva.getComentario();  // Retorna el comentario de la reserva
    }

    @Transactional
    public ComentarioEntity actualizarComentario(Long reservaId, ComentarioEntity comentarioActualizado) throws EntityNotFoundException {
        // Encuentra la reserva
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));

        // Actualiza el comentario asociado a la reserva
        ComentarioEntity comentario = reserva.getComentario();
        if (comentario == null) {
            throw new EntityNotFoundException("No hay comentario asociado a esta reserva");
        }

        comentario.setComentario(comentarioActualizado.getComentario());
        comentario.setCalificacion(comentarioActualizado.getCalificacion()); 
        return comentarioRepository.save(comentario); 
    }

    @Transactional
    public void eliminarComentario(Long reservaId) throws EntityNotFoundException {
        // Encuentra la reserva y elimina el comentario asociado
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        
        ComentarioEntity comentario = reserva.getComentario();
        if (comentario != null) {
            comentarioRepository.delete(comentario);  // Elimina el comentario
            reserva.setComentario(null);
        } else {
            throw new EntityNotFoundException("No hay comentario asociado a esta reserva");
        }
    }

    @Transactional
    public void eliminarReservaYComentario(Long reservaId) throws EntityNotFoundException {
        // Encuentra la reserva y elimina tanto la reserva como el comentario
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));

        ComentarioEntity comentario = reserva.getComentario();
        if (comentario != null) {
            comentario.setReserva(null);
            comentarioRepository.delete(comentario);  // Elimina el comentario
            reserva.setComentario(null);
            reservaRepository.delete(reserva);  // Elimina la reserva
        }

        reservaRepository.delete(reserva);  // Elimina la reserva
    }

    @Transactional
    public ComentarioEntity asociarComentarioAReserva(Long reservaId, Long comentarioId) throws EntityNotFoundException {
        // Buscar la reserva
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));

        // Buscar el comentario
        ComentarioEntity comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException("El comentario no existe"));

        // Verificar si la reserva ya tiene un comentario asociado
        if (reserva.getComentario() != null) {
            // Si ya tiene un comentario, actualizarlo
            ComentarioEntity comentarioExistente = reserva.getComentario();
            comentarioExistente.setComentario(comentario.getComentario());
            comentarioExistente.setCalificacion(comentario.getCalificacion());
            return comentarioRepository.save(comentarioExistente);
        } else {
            // Si no tiene, asignar el nuevo comentario
            reserva.setComentario(comentario);
            comentario.setReserva(reserva);
            return comentarioRepository.save(comentario);
        }
    }

    @Transactional
    public ComentarioEntity crearComentarioEnReserva(@NotNull Long reservaId, @NotNull Long comentarioId) 
            throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Iniciando la creación del comentario en la reserva con ID: {}", reservaId);

        // Buscar la reserva en la base de datos
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva con ID " + reservaId + " no está en la base de datos"));

        // Buscar el comentario en la base de datos
        ComentarioEntity comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException("El comentario con ID " + comentarioId + " no está en la base de datos"));

        // Asociar el comentario a la reserva
        comentario.setReserva(reserva);
        reserva.setComentario(comentario);

        // Guardar los cambios en la base de datos
        comentarioRepository.save(comentario);
        reservaRepository.save(reserva);

        log.info("Comentario con ID {} asociado exitosamente a la reserva con ID {}", comentarioId, reservaId);
        
        return comentario;
    }



}
