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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

import java.util.stream.Collectors;

 /**
 * Clase que implementa la conexión con el repositorio de Servicio
 *
 * @author Juan Caicedo
 */
@Slf4j
@Service
public class AsesoriaService {
    /**
     * Conexión con el repositorio de Servicio Asesoria
     */
    @Autowired
    AsesoriaRepository asesoriaRepository;

    @Autowired
    ProfesorRepository profesorRepository;

        /**
         * Crea una nueva asesoría en el servicio.
         * @param asesoria   Entidad de la asesoría a crear.
         * @param profesorId ID del profesor asociado (no puede ser nulo).
         * @return La entidad de la asesoría creada.
         * @throws IllegalOperationException Si algún campo obligatorio está vacío o si el profesor no existe.
         */
        @Transactional
        public AsesoriaEntity createAsesoria(@Valid @NotNull AsesoriaEntity asesoria, @NotNull Long profesorId) throws IllegalOperationException {
            log.info("Iniciando creación de asesoría");
            ProfesorEntity profesor = profesorRepository.findById(profesorId).orElseThrow(() -> new IllegalOperationException("El profesor con ID " + profesorId + " no existe."));
            asesoria.setProfesor(profesor);
            asesoria.setCompletada(false);
            return asesoriaRepository.save(asesoria);
            
        }
    /**
     * Obtiene todas las asesoría con base a su id
     * @param asesoriaId id de la asesoría
     * @return asesoriaEntity
     * @throws IllegalOperationException si la asesoría no existe
     */

    @Transactional
    public AsesoriaEntity getAsesoriaEntity(@NotNull Long asesoriaId) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesoria con id = {}", asesoriaId);
        
