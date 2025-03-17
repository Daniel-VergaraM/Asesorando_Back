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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import jakarta.transaction.Transactional;


@Slf4j
@Data
@Service

public class ReservaCalendarioService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CalendarioRepository calendarioRepository;


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
            throw new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe");
        }

        // Obtener todas las reservas asociadas al calendario
        List<ReservaEntity> reservas = reservaRepository.findByCalendarioId(calendarioId);

        return reservas;
    }

    @Transactional
    public ReservaEntity crearReservaEnCalendario(@NotNull Long calendarioId, @NotNull Long reservaId) 
            throws EntityNotFoundException, IllegalOperationException {
        

        // Buscar el calendario en la base de datos
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no está en la base de datos"));

        // Buscar la reserva en la base de datos
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva con ID " + reservaId + " no está en la base de datos"));

        // Verificar si la reserva ya está asociada a un calendario
        if (reserva.getCalendario() != null) {
            throw new IllegalOperationException("La reserva con ID " + reservaId + " ya está asociada a un calendario");
        }

        // Asociar la reserva al calendario
        reserva.setCalendario(calendario);
        calendario.getReservas().add(reserva);

        // Guardar los cambios en la base de datos
        reservaRepository.save(reserva);
        calendarioRepository.save(calendario);

        
        return reserva;
    }




}