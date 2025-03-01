package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.EstudianteRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class EstudianteReservaService {
    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional
    public ReservaEntity crearReserva(Long estudianteId, ReservaEntity nuevaReserva) throws EntityNotFoundException {
        EstudianteEntity estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("El estudiante no existe"));

        nuevaReserva.setEstudiante(estudiante);
        return reservaRepository.save(nuevaReserva);
    }

    @Transactional
    public List<ReservaEntity> listarReservasPorEstudiante(Long estudianteId) {
        return reservaRepository.findByEstudianteId(estudianteId);
    }

    @Transactional
    public ReservaEntity actualizarReserva(Long reservaId, ReservaEntity reservaActualizada) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        reserva.setFechaReserva(reservaActualizada.getFechaReserva()); 
        return reservaRepository.save(reserva);
    }

    @Transactional
    public void eliminarReserva(Long reservaId) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        reservaRepository.delete(reserva);
    }
}
