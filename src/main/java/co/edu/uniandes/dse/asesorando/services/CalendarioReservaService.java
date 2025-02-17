package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;

public class CalendarioReservaService {
    @Autowired
    private CalendarioRepository calendarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional
    public CalendarioEntity crearCalendario(CalendarioEntity calendario) {
        return calendarioRepository.save(calendario);
    }

    @Transactional
    public List<CalendarioEntity> listarCalendarios() {
        return calendarioRepository.findAll();
    }

    @Transactional
    public CalendarioEntity actualizarCalendario(Long calendarioId, CalendarioEntity nuevoCalendario) {
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new IllegalArgumentException("El calendario no existe"));

        calendario.setFechaFin(nuevoCalendario.getFechaFin());
        calendario.setFechaInicio(nuevoCalendario.getFechaInicio());
        return calendarioRepository.save(calendario);
    }

    @Transactional
    public void eliminarCalendarioYReservas(Long calendarioId) {
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new IllegalArgumentException("El calendario no existe"));

        List<ReservaEntity> reservas = reservaRepository.findByCalendarioId(calendarioId);
        reservaRepository.deleteAll(reservas);

        calendarioRepository.delete(calendario);
    }
}
