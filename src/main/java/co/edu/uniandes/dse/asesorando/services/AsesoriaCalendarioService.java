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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.validation.annotation.Validated;
 
 import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
 import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
 import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
 import lombok.Data;
 import lombok.extern.slf4j.Slf4j;
 import jakarta.transaction.Transactional;
 
 /**
  * Servicio que maneja la relación entre Asesorías y Calendarios.
  */
@Slf4j
 @Data
 @Service
 
 public class AsesoriaCalendarioService {
 
     @Autowired
     private AsesoriaRepository asesoriaRepository;
 
     @Autowired
     private CalendarioRepository calendarioRepository;
 
     /**
     * Crea una asesoría y la asigna a un calendario.
     *
     * @param calendarioId ID del calendario.
     * @param asesoria     Entidad de la asesoría a crear.
     * @return Asesoría creada y asignada al calendario.
     * @throws EntityNotFoundException   Si el calendario no existe.
     * @throws IllegalOperationException Si la asesoría ya está asignada a un calendario.
     */
    @Transactional
    public AsesoriaEntity crearAsesoriaEnCalendario(@NotNull Long calendarioId, @Valid @NotNull AsesoriaEntity asesoria) throws EntityNotFoundException, IllegalOperationException {
        log.info("Iniciacion de la  creación de asesoría en el calendario con ID: {}", calendarioId);

        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no esta en la base de datos"));

        if (asesoria.getCalendario() != null) {
            throw new IllegalOperationException("La asesoría ya está asignada a un calendario");
        }

        asesoria.setCalendario(calendario);
        AsesoriaEntity resultado = asesoriaRepository.save(asesoria);

        log.info("La Asesoría ha sido creada exitosamente con respectivo ID: {}", resultado.getId());
        return resultado;
    }
 
      /**
     * Lista todas las asesorías asociadas a un calendario.
     *
     * @param calendarioId ID del calendario.
     * @return Lista de asesorías en el calendario.
     * @throws EntityNotFoundException Si el calendario no existe.
     */
    @Transactional
    public List<AsesoriaEntity> listarAsesoriasDeCalendario(@NotNull Long calendarioId) throws EntityNotFoundException {
        log.info("Listando asesorías en el calendario con ID: {}", calendarioId);

        if (!calendarioRepository.existsById(calendarioId)) {
            throw new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe en la base de datos");
        }

        return asesoriaRepository.findByCalendarioId(calendarioId);
    }

 
     /**
     * Actualiza una asesoría dentro de un calendario.
     *
     * @param calendarioId  ID del calendario.
     * @param asesoriaId    ID de la asesoría a actualizar.
     * @param nuevaAsesoria Datos nuevos de la asesoría.
     * @return Asesoría actualizada.
     * @throws EntityNotFoundException   Si el calendario o la asesoría no existen.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */
    @Transactional
    public AsesoriaEntity actualizarAsesoriaEnCalendario(@NotNull Long calendarioId, @NotNull Long asesoriaId,@Valid @NotNull AsesoriaEntity nuevaAsesoria)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Actualizando asesoría con ID: {} en el calendario con ID: {}", asesoriaId, calendarioId);

        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe"));

        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La asesoría con ID " + asesoriaId + " no existe"));

        if (!asesoria.getCalendario().equals(calendario)) {
            throw new IllegalOperationException("La asesoría no pertenece a este calendario");
        }

        asesoria.setDuracion(nuevaAsesoria.getDuracion());
        asesoria.setTematica(nuevaAsesoria.getTematica());
        asesoria.setTipo(nuevaAsesoria.getTipo());
        asesoria.setArea(nuevaAsesoria.getArea());
        asesoria.setCompletada(nuevaAsesoria.getCompletada());
        AsesoriaEntity resultado = asesoriaRepository.save(asesoria);

        log.info("Asesoría actualizada exitosamente con ID: {}", resultado.getId());
        return resultado;
    }
 
     /**
     * Elimina una asesoría de un calendario.
     *
     * @param calendarioId ID del calendario.
     * @param asesoriaId   ID de la asesoría a eliminar.
     * @throws EntityNotFoundException   Si el calendario o la asesoría no existen.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */
    @Transactional
    public void eliminarAsesoriaDeCalendario(@NotNull Long calendarioId, @NotNull Long asesoriaId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Eliminando asesoría con ID: {} del calendario con ID: {}", asesoriaId, calendarioId);

        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe"));

        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La asesoría con ID " + asesoriaId + " no existe"));

        if (!asesoria.getCalendario().equals(calendario)) {
            throw new IllegalOperationException("La asesoría no pertenece a este calendario");
        }

        asesoriaRepository.delete(asesoria);
 }
}
 