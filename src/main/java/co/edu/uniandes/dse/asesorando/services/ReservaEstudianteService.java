package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class ReservaEstudianteService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional
    public ReservaEntity crearReserva(ReservaEntity nuevaReserva) {
        return reservaRepository.save(nuevaReserva);
    }

    @Transactional
    public List<ReservaEntity> listarReservas() {
        return reservaRepository.findAll();
    }

    @Transactional
    public EstudianteEntity obtenerEstudiantePorReserva(Long reservaId) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        return reserva.getEstudiante(); // Retorna el estudiante relacionado
    }

    @Transactional
    public List<ReservaEntity> listarReservasPorEstudiante(Long estudianteId) {
        return reservaRepository.findByEstudianteId(estudianteId);
    }

    @Transactional
    public void eliminarReserva(Long reservaId) throws EntityNotFoundException {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("La reserva no existe"));

        reservaRepository.delete(reserva);
    }

}
