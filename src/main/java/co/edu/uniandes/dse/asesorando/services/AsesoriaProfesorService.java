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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import lombok.extern.slf4j.Slf4j;
import jakarta.transaction.Transactional;

/**
 * Servicio que maneja la relación entre Asesorías y Profesores.
 */
@Slf4j
@Service
public class AsesoriaProfesorService {

    /** LAS CONEXIONES RESPECTIVAS */
    @Autowired
    private AsesoriaRepository asesoriaRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    private String exceptionPartString = "El profesor con ID ";

    private String exceptionPartString2 = " no existe.";
    private String exceptionPartString3 = "La asesoría con ID ";

    /**
     * Asigna una asesoría existente a un profesor.
     *
     * @param profesorId ID del profesor.
     * @param asesoriaId ID de la asesoría a asignar.
     * @return Asesoría asignada al profesor.
     * @throws EntityNotFoundException Si el profesor o la asesoría no existen.
     */
    @Transactional
    public AsesoriaEntity crearAsesoriaParaProfesor(Long profesorId, Long asesoriaId) throws EntityNotFoundException {
        log.info("Asignando asesoría ID {} al profesor ID {}", asesoriaId, profesorId);

        ProfesorEntity profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString + profesorId + exceptionPartString2));

        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3 + asesoriaId + exceptionPartString2));

        // Validar si la asesoría ya tiene un profesor asignado
        if (asesoria.getProfesor() != null) {
            throw new EntityNotFoundException("La asesoría ya está asignada a un profesor.");
        }

        asesoria.setProfesor(profesor);
        return asesoriaRepository.save(asesoria);
    }

    /**
     * Lista todas las asesorías asignadas a un profesor.
     *
     * @param profesorId ID del profesor.
     * @return Lista de asesorías del profesor.
     * @throws EntityNotFoundException Si el profesor no existe.
     */
    @Transactional
    public List<AsesoriaEntity> listarAsesoriasDeProfesor(Long profesorId) throws EntityNotFoundException {
        log.info("Listando asesorías del profesor ID {}", profesorId);

        if (!profesorRepository.existsById(profesorId)) {
            throw new EntityNotFoundException(exceptionPartString + profesorId + exceptionPartString2);
        }

        return asesoriaRepository.findByProfesorId(profesorId);
    }

    /**
     * Actualiza una asesoría de un profesor.
     *
     * @param profesorId    ID del profesor.
     * @param asesoriaId    ID de la asesoría a actualizar.
     * @param nuevaAsesoria Datos nuevos de la asesoría.
     * @return Asesoría actualizada.
     * @throws EntityNotFoundException Si la asesoría o el profesor no existen o no
     *                                 están asociados.
     */
    @Transactional
    public AsesoriaEntity actualizarAsesoriaDeProfesor(Long profesorId, Long asesoriaId, AsesoriaEntity nuevaAsesoria)
            throws EntityNotFoundException {
        log.info("Actualizando asesoría ID {} del profesor ID {}", asesoriaId, profesorId);

        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3 + asesoriaId + exceptionPartString2));

        if (!asesoria.getProfesor().getId().equals(profesorId)) {
            throw new EntityNotFoundException("La asesoría no pertenece a este profesor.");
        }

        // Asignar el ID correcto a la nueva asesoría
        nuevaAsesoria.setId(asesoriaId);
        nuevaAsesoria.setProfesor(asesoria.getProfesor()); // Mantener el mismo profesor
        AsesoriaEntity asesoriaActualizada = asesoriaRepository.save(nuevaAsesoria);

        log.info("Asesoría ID {} actualizada correctamente.", asesoriaId);
        return asesoriaActualizada;
    }

    /**
     * Elimina una asesoría de un profesor.
     *
     * @param profesorId ID del profesor.
     * @param asesoriaId ID de la asesoría a eliminar.
     * @throws EntityNotFoundException Si la asesoría no existe o no pertenece al
     *                                 profesor.
     */
    @Transactional
    public void eliminarAsesoriaDeProfesor(Long profesorId, Long asesoriaId) throws EntityNotFoundException {
        log.info("Eliminando asesoría ID {} del profesor ID {}", asesoriaId, profesorId);

        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionPartString3 + asesoriaId + exceptionPartString2));

        if (asesoria.getProfesor() == null || !asesoria.getProfesor().getId().equals(profesorId)) {
            throw new EntityNotFoundException("La asesoría no pertenece a este profesor.");
        }

        asesoriaRepository.delete(asesoria);
        log.info("Asesoría ID {} eliminada correctamente.", asesoriaId);
    }
}
