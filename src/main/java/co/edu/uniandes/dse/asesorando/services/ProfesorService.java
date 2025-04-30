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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorPresencialEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorVirtualEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con el repositorio de Profesor
 *
 * @author Daniel-VergaraM
 */
@Slf4j
@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    private static final List<String> tipos = List.of("PROFESOR", "PROFESORVIRTUAL", "PROFESORPRESENCIAL");

    /**
     * Metodo para registrar un profesor por medio de sus atributos base
     *
     * @param nombre
     * @param correo
     * @param contrasena
     * @return
     */
    @Transactional
    public ProfesorEntity createProfesor(@Valid @NotNull String nombre, @Valid @NotNull String correo, @Valid @NotNull String contrasena, @Valid @NotNull String tipo) throws EntityNotFoundException {
        ProfesorEntity profesor = new ProfesorEntity();
        profesor.setNombre(nombre);
        profesor.setCorreo(correo);
        profesor.setContrasena(contrasena);

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findByCorreo(correo);

        if (profesorExistente.isPresent()) {
            throw new EntityNotFoundException("El profesor ya esta registrado.");
        }

        if (!tipos.contains(tipo)) {
            throw new EntityNotFoundException("El tipo de profesor no es valido.");
        }

        log.info("Profesor creado");
        return profesorRepository.save(profesor);
    }

    /**
     * Metodo para registrar un profesor por medio de un objeto profesor
     *
     * @param profesor
     * @return
     */
    @Transactional
    public ProfesorEntity createProfesor(@Valid @NotNull ProfesorEntity profesor, @Valid @NotNull String tipo) throws EntityNotFoundException {
        log.info("Registrando un profesor nuevo");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesor.getId());

        if (profesorExistente.isPresent()) {
            log.error("El profesor ya esta registrado.");
            throw new EntityNotFoundException("El profesor ya esta registrado.");
        }

        if (!tipos.contains(tipo)) {
            log.error("El tipo de profesor no es valido.");
            throw new EntityNotFoundException("El tipo de profesor no es valido.");
        }

        log.info("Profesor creado");
        return profesorRepository.save(profesor);
    }

    /**
     * Metodo para actualizar un profesor por medio de su id y un objeto
     * profesor
     *
     * @param id
     * @param profesor
     * @return
     */
    @Transactional
    public ProfesorEntity updateProfesor(@NotNull Long id, @Valid @NotNull ProfesorEntity profesor) throws EntityNotFoundException {
        log.info("Actualizando un profesor");

        ProfesorEntity profesorExistente = profesorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El profesor no existe."));

        profesorExistente.setNombre(profesor.getNombre());
        profesorExistente.setCorreo(profesor.getCorreo());
        profesorExistente.setTelefono(profesor.getTelefono());
        profesorExistente.setContrasena(profesor.getContrasena());
        profesorExistente.setFotoUrl(profesor.getFotoUrl());
        profesorExistente.setVideoUrl(profesor.getVideoUrl());
        // Profesor
        profesorExistente.setTipo(profesor.getTipo());
        profesorExistente.setFormacion(profesor.getFormacion());
        profesorExistente.setExperiencia(profesor.getExperiencia());
        profesorExistente.setPrecioHora(profesor.getPrecioHora());
        // Relaciones
        profesorExistente.setTematicas(profesor.getTematicas());
        profesorExistente.setAsesorias(profesor.getAsesorias());
        profesorExistente.setCalendario(profesor.getCalendario());

        if (profesor instanceof ProfesorVirtualEntity && profesorExistente instanceof ProfesorVirtualEntity) {
            ((ProfesorVirtualEntity) profesorExistente)
                    .setEnlaceReunion(((ProfesorVirtualEntity) profesor).getEnlaceReunion());
        }

        if (profesor instanceof ProfesorPresencialEntity && profesorExistente instanceof ProfesorPresencialEntity) {
            ((ProfesorPresencialEntity) profesorExistente)
                    .setCodigoPostal(((ProfesorPresencialEntity) profesor).getCodigoPostal());
            ((ProfesorPresencialEntity) profesorExistente)
                    .setLatitud(((ProfesorPresencialEntity) profesor).getLatitud());
            ((ProfesorPresencialEntity) profesorExistente)
                    .setLongitud(((ProfesorPresencialEntity) profesor).getLongitud());
        }
        log.info("Profesor actualizado");
        return profesorRepository.save(profesorExistente);
    }

    /**
     * Metodo para eliminar un profesor por medio de su id
     *
     * @param profesorId
     */
    @Transactional
    public void deleteProfesor(@NotNull Long profesorId) throws EntityNotFoundException {
        log.info("Eliminando un profesor");

        ProfesorEntity profesorExistente = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException("El profesor no existe."));

        log.info("Profesor eliminado");
        profesorRepository.deleteById(profesorExistente.getId());
    }

    /**
     * Metodo para obtener un profesor por medio de su id
     *
     * @param profesorId
     * @return
     */
    @Transactional
    public ProfesorEntity getProfesor(Long profesorId) throws EntityNotFoundException {
        log.info("Obteniendo un profesor");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesorId);

        if (profesorExistente.isEmpty()) {
            throw new EntityNotFoundException("El profesor no existe.");
        }

        log.info("Profesor obtenido");
        return profesorExistente.get();
    }

    /**
     * Metodo para obtener todos los profesores
     *
     * @return
     */
    @Transactional
    public <T extends ProfesorEntity> Iterable<T> getProfesores() throws EntityNotFoundException {
        log.info("Obteniendo todos los profesores");
        List<T> profesores = new ArrayList<>();
        profesores.addAll(profesorRepository.findByTipo("PROFESORVIRTUAL"));
        profesores.addAll(profesorRepository.findByTipo("PROFESOR"));
        profesores.addAll(profesorRepository.findByTipo("PROFESORPRESENCIAL"));
        log.info("Profesores obtenidos");
        return profesores;
    }

    /**
     * Metodo para obtener un profesor por medio de su correo
     *
     * @param correo
     * @return
     */
    @Transactional
    public <T extends ProfesorEntity> T getProfesorPorCorreo(String correo) throws EntityNotFoundException {
        log.info("Obteniendo un profesor por correo");
        T profesorExistente = (T) profesorRepository.findByCorreo(correo).orElseThrow(() -> new EntityNotFoundException("El profesor no existe."));

        log.info("Profesor obtenido");
        return profesorExistente;
    }

    /**
     * Metodo para obtener un profesor por medio de su nombre
     *
     * @param nombre
     * @return
     */
    @Transactional
    public <T extends ProfesorEntity> T getProfesorPorNombre(String nombre) throws EntityNotFoundException {
        log.info("Obteniendo un profesor por nombre");
        Optional<T> profesorExistente = profesorRepository.findByNombre(nombre);
        if (profesorExistente.isEmpty()) {
            throw new EntityNotFoundException("El profesor no existe.");
        }

        log.info("Profesor obtenido");
        return profesorExistente.get();
    }

    /**
     * Metodo para obtener un profesor por medio de su tematica
     *
     * @param tematica
     * @return
     */
    @Transactional
    public Iterable<ProfesorEntity> getProfesorPorTematica(String tematica) {
        log.info("Obteniendo un profesor por tematica");
        List<ProfesorEntity> profesores = new ArrayList<>();
        profesores.addAll(profesorRepository.findAll());
        profesores.removeIf(
                profesor -> profesor.getTematicas()
                        .stream().noneMatch(t -> t.getTema()
                        .equals(tematica)));
        log.info("Profesores obtenidos");
        return profesores;
    }

    /**
     * Metodo para obtener varios profesores por medio de su tipo
     *
     * @param tipo
     * @return
     */
    @Transactional
    public List<ProfesorEntity> getProfesoresPorTipo(String tipo) throws EntityNotFoundException {
        log.info("Obteniendo un profesor por tipo");
        List<ProfesorEntity> profesores = new ArrayList<>();
        if (!tipos.contains(tipo)) {
            throw new EntityNotFoundException("El tipo de profesor no es valido.");
        }
        profesores.addAll(profesorRepository.findByTipo(tipo));
        log.info("Profesores obtenidos");
        return profesores;
    }

    /**
     * Metodo para obtener un profesor por medio de su tipo y tematica
     *
     * @param tipo
     * @param tematica
     * @return
     */
    @Transactional
    public Iterable<ProfesorEntity> getProfesorPorTipoTematica(String tipo, String tematica) throws EntityNotFoundException {
        log.info("Obteniendo un profesor por tipo y tematica");
        List<ProfesorEntity> profesores = new ArrayList<>();
        if (!tipos.contains(tipo)) {
            throw new EntityNotFoundException("El tipo de profesor no es valido.");
        }
        profesores.addAll(profesorRepository.findByTipo(tipo));
        profesores.removeIf(
                profesor -> profesor.getTematicas()
                        .stream().noneMatch(t -> t.getTema()
                        .equals(tematica)));
        log.info("Profesores obtenidos");
        return profesores;
    }

}
