package co.edu.uniandes.dse.asesorando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import co.edu.uniandes.dse.asesorando.repositories.TematicaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfesorTematicaService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private TematicaRepository tematicaRepository;

    @Transactional
    public TematicaEntity agregarTematicaAProfesor(Long profesorId, Long tematicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de agregar temática a profesor");

        Optional<TematicaEntity> tematica = tematicaRepository.findById(tematicaId);
        if (tematica.isEmpty()) {
            throw new EntityNotFoundException("No se encontró la temática con ID: " + tematicaId);
        }

        Optional<ProfesorEntity> profesor = profesorRepository.findById(profesorId);
        if (profesor.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el profesor con ID: " + profesorId);
        }

        profesor.get().getTematicas().add(tematica.get());
        return tematica.get();
    }

    @Transactional
    public List<TematicaEntity> getTematicasDeProfesor(Long profesorId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar temáticas de profesor");

        Optional<ProfesorEntity> profesor = profesorRepository.findById(profesorId);
        if (profesor.isEmpty()) {
            throw new EntityNotFoundException("Profesor no encontrado con ID: " + profesorId);
        }

        return profesor.get().getTematicas();
    }

    @Transactional
    public TematicaEntity getTematicaDeProfesor(Long profesorId, Long tematicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar una temática específica de un profesor");

        Optional<ProfesorEntity> profesor = profesorRepository.findById(profesorId);
        if (profesor.isEmpty()) {
            throw new EntityNotFoundException("Profesor no encontrado con ID: " + profesorId);
        }

        for (TematicaEntity tematica : profesor.get().getTematicas()) {
            if (tematica.getId().equals(tematicaId)) {
                return tematica;
            }
        }

        throw new EntityNotFoundException("La temática no está asociada con este profesor");
    }

    @Transactional
    public List<TematicaEntity> actualizarProfesorTematicas(Long profesorId, List<TematicaEntity> tematicas)
            throws EntityNotFoundException {
        Optional<ProfesorEntity> profesor = profesorRepository.findById(profesorId);
        if (profesor.isEmpty()) {
            throw new EntityNotFoundException("Profesor no encontrado");
        }

        profesor.get().setTematicas(tematicas);
        return profesor.get().getTematicas();
    }

    @Transactional
    public void eliminarTematicaDeProfesor(Long profesorId, Long tematicaId) throws EntityNotFoundException {
        Optional<ProfesorEntity> profesor = profesorRepository.findById(profesorId);
        Optional<TematicaEntity> tematica = tematicaRepository.findById(tematicaId);

        if (profesor.isEmpty() || tematica.isEmpty()) {
            throw new EntityNotFoundException("Profesor o Temática no encontrados");
        }

        profesor.get().getTematicas().remove(tematica.get());
    }
}
