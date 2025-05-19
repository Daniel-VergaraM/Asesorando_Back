package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
@Service

public class ReservaCalendarioService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CalendarioRepository calendarioRepository;

    private String exceptionPartString = "El calendario con ID ";

    private String exceptionPartString2 = "La reserva con ID ";

    private String exceptionPartString3 = " no está en la base de datos";


    @Transactional
    public ReservaEntity asociarReservaACalendario(Long reservaId, Long calendarioId) throws EntityNotFoundException {
        // Buscar la reserva
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        // Buscar el calendario
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario no existe"));


        // Asociar la reserva al nuevo calendario
        reserva.setCalendario(calendario);
        calendario.getReservas().add(reserva);

        return reservaRepository.save(reserva);
    }


    @Transactional
    public void eliminarReservaDeCalendario(Long calendarioId, Long reservaId) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));


        reservaRepository.delete(reserva);
    }

    @Transactional
    public List<ReservaEntity> obtenerReservasPorCalendario(Long calendarioId) throws EntityNotFoundException {
        log.info("Buscando todas las reservas del calendario con ID {}", calendarioId);

        // Verificar que el calendario existe
        if (!calendarioRepository.existsById(calendarioId)) {
            throw new EntityNotFoundException(exceptionPartString + calendarioId + " no existe");
        }

        // Obtener  y devolver todas las reservas asociadas al calendario
        return reservaRepository.findByCalendarioId(calendarioId);
    }

    @Transactional
    public ReservaEntity crearReservaEnCalendario(@NotNull Long calendarioId, @NotNull Long reservaId) 
            throws EntityNotFoundException, IllegalOperationException {
        

        // Buscar el calendario en la base de datos
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + calendarioId + exceptionPartString3));

        // Buscar la reserva en la base de datos
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2 + reservaId + exceptionPartString3));

        // Verificar si la reserva ya está asociada a un calendario
        if (reserva.getCalendario() != null) {
            throw new IllegalOperationException(exceptionPartString2 + reservaId + " ya está asociada a un calendario");
        }

        // Asociar la reserva al calendario
        reserva.setCalendario(calendario);
        calendario.getReservas().add(reserva);

        // Guardar los cambios en la base de datos
        reservaRepository.save(reserva);
        calendarioRepository.save(calendario);

        
        return reserva;
    }


    public List<ReservaEntity> listarReservasDeCalendario(Long id) {
        return reservaRepository.findByCalendarioId(id);
        }

        @Transactional
        public ReservaEntity actualizarReservaEnCalendario(Long calendarioId, Long reservaId, ReservaEntity reservaActualizada) 
            throws EntityNotFoundException, IllegalOperationException {
        // Buscar el calendario en la base de datos
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
            .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + calendarioId + exceptionPartString3));

        // Buscar la reserva en la base de datos
        ReservaEntity reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2 + reservaId + exceptionPartString3));

        // Verificar si la reserva está asociada al calendario
        if (!reserva.getCalendario().getId().equals(calendario.getId())) {
            throw new IllegalOperationException(exceptionPartString2 + reservaId + " no está asociada al calendario con ID " + calendarioId);
        }

        // Actualizar los detalles de la reserva
        reserva.setFechaReserva(reservaActualizada.getFechaReserva());
        reserva.setEstudiante(reservaActualizada.getEstudiante());
        reserva.setAsesoria(reservaActualizada.getAsesoria());
        reserva.setComentario(reservaActualizada.getComentario());
        reserva.setCancelada(reservaActualizada.getCancelada());
        reserva.setEstado(reservaActualizada.getEstado());


        // Guardar los cambios en la base de datos
        return reservaRepository.save(reserva);
    }




}