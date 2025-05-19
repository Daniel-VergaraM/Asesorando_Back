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
  implementado por @JuanCaicedo
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
    public AsesoriaEntity crearAsesoriaEnCalendario(@NotNull Long calendarioId, @NotNull Long asesoriaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Iniciacion de la  creación de asesoría en el calendario con ID: {}", calendarioId);
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId).orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no esta en la base de datos"));
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new EntityNotFoundException("La asesoría con ID " + asesoriaId + " no esta en la base de datos"));
        asesoria.setCalendario(calendario);
        calendario.getAsesorias().add(asesoria);
        asesoriaRepository.save(asesoria);
        calendarioRepository.save(calendario);
        return asesoria;
    }
    /**
     * Obtiene todas las asesorías asociadas a un calendario.
     **/
 
    @Transactional
    public List<AsesoriaEntity> getAsesoriasByCalendarioId(@NotNull Long calendarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consulta de asesorías para el calendario con ID = {}", calendarioId);
        
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe."));

        return calendario.getAsesorias();
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
     */    @Transactional
    public AsesoriaEntity updateAsesoriaInCalendario(@NotNull Long calendarioId, @NotNull Long asesoriaId, AsesoriaEntity nuevaAsesoria) throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Inicia proceso de actualización de asesoría con ID = {} en el calendario con ID = {}", asesoriaId, calendarioId);
        
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe."));
        
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La asesoría con ID " + asesoriaId + " no existe."));
        
        if (asesoria.getCalendario() == null || !asesoria.getCalendario().getId().equals(calendario.getId())) {
            throw new IllegalOperationException("La asesoría no pertenece a este calendario.");
        }
        
        // Si hay datos nuevos para actualizar
        if (nuevaAsesoria != null) {
            if (nuevaAsesoria.getTematica() != null) 
                asesoria.setTematica(nuevaAsesoria.getTematica());
            if (nuevaAsesoria.getDuracion() != null) 
                asesoria.setDuracion(nuevaAsesoria.getDuracion());
            if (nuevaAsesoria.getTipo() != null) 
                asesoria.setTipo(nuevaAsesoria.getTipo());
            if (nuevaAsesoria.getArea() != null) 
                asesoria.setArea(nuevaAsesoria.getArea());
        }
        
        // La asesoría ya pertenece al calendario, actualizamos sus datos
        AsesoriaEntity asesoriaActualizada = asesoriaRepository.save(asesoria);
        calendarioRepository.save(calendario);

        log.info("Finaliza proceso de actualización de asesoría con ID = {}", asesoriaId);
        return asesoriaActualizada;
    }

    /**
     * Sobrecarga del método para mantener compatibilidad con código existente.
     * 
     * @param calendarioId ID del calendario.
     * @param asesoriaId ID de la asesoría a actualizar.
     * @return Asesoría actualizada.
     * @throws EntityNotFoundException Si el calendario o la asesoría no existen.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */
    @Transactional
    public AsesoriaEntity updateAsesoriaInCalendario(@NotNull Long calendarioId, @NotNull Long asesoriaId) throws EntityNotFoundException, IllegalOperationException {
        return updateAsesoriaInCalendario(calendarioId, asesoriaId, null);
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
    public void deleteAsesoriaFromCalendario(@NotNull Long calendarioId, @NotNull Long asesoriaId)
            throws EntityNotFoundException, IllegalOperationException {
        
        log.info("Inicia proceso de eliminación de asesoría con ID = {} del calendario con ID = {}", asesoriaId, calendarioId);
        
        CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                .orElseThrow(() -> new EntityNotFoundException("El calendario con ID " + calendarioId + " no existe."));
        
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La asesoría con ID " + asesoriaId + " no existe."));

        if (asesoria.getCalendario() == null || !asesoria.getCalendario().equals(calendario)) {
            throw new IllegalOperationException("La asesoría no pertenece a este calendario.");
        }

        calendario.getAsesorias().remove(asesoria);
        asesoriaRepository.deleteById(asesoriaId);

        log.info("Termina proceso de eliminación de asesoría con ID = {}", asesoriaId);
    }
}
