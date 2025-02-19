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
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
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

    /**
     * Obtiene todas las tematicas de un profesor
     *
     * @param profesorId
     * @return Set<TematicaEntity>
     *
     */
    @Transactional
    public Set<TematicaEntity> obtenerTematicas(@NotNull Long profesorId) {
        log.info("Obteniendo todas las tematicas del profesor con id: {}", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        return profesorExistente.getTematicas();
    }

    /**
     * Obtiene todos los profesores de una tematica
     *
     * @param tematicaId
     * @return List<ProfesorEntity>
     */
    @Transactional
    public List<ProfesorEntity> obtenerProfesores(@NotNull Long tematicaId) {
        log.info("Obteniendo todos los profesores de la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));
        return tematicaExistente.getProfesores();
    }

    /**
     * Elimina a un profesor de todas las tematicas
     *
     * @param profesorId
     * @return
     */
    @Transactional
    public void eliminarProfesorDeTematicas(@NotNull Long profesorId) {
        log.info("Eliminando al profesor con id: {} de todas las tematicas", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        profesorExistente.getTematicas().forEach((tematica) -> tematica.getProfesores().remove(profesorExistente));
        profesorExistente.getTematicas().clear();
    }

    /**
     * Agrega un profesor a una tematica existente y viceversa
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public void agregarProfesorATematica(@NotNull Long profesorId, @NotNull Long tematicaId) {
        log.info("Agregando el profesor con id: {} a la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));

        profesorExistente.getTematicas().add(tematicaExistente);
        tematicaExistente.getProfesores().add(profesorExistente);
    }

    /**
     * Elimina un profesor de una tematica existente y viceversa
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public void eliminarProfesorDeTematica(@NotNull Long profesorId, @NotNull Long tematicaId) {
        log.info("Eliminando el profesor con id: {} de la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));
        profesorExistente.getTematicas().remove(tematicaExistente);
        tematicaExistente.getProfesores().remove(profesorExistente);
    }

    /**
     * Elimina todos los profesores de una tematica
     *
     * @param tematicaId
     * @return
     */
    @Transactional
    public void eliminarProfesoresDeTematica(@NotNull Long tematicaId) {
        log.info("Eliminando todos los profesores de la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));

        tematicaExistente.getProfesores().forEach((profesor) -> profesor.getTematicas().remove(tematicaExistente));
        tematicaExistente.getProfesores().clear();
    }

    /**
     * Elimina todas las tematicas de un profesor
     *
     * @param profesorId
     * @return
     */
    @Transactional
    public void eliminarTematicasDeProfesor(@NotNull Long profesorId) {
        log.info("Eliminando todas las tematicas del profesor con id: {}", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));

        profesorExistente.getTematicas().forEach((tematica) -> tematica.getProfesores().remove(profesorExistente));
        profesorExistente.getTematicas().clear();
    }

    /**
     * Elimina un profesor de una tematica existente y viceversa
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public void eliminarTematicaProfesor(@NotNull Long profesorId, @NotNull Long tematicaId) {
        log.info("Eliminando el profesor con id: {} de la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));
        profesorExistente.getTematicas().remove(tematicaExistente);
        tematicaExistente.getProfesores().remove(profesorExistente);
    }

    /**
     * Verifica si un profesor posee una tematica
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public Boolean profesorPoseeTematica(@NotNull Long profesorId, @NotNull Long tematicaId) {
        log.info("Verificando si el profesor con id: {} posee la tematica con id: {}", profesorId, tematicaId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));
        return profesorExistente.getTematicas().contains(tematicaExistente);
    }

    /**
     * Verifica si una tematica posee un profesor
     *
     * @param profesorId
     * @param tematicaId
     * @return
     */
    @Transactional
    public Boolean tematicaPoseeProfesor(@NotNull Long profesorId, @NotNull Long tematicaId) {
        log.info("Verificando si la tematica con id: {} posee el profesor con id: {}", tematicaId, profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));
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
    public Boolean profesorPoseeTematicas(@NotNull Long profesorId) {
        log.info("Verificando si el profesor con id: {} posee tematicas", profesorId);
        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("El profesor no existe."));
        return !profesorExistente.getTematicas().isEmpty();
    }

    /**
     * Verifica si una tematica posee profesores
     *
     * @param tematicaId
     * @return
     */
    @Transactional
    public Boolean tematicaPoseeProfesores(@NotNull Long tematicaId) {
        log.info("Verificando si la tematica con id: {} posee profesores", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new IllegalArgumentException("La tematica no existe."));
        return !tematicaExistente.getProfesores().isEmpty();
    }

}
