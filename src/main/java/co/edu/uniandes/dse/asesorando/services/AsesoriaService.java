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
 
 /**
  * Clase que implementa la conexión con el repositorio de Servicio Asesoría.
  *
  * @author Juan Caicedo
  */
 @Slf4j
 @Service
 public class AsesoriaService {
 
     /** Conexión con el repositorio de Servicio Asesoría */
     @Autowired
     private AsesoriaRepository asesoriaRepository;
 
     @Autowired
     private ProfesorRepository profesorRepository;
 
     /**
      * Crea una nueva asesoría en el servicio.
      * 
      * @param asesoria   Entidad de la asesoría a crear.
      * @param profesorId ID del profesor asociado (no puede ser nulo).
      * @return La entidad de la asesoría creada.
      * @throws IllegalOperationException Si algún campo obligatorio está vacío o si el profesor no existe.
      */
     @Transactional
     public AsesoriaEntity createAsesoria(@Valid @NotNull AsesoriaEntity asesoria, @NotNull Long profesorId) 
             throws IllegalOperationException {
         log.info("Iniciando creación de asesoría");
 
         ProfesorEntity profesor = profesorRepository.findById(profesorId)
                 .orElseThrow(() -> new IllegalOperationException("El profesor con ID " + profesorId + " no existe."));
 
         asesoria.setProfesor(profesor);
         asesoria.setCompletada(false);
 
         return asesoriaRepository.save(asesoria);
     }
 
     /**
      * Obtiene una asesoría por su ID.
      * 
      * @param asesoriaId ID de la asesoría.
      * @return AsesoriaEntity.
      * @throws IllegalOperationException Si la asesoría no existe.
      */
     @Transactional
     public AsesoriaEntity getAsesoriaEntity(@NotNull Long asesoriaId) throws IllegalOperationException {
         log.info("Inicia proceso de consulta de asesoria con id = {}", asesoriaId);
 
         return asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new IllegalOperationException("La asesoría con el ID proporcionado no está en la base de datos."));
     }
 
     /**
      * Método para actualizar una asesoría mediante su ID y un objeto asesoría.
      *
      * @param asesoriaId ID de la asesoría a actualizar.
      * @param asesoria   Objeto asesoría con los datos actualizados.
      * @return La asesoría actualizada.
      * @throws IllegalOperationException Si la asesoría no existe o si los datos son inválidos.
      **/
     @Transactional
     public AsesoriaEntity updateAsesoriaEntity(Long asesoriaId, AsesoriaEntity asesoria)
             throws EntityNotFoundException, IllegalOperationException {
         log.info("Inicia proceso de actualizar la asesoría con id = {}", asesoriaId);
 
         AsesoriaEntity dummy = asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new EntityNotFoundException("La asesoría con el ID proporcionado no está en el sistema."));
 
         asesoria.setId(dummy.getId());
         log.info("Termina proceso de actualizar la asesoría con id = {}", asesoriaId);
         
         return asesoriaRepository.save(asesoria);
     }
 
     /**
      * Elimina una asesoría por su ID.
      * 
      * @param asesoriaId ID de la asesoría.
      * @return AsesoriaEntity.
      * @throws IllegalOperationException Si la asesoría no existe.
      */
     @Transactional
     public AsesoriaEntity deleteAsesoriaEntity(@NotNull Long asesoriaId) throws IllegalOperationException {
         log.info("Inicia proceso de eliminación de asesoria con id = {}", asesoriaId);
 
         AsesoriaEntity asesoriaEntity = asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new IllegalOperationException("La asesoría con el ID proporcionado no está en el sistema."));
 
         asesoriaRepository.deleteById(asesoriaEntity.getId());
         return asesoriaEntity;
     }
 
     /**
      * Obtiene todas las asesorías por área.
      * 
      * @param area Área de la asesoría.
      * @return Lista de asesorías.
      * @throws IllegalOperationException Si no se encuentran asesorías con el área proporcionada.
      */
     @Transactional
     public List<AsesoriaEntity> getAsesoriasByArea(String area) throws IllegalOperationException {
         log.info("Inicia proceso de consulta de asesorías con área = {}", area);
 
         if (area == null || area.isBlank()) {
             throw new IllegalOperationException("El área no puede estar vacía.");
         }
 
         List<AsesoriaEntity> asesorias = asesoriaRepository.findByArea(area);
         if (asesorias.isEmpty()) {
             throw new IllegalOperationException("No se encontraron asesorías con el área proporcionada.");
         }
         return asesorias;
     }
 
     @Transactional
     public List<AsesoriaEntity> getAsesoriasByCompletada(Boolean completada, long profesorId) throws IllegalOperationException {
         log.info("Inicia consulta de asesorías con completada = {} y profesorId = {}", completada, profesorId);
 
         if (completada == null) {
             throw new IllegalOperationException("El valor de completada no puede ser nulo.");
         }
 
         List<AsesoriaEntity> asesorias = asesoriaRepository.findByProfesorId(profesorId);
         List<AsesoriaEntity> asesoriasFiltradas = asesorias.stream()
                 .filter(asesoria -> Boolean.TRUE.equals(asesoria.getCompletada()) == completada)
                 .collect(Collectors.toCollection(ArrayList::new));
 
         if (asesoriasFiltradas.isEmpty()) {
             throw new IllegalOperationException("No se encontraron asesorías con estado " + completada + " para el profesor con ID " + profesorId);
         }
 
         return asesoriasFiltradas;
     }
 
     /**
      * Obtiene todas las asesorías de la base de datos.
      */
     public List<AsesoriaEntity> getAllAsesorias() {
         return asesoriaRepository.findAll();
     }
     
 }
 