        return asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new IllegalOperationException("La asesoria con el id proporcionado no esta en el sistema"));
    }


    /**
    * Método para actualizar una asesoría por medio dediante su ID y un objeto asesoría.
     *
     * @param asesoriaId ID de la asesoría a actualizar.
     * @param asesoria   Objeto asesoría con los datos actualizados.
     * @return La asesoría actualizada.
     * @throws IllegalOperationException Si la asesoría no existe o si los datos son inválidos.
     **/

     @Transactional
     public AsesoriaEntity updateAsesoriaEntity(Long asesoriaId, AsesoriaEntity asesoria) throws EntityNotFoundException, IllegalOperationException {
         log.info("Inicia proceso de actualizar la asesoría con id = {}", asesoriaId);
<<<<<<< HEAD
         Optional<AsesoriaEntity> optionalAsesoria = asesoriaRepository.findById(asesoriaId);
         if (optionalAsesoria.isEmpty()) {throw new EntityNotFoundException("La asesoría con el ID proporcionado no está en el sistema.");}
=======
     
         Optional<AsesoriaEntity> optionalAsesoria = asesoriaRepository.findById(asesoriaId);
         if (optionalAsesoria.isEmpty()) {throw new EntityNotFoundException("La asesoría con el ID proporcionado no está en el sistema.");}
        
>>>>>>> cb2b401 (AJUSTES A SERVICIOS DE Asesoria)
         asesoria.setId(asesoriaId);
         log.info("Termina proceso de actualizar la asesoría con id = {}", asesoriaId);
         return asesoriaRepository.save(asesoria);
}
     
    
    /**
     * Elimina una asesoría con base a su id
     * @param asesoriaId id de la asesoría
     * @return asesoriaEntity
     * @throws IllegalOperationException si la asesoría no existe
     */
    @Transactional
    public AsesoriaEntity deleteAsesoriaEntity(@NotNull Long asesoriaId) throws IllegalOperationException {
        log.info("Inicia proceso de eliminación de asesoria con id = {}", asesoriaId);
        
        AsesoriaEntity asesoriaEntity = asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new IllegalOperationException("La asesoria con el id proporcionado no esta en el sistema"));
        
        asesoriaRepository.deleteById(asesoriaEntity.getId());
        return asesoriaEntity;
    }


    /**
     * Obtiene todas las asesorías con base a su área
     * @param area área de la asesoría
     * @return Lista de asesorías
     * @throws IllegalOperationException si no se encuentran asesorías con el área proporcionada
     */
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByArea(String area) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con area = {}", area);
        if (area == null || area.isBlank()) {throw new IllegalOperationException("El area no puede estar vacía.");}
        List<AsesoriaEntity> asesorias = asesoriaRepository.findByArea(area);
        if(asesorias==null || asesorias.isEmpty()){throw new IllegalOperationException("No se encontraron asesorias con el area proporcionada.");}
        return asesorias;
    }

    @Transactional
    public List<AsesoriaEntity> getAsesoriasByCompletada(Boolean completada, long profesorId) throws IllegalOperationException {
        log.info("Inicia consulta de asesorías con completada = {} y profesorId = {}", completada, profesorId);
<<<<<<< HEAD
        if (completada == null) {throw new IllegalOperationException("El valor de completada no puede ser nulo.");}
        
        List<AsesoriaEntity> asesorias = asesoriaRepository.findByProfesorId(profesorId);
        if (asesorias == null || asesorias.isEmpty()) {throw new IllegalOperationException("No se encontraron asesorías para el profesor con ID " + profesorId);}
    
        // Filtrar asesorias  completadaS
        List<AsesoriaEntity> asesoriasFiltradas = asesorias.stream().filter(asesoria -> Boolean.TRUE.equals(asesoria.getCompletada()) == completada).collect(Collectors.toCollection(ArrayList::new));
        if (asesoriasFiltradas.isEmpty()) {throw new IllegalOperationException("No se encontraron asesorías con estado " + completada + " para el profesor con ID " + profesorId);}
=======
    
        if (completada == null) {
            throw new IllegalOperationException("El valor de completada no puede ser nulo.");
        }
    
        List<AsesoriaEntity> asesorias = asesoriaRepository.findByProfesorId(profesorId);
        
        if (asesorias == null || asesorias.isEmpty()) {
            throw new IllegalOperationException("No se encontraron asesorías para el profesor con ID " + profesorId);
        }
    
        // Filtrar asesorías por estado de completada
        List<AsesoriaEntity> asesoriasFiltradas = asesorias.stream().filter(asesoria -> Boolean.TRUE.equals(asesoria.getCompletada()) == completada).collect(Collectors.toCollection(ArrayList::new));
    
        if (asesoriasFiltradas.isEmpty()) {
            throw new IllegalOperationException("No se encontraron asesorías con estado " + completada + " para el profesor con ID " + profesorId);
        }
>>>>>>> cb2b401 (AJUSTES A SERVICIOS DE Asesoria)
    
        return asesoriasFiltradas;
    }
    
    
    /**
     * Obtiene todas las asesorías con base a su profesorId
     * @param profesorId profesorId de la asesoría
     * @return Lista de asesorías
     * @throws IllegalOperationException si no se encuentran asesorías con el profesorId proporcionado
     */
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByProfesorId(Long profesorId) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con profesorId = {}", profesorId);
        if (profesorId == null) {throw new IllegalOperationException("El id del profesor no puede ser nulo.");}
        List<AsesoriaEntity> asesorias = asesoriaRepository.findByProfesorId(profesorId);
        if(asesorias==null || asesorias.isEmpty()){throw new IllegalOperationException("No se encontraron asesorías con el profesor proporcionado");}
        
        return asesorias;
    }

    /**
     * Obtiene todas las asesorías con base a su calendarioId
     * @param calendarioId calendarioId de la asesoría
     * @return Lista de asesorías
     * @throws IllegalOperationException si no se encuentran asesorías con el calendarioId 
     */
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByCalendarioId(Long calendarioId) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con calendarioId = {}", calendarioId);
        if (calendarioId == null) {
            throw new IllegalOperationException("El id del calendario no puede ser nulo.");
        }
        if (calendarioId <= 0) {
            throw new IllegalOperationException("El id del calendario no puede ser negativo.");
        }
        List<AsesoriaEntity> asesorias = asesoriaRepository.findByCalendarioId(calendarioId);
        if(asesorias==null || asesorias.isEmpty()){
            throw new IllegalOperationException("No se encontraron asesorías con el calendario");
        }
        return asesorias;
    }



    public List<AsesoriaEntity> getAllAsesorias() {

        return asesoriaRepository.findAll();
    }
    

    
}
