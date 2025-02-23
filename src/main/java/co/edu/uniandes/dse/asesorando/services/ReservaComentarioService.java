package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
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

    @Transactional
    public ComentarioEntity crearComentario(Long reservaId, ComentarioEntity nuevoComentario) {
        // Encuentra la reserva por su ID
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        // Asocia el comentario a la reserva
        nuevoComentario.setReserva(reserva);
        return comentarioRepository.save(nuevoComentario);  // Guarda el comentario
    }

    @Transactional
    public ComentarioEntity obtenerComentarioPorReserva(Long reservaId) {
        // Obtiene el comentario asociado a la reserva
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        return reserva.getComentario();  // Retorna el comentario de la reserva
    }

    @Transactional
    public ComentarioEntity actualizarComentario(Long reservaId, ComentarioEntity comentarioActualizado) {
        // Encuentra la reserva
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        // Actualiza el comentario asociado a la reserva
        ComentarioEntity comentario = reserva.getComentario();
        if (comentario == null) {
            throw new IllegalArgumentException("No hay comentario asociado a esta reserva");
        }

        comentario.setComentario(comentarioActualizado.getComentario());
        comentario.setCalificacion(comentarioActualizado.getCalificacion()); 
        return comentarioRepository.save(comentario); 
    }

    @Transactional
    public void eliminarComentario(Long reservaId) {
        // Encuentra la reserva y elimina el comentario asociado
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));
        
        ComentarioEntity comentario = reserva.getComentario();
        if (comentario != null) {
            comentarioRepository.delete(comentario);  // Elimina el comentario
            reserva.setComentario(null);
        } else {
            throw new IllegalArgumentException("No hay comentario asociado a esta reserva");
        }
    }

    @Transactional
    public void eliminarReservaYComentario(Long reservaId) {
        // Encuentra la reserva y elimina tanto la reserva como el comentario
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        ComentarioEntity comentario = reserva.getComentario();
        if (comentario != null) {
            comentario.setReserva(null);
            comentarioRepository.delete(comentario);  // Elimina el comentario
            reserva.setComentario(null);
            reservaRepository.delete(reserva);  // Elimina la reserva
        }

        reservaRepository.delete(reserva);  // Elimina la reserva
    }
}
