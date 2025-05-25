package co.edu.uniandes.dse.asesorando.services;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;

@Slf4j
@Service
public class ReservaAsesoriaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private AsesoriaRepository asesoriaRepository;

    private String exceptionPartString = "Reserva con ID ";

    private String exceptionPartString2 = " no encontrada";

    private String exceptionPartString3 = "No hay asesoría asociada a la reserva con ID ";

    /**
     * Asocia una asesoría a una reserva (crear o actualizar).
     */
    @Transactional
    public AsesoriaEntity asociarAsesoriaAReserva(Long reservaId, Long asesoriaId) throws EntityNotFoundException {
        log.info("Asociando asesoría con ID {} a la reserva con ID {}", asesoriaId, reservaId);

        // Buscar la reserva en la base de datos
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + reservaId + exceptionPartString2));

        // Verificar si la reserva ya tiene una asesoría asociada
        if (reserva.getAsesoria() != null) {
            throw new IllegalStateException("La reserva ya tiene una asesoría asociada");
        }

        // Buscar la asesoría en la base de datos
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Asesoría con ID " + asesoriaId + " no encontrada"));

        // Asociar la asesoría a la reserva
        asesoria.setReserva(reserva);
        reserva.setAsesoria(asesoria);

        // Guardar los cambios en la base de datos
        reservaRepository.save(reserva);
        log.info("Asesoría con ID {} asociada exitosamente a la reserva con ID {}", asesoriaId, reservaId);
        
        return asesoriaRepository.save(asesoria);
    }

    /**
     * Obtiene la asesoría asociada a una reserva.
     */
    @Transactional
    public AsesoriaEntity obtenerAsesoriaPorReserva(Long reservaId) throws EntityNotFoundException {
        log.info("Obteniendo asesoría de la reserva con ID: {}", reservaId);

        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + reservaId + " no encontrada"));

        if (reserva.getAsesoria() == null) {
            throw new EntityNotFoundException(exceptionPartString3 + reservaId);
        }

        log.info("Asesoría obtenida exitosamente para la reserva con ID: {}", reservaId);
        return reserva.getAsesoria();
    }

    /**
     * Actualiza la asesoría de una reserva.
     */
    @Transactional
    public AsesoriaEntity actualizarAsesoria(Long reservaId, AsesoriaEntity asesoriaActualizada) throws EntityNotFoundException {
        log.info("Actualizando asesoría de la reserva con ID: {}", reservaId);

        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + reservaId + " no encontrada"));

        AsesoriaEntity asesoriaExistente = reserva.getAsesoria();
        if (asesoriaExistente == null) {
            throw new EntityNotFoundException(exceptionPartString3 + reservaId);
        }

        asesoriaActualizada.setId(asesoriaExistente.getId());
        asesoriaActualizada.setReserva(reserva);

        log.info("Asesoría actualizada exitosamente para la reserva con ID: {}", reservaId);
        return asesoriaRepository.save(asesoriaActualizada);
    }

    /**
     * Elimina la asesoría asociada a una reserva.
     */
    @Transactional
    public void eliminarAsesoria(Long reservaId) throws EntityNotFoundException {
        log.info("Eliminando asesoría de la reserva con ID: {}", reservaId);

        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + reservaId + " no encontrada"));

        AsesoriaEntity asesoria = reserva.getAsesoria();
        if (asesoria == null) {
            throw new EntityNotFoundException(exceptionPartString3 + reservaId);
        }

        reserva.setAsesoria(null);
        reservaRepository.save(reserva);
        asesoriaRepository.delete(asesoria);

        log.info("Asesoría eliminada exitosamente de la reserva con ID: {}", reservaId);
    }



    @Transactional
    public AsesoriaEntity crearAsesoriaEnReserva(@NotNull Long reservaId, @NotNull Long asesoriaId) 
            throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Iniciando la creación de la asesoría en la reserva con ID: {}", reservaId);

        // Buscar la reserva en la base de datos
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva con ID " + reservaId + " no está en la base de datos"));

        // Buscar la asesoría en la base de datos
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La asesoría con ID " + asesoriaId + " no está en la base de datos"));

        // Verificar si la reserva ya tiene una asesoría asociada
        if (reserva.getAsesoria() != null) {
            throw new IllegalOperationException("La reserva con ID " + reservaId + " ya tiene una asesoría asociada");
        }

        // Asociar la asesoría a la reserva
        asesoria.setReserva(reserva);
        reserva.setAsesoria(asesoria);

        // Guardar los cambios en la base de datos
        asesoriaRepository.save(asesoria);
        reservaRepository.save(reserva);

        log.info("Asesoría con ID {} asociada exitosamente a la reserva con ID {}", asesoriaId, reservaId);
        
        return asesoria;
    }

}
