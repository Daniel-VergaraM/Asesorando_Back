package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
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
    public ReservaEntity crearReservaEnCalendario(Long calendarioId, ReservaEntity reserva) throws EntityNotFoundException {
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario no existe"));

        reserva.setCalendario(calendario);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public List<ReservaEntity> listarReservasDeCalendario(Long calendarioId) {
        return reservaRepository.findByCalendarioId(calendarioId);
    }

    @Transactional
    public ReservaEntity actualizarReservaEnCalendario(Long calendarioId, Long reservaId, ReservaEntity nuevaReserva) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        if (!reserva.getCalendario().getId().equals(calendarioId)) {
            throw new EntityNotFoundException("La reserva no pertenece a este calendario");
        }

        reserva.setFechaReserva(nuevaReserva.getFechaReserva());
        return reservaRepository.save(reserva);
    }

    @Transactional
    public void eliminarReservaDeCalendario(Long calendarioId, Long reservaId) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        if (!reserva.getCalendario().getId().equals(calendarioId)) {
            throw new EntityNotFoundException("La reserva no pertenece a este calendario");
        }

        reservaRepository.delete(reserva);
    }

}