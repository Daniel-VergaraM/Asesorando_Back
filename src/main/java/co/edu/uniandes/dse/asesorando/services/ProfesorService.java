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

    private String exceptionPartString = "El profesor ya esta registrado.";

    private String exceptionPartString2 = "El tipo de profesor no es valido.";

    private String exceptionPartString3 = "El profesor no existe.";

    private String exceptionPartString4 = "Profesores obtenidos: ";

    /**
     * Metodo para registrar un profesor por medio de sus atributos base
     *
     * @param nombre
     * @param correo
     * @param contrasena
     * @return
     */
    @Transactional
    public ProfesorEntity createProfesor(@Valid @NotNull String nombre, @Valid @NotNull String correo,
            @Valid @NotNull String contrasena, @Valid @NotNull String tipo) throws EntityNotFoundException {
        ProfesorEntity profesor = new ProfesorEntity();
        profesor.setNombre(nombre);
        profesor.setCorreo(correo);
        profesor.setContrasena(contrasena);

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findByCorreo(correo);

        if (profesorExistente.isPresent()) {
            throw new EntityNotFoundException(exceptionPartString);
        }

        if (!tipos.contains(tipo)) {
            throw new EntityNotFoundException(exceptionPartString2);
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
    public ProfesorEntity createProfesor(@Valid @NotNull ProfesorEntity profesor, @Valid @NotNull String tipo)
            throws EntityNotFoundException {
        log.info("Registrando un profesor nuevo");

        Optional<ProfesorEntity> profesorExistente = profesorRepository.findById(profesor.getId());

        if (profesorExistente.isPresent()) {
            log.error(exceptionPartString);
            throw new EntityNotFoundException(exceptionPartString);
        }

        if (!tipos.contains(tipo)) {
            log.error(exceptionPartString2);
            throw new EntityNotFoundException(exceptionPartString2);
        }

        log.info("Profesor creado");
        return profesorRepository.save(profesor);
    }

    /**
     * Metodo para actualizar un profesor por medio de su id y un objeto profesor
     *
     * @param id
     * @param profesor
     * @return
     */
    @Transactional
    public ProfesorEntity updateProfesor(@NotNull Long id, @Valid @NotNull ProfesorEntity profesor)
            throws EntityNotFoundException {
        log.info("Actualizando un profesor");

        ProfesorEntity profesorExistente = profesorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3));

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

        // Correctly handle collections with orphanRemoval=true
        // Instead of replacing the asesorias collection, we modify it in-place
        if (profesor.getAsesorias() != null) {
            // Clear and add instead of replacing the collection reference
            profesorExistente.getAsesorias().clear();
            if (!profesor.getAsesorias().isEmpty()) {
                profesorExistente.getAsesorias().addAll(profesor.getAsesorias());
            }
        }

        // Handle calendario collection in the same way
        if (profesor.getCalendario() != null) {
            profesorExistente.getCalendario().clear();
            if (!profesor.getCalendario().isEmpty()) {
                profesorExistente.getCalendario().addAll(profesor.getCalendario());
            }
        }
        if (profesor instanceof ProfesorVirtualEntity profesorVirtual
                && profesorExistente instanceof ProfesorVirtualEntity profesorExistenteVirtual) {
            profesorExistenteVirtual.setEnlaceReunion(profesorVirtual.getEnlaceReunion());
        }
        if (profesor instanceof ProfesorPresencialEntity profesorPresencial
                && profesorExistente instanceof ProfesorPresencialEntity profesorExistentePresencial) {
            profesorExistentePresencial.setCodigoPostal(profesorPresencial.getCodigoPostal());
            profesorExistentePresencial.setLatitud(profesorPresencial.getLatitud());
            profesorExistentePresencial.setLongitud(profesorPresencial.getLongitud());
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
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3));

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
            throw new EntityNotFoundException(exceptionPartString3);
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
    public List<ProfesorEntity> getProfesores() throws EntityNotFoundException {
        log.info("Obteniendo todos los profesores");
        List<ProfesorEntity> profesores = profesorRepository.findAll();
        log.info(exceptionPartString4 + profesores.size());
        return profesores;
    }

    /**
     * Metodo para obtener un profesor por medio de su correo
     *
     * @param correo
     * @return
     */
    @Transactional
    public ProfesorEntity getProfesorPorCorreo(String correo) throws EntityNotFoundException {
        log.info("Obteniendo un profesor por correo");
        return profesorRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3));
    }

    /**
     * Metodo para obtener un profesor por medio de su nombre
     *
     * @param nombre
     * @return
     */
    @Transactional
    public ProfesorEntity getProfesorPorNombre(String nombre) throws EntityNotFoundException {
        log.info("Obteniendo un profesor por nombre");
        return profesorRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3));
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
        profesores.removeIf(profesor -> profesor.getTematicas().stream().noneMatch(t -> t.getTema().equals(tematica)));
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
        log.info("Obteniendo profesores por tipo: " + tipo);

        if (!tipos.contains(tipo)) {
            throw new EntityNotFoundException(exceptionPartString2);
        }

        List<ProfesorEntity> profesores = profesorRepository.findByTipo(tipo);
        log.info(exceptionPartString4 + profesores.size());
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
    public Iterable<ProfesorEntity> getProfesorPorTipoTematica(String tipo, String tematica)
            throws EntityNotFoundException {
        log.info("Obteniendo profesores por tipo y tematica: " + tipo + ", " + tematica);

        if (!tipos.contains(tipo)) {
            throw new EntityNotFoundException(exceptionPartString2);
        }

        // Use the repository method to find professors by type first
        List<ProfesorEntity> profesoresPorTipo = profesorRepository.findByTipo(tipo);

        // Then filter by tematica
        List<ProfesorEntity> profesores = profesoresPorTipo.stream().filter(profesor -> profesor.getTematicas() != null
                && profesor.getTematicas().stream().anyMatch(t -> t.getTema() != null && t.getTema().equals(tematica)))
                .toList();

        log.info(exceptionPartString4 + profesores.size());
        return profesores;
    }

}
