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

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con el repositorio de Usuario
 *
 * @author Daniel-VergaraM
 */
@Slf4j
@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Transactional
    public ProfesorEntity registrarProfesor(@Valid @NotNull ProfesorEntity profesor) {
        log.info("Registrando un profesor nuevo");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesor.getId());

        if (profesorExistente.isPresent()) {
            throw new IllegalArgumentException("El profesor ya esta registrado.");
        }

        log.info("Profesor creado");
        return profesorRepository.save(profesor);
    }

    @Transactional
    public ProfesorEntity actualizarProfesor(@Valid @NotNull ProfesorEntity profesor) {
        log.info("Actualizando un profesor");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesor.getId());

        if (profesorExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        log.info("Profesor actualizado");
        return profesorRepository.save(profesor);
    }

    @Transactional
    public void eliminarProfesor(Long profesorId) {
        log.info("Eliminando un profesor");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesorId);

        if (profesorExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        log.info("Profesor eliminado");
        profesorRepository.deleteById(profesorId);
    }

    @Transactional
    public ProfesorEntity obtenerProfesor(Long profesorId) {
        log.info("Obteniendo un profesor");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesorId);

        if (profesorExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        log.info("Profesor obtenido");
        return profesorExistente.get();
    }

    @Transactional
    public <T extends ProfesorEntity> Iterable<T> obtenerProfesores() {
        log.info("Obteniendo todos los profesores");
        Set<T> profesores = Set.of();
        profesores.addAll(profesorRepository.findByTipo("PROFESORVIRTUAL"));
        profesores.addAll(profesorRepository.findByTipo("PROFESOR"));
        profesores.addAll(profesorRepository.findByTipo("PROFESORPRESENCIAL"));
        log.info("Profesores obtenidos");
        return profesores;
    }

    @Transactional
    public <T extends ProfesorEntity> T obtenerProfesorPorCorreo(String correo) {
        log.info("Obteniendo un profesor por correo");
        Optional<T> profesorExistente = profesorRepository.findByCorreo(correo);
        if (profesorExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        log.info("Profesor obtenido");
        return (T) profesorExistente.get();
    }

    @Transactional
    public <T extends ProfesorEntity> T obtenerProfesorPorNombre(String nombre) {
        log.info("Obteniendo un profesor por nombre");
        Optional<T> profesorExistente = profesorRepository.findByNombre(nombre);
        if (profesorExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        log.info("Profesor obtenido");
        return profesorExistente.get();
    }

    @Transactional
    public Iterable<ProfesorEntity> obtenerProfesorPorTematica(String tematica) {
        log.info("Obteniendo un profesor por tematica");
        Set<ProfesorEntity> profesores = Set.of();
        profesores.addAll(profesorRepository.findAll());
        profesores.removeIf(
                profesor -> profesor.getTematicas()
                        .stream().noneMatch(t -> t.getTema()
                        .equals(tematica)));
        log.info("Profesores obtenidos");
        return profesores;
    }

    @Transactional
    public Iterable<ProfesorEntity> obtenerProfesorPorTipo(String tipo) {
        log.info("Obteniendo un profesor por tipo");
        Set<ProfesorEntity> profesores = Set.of();
        profesores.addAll(profesorRepository.findByTipo(tipo));
        log.info("Profesores obtenidos");
        return profesores;
    }

    @Transactional
    public Iterable<ProfesorEntity> obtenerProfesorPorTipoAndTematica(String tipo, String tematica) {
        log.info("Obteniendo un profesor por tipo y tematica");
        Set<ProfesorEntity> profesores = Set.of();
        profesores.addAll(profesorRepository.findByTipo(tipo));
        profesores.removeIf(
                profesor -> profesor.getTematicas()
                        .stream().noneMatch(t -> t.getTema()
                        .equals(tematica)));
        log.info("Profesores obtenidos");
        return profesores;
    }

}
