package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfesorCalendarioService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private CalendarioRepository calendarioRepository;

    @Transactional
    public CalendarioEntity addCalendario(Long profesorId, Long calendarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar un calendario al profesor con id = {}", profesorId);

        if (!profesorRepository.existsById(profesorId)) {
            throw new EntityNotFoundException("No se encontró el profesor con id: " + profesorId);
        }

        if (!calendarioRepository.existsById(calendarioId)) {
            throw new EntityNotFoundException("No se encontró el calendario con id: " + calendarioId);
        }

        ProfesorEntity profesor = profesorRepository.findById(profesorId).orElseThrow(()
                -> new EntityNotFoundException("No se encontró el profesor con id: " + profesorId));

        CalendarioEntity calendario = calendarioRepository.findById(calendarioId).get();

        calendario.setProfesor(profesor);
        profesor.getCalendario().add(calendario);
        profesorRepository.save(profesor);
        calendarioRepository.save(calendario);

        log.info("Termina proceso de asociar un calendario al profesor con id = {}", profesorId);
        return calendario;
    }

    @Transactional
    public List<CalendarioEntity> getCalendarios(Long profesorId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los calendarios del profesor con id = {}", profesorId);

        if (!profesorRepository.existsById(profesorId)) {
            throw new EntityNotFoundException("No se encontró el profesor con id: " + profesorId);
        }

        ProfesorEntity profesor = profesorRepository.findById(profesorId).get();
        List<CalendarioEntity> calendarios = calendarioRepository.findByProfesor(profesor);

        log.info("Termina proceso de consultar todos los calendarios del profesor con id = {}", profesorId);
        return calendarios;
    }

    @Transactional
    public CalendarioEntity getCalendario(Long profesorId, Long calendarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el calendario con id = {} del profesor con id = {}", calendarioId, profesorId);

        if (!profesorRepository.existsById(profesorId)) {
            throw new EntityNotFoundException("No se encontró el profesor con id: " + profesorId);
        }

        if (!calendarioRepository.existsById(calendarioId)) {
            throw new EntityNotFoundException("No se encontró el calendario con id: " + calendarioId);
        }

        ProfesorEntity profesor = profesorRepository.findById(profesorId).get();
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId).get();

        if (!calendario.getProfesor().equals(profesor)) {
            throw new EntityNotFoundException("El calendario no está asociado a este profesor.");
        }

        if (!profesor.getCalendario().contains(calendario)) {
            throw new EntityNotFoundException("El calendario no está asociado a este profesor.");
        }

        log.info("Termina proceso de consultar el calendario con id = {} del profesor con id = {}", calendarioId, profesorId);
        return calendario;
    }

    @Transactional
    public void removeCalendario(Long profesorId, Long calendarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar un calendario del profesor con id = {}", profesorId);

        if (!profesorRepository.existsById(profesorId)) {
            throw new EntityNotFoundException("No se encontró el profesor con id: " + profesorId);
        }

        if (!calendarioRepository.existsById(calendarioId)) {
            throw new EntityNotFoundException("No se encontró el calendario con id: " + calendarioId);
        }

        ProfesorEntity profesor = profesorRepository.findById(profesorId).get();
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId).get();

        if (!calendario.getProfesor().equals(profesor)) {
            throw new EntityNotFoundException("El calendario no está asociado a este profesor.");
        }

        calendario.setProfesor(null);
        profesor.getCalendario().remove(calendario);
        profesorRepository.save(profesor);
        calendarioRepository.save(calendario);

        log.info("Finaliza proceso de eliminar un calendario del profesor con id = {}", profesorId);
    }
}
