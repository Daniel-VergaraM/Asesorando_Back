/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package co.edu.uniandes.dse.asesorando.services;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import co.edu.uniandes.dse.asesorando.repositories.TematicaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con el repositorio de Tematica y Profesor
 * (Operaciones conjuntas)
 *
 * @author Daniel-VergaraM
 */
@Slf4j
@Service
public class TematicaProfesorService {

    @Autowired
    private TematicaRepository tematicaRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    private String exceptionPartString = "El profesor no existe.";
    
    private String exceptionPartString2 = "La tematica no existe.";

    /**
     * Obtiene todas las tematicas de un profesor
     *
     * @param profesorId
     * @return List<TematicaEntity>
     *
     */
    @Transactional
    public List<TematicaEntity> obtenerTematicas(@NotNull Long profesorId) throws EntityNotFoundException {
        log.info("Obteniendo todas las tematicas del profesor con id: {}", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        return profesorExistente.getTematicas();
    }   

    /**
     * Elimina a un profesor de todas las tematicas
     *
     * @param profesorId
     * @return
     */
    @Transactional
    public ProfesorEntity eliminarProfesorDeTematicas(@NotNull Long profesorId) throws EntityNotFoundException {
        log.info("Eliminando al profesor con id: {} de todas las tematicas", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        profesorExistente.getTematicas().forEach(tematica -> tematica.getProfesores().remove(profesorExistente));
        profesorExistente.getTematicas().clear();
        return profesorExistente;
    }

    /**
     * Agrega un profesor a una tematica existente y viceversa
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public ProfesorEntity agregarProfesorATematica(@NotNull Long profesorId, @NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Agregando el profesor con id: {} a la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));

        profesorExistente.getTematicas().add(tematicaExistente);
        tematicaExistente.getProfesores().add(profesorExistente);
        return profesorExistente;
    }

    /**
     * Elimina un profesor de una tematica existente y viceversa
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public ProfesorEntity eliminarProfesorDeTematica(@NotNull Long profesorId, @NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Eliminando el profesor con id: {} de la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        profesorExistente.getTematicas().remove(tematicaExistente);
        tematicaExistente.getProfesores().remove(profesorExistente);
        return profesorExistente;
    }

    /**
     * Elimina todos los profesores de una tematica
     *
     * @param tematicaId
     * @return
     */
    @Transactional
    public TematicaEntity eliminarProfesoresDeTematica(@NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Eliminando todos los profesores de la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));

        tematicaExistente.getProfesores().forEach(profesor -> profesor.getTematicas().remove(tematicaExistente));
        tematicaExistente.getProfesores().clear();
        return tematicaExistente;
    }

    /**
     * Elimina todas las tematicas de un profesor
     *
     * @param profesorId
     * @return
     */
    @Transactional
    public ProfesorEntity eliminarTematicasDeProfesor(@NotNull Long profesorId) throws EntityNotFoundException {
        log.info("Eliminando todas las tematicas del profesor con id: {}", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));

        profesorExistente.getTematicas().forEach(tematica -> tematica.getProfesores().remove(profesorExistente));
        profesorExistente.getTematicas().clear();
        return profesorExistente;
    }

    /**
     * Verifica si un profesor posee una tematica
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public Boolean profesorPoseeTematica(@NotNull Long profesorId, @NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Verificando si el profesor con id: {} posee la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        return profesorExistente.getTematicas().contains(tematicaExistente);
    }

    /**
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public ProfesorEntity getProfesorDeTematica(@NotNull Long profesorId, @NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Obteniendo el profesor con id: {} de la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        return tematicaExistente.getProfesores().stream().filter(profesor -> profesor.getId().equals(profesorExistente.getId()))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("El profesor no existe en la tematica."));
    }

    /**
     *
     * @param tematicaId
     * @return
     */
    @Transactional
    public List<ProfesorEntity> getProfesoresDeTematica(@NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Obteniendo todos los profesores de la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        return tematicaExistente.getProfesores();
    }

    /**
     *
     * @param tematicaId
     * @param profesores
     * @return
     */
    @Transactional
    public List<ProfesorEntity> actualizarTematicaProfesor(@NotNull Long tematicaId, @NotNull List<ProfesorEntity> profesores) throws EntityNotFoundException {
        log.info("Actualizando los profesores de la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        tematicaExistente.getProfesores().clear();
        profesores.forEach(profesor -> tematicaExistente.getProfesores().add(profesor));
        return tematicaExistente.getProfesores();
    }

    /**
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public TematicaEntity getTematicaDeProfesor(@NotNull Long profesorId, @NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Obteniendo la tematica con id: {} del profesor con id: {}", tematicaId, profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        return profesorExistente.getTematicas().stream().filter(tematica -> tematica.getId().equals(tematicaExistente.getId()))
                .findFirst().orElseThrow(() -> new EntityNotFoundException("La tematica no existe en el profesor."));
    }

    /**
     * Verifica si una tematica posee un profesor
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public Boolean tematicaPoseeProfesor(@NotNull Long profesorId, @NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Verificando si la tematica con id: {} posee el profesor con id: {}", tematicaId, profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        return tematicaExistente.getProfesores().contains(profesorExistente);
    }

    /**
     * Verifica si un profesor posee tematicas
     *
     * @param profesorId
     * @return
     *
     */
    @Transactional
    public Boolean profesorPoseeTematicas(@NotNull Long profesorId) throws EntityNotFoundException {
        log.info("Verificando si el profesor con id: {} posee tematicas", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString));
        return !profesorExistente.getTematicas().isEmpty();
    }

    /**
     * Verifica si una tematica posee profesores
     *
     * @param tematicaId
     * @return
     */
    @Transactional
    public Boolean tematicaPoseeProfesores(@NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Verificando si la tematica con id: {} posee profesores", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString2));
        return !tematicaExistente.getProfesores().isEmpty();
    }

}
