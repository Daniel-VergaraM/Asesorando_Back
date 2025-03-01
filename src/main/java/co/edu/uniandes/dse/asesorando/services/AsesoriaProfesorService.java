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
 import lombok.Data;
 import lombok.extern.slf4j.Slf4j;
 import jakarta.transaction.Transactional;
 
 /**
  * Servicio que maneja la relación entre Asesorías y Profesores.
  */
 @Slf4j
 @Data
 @Service
 public class AsesoriaProfesorService {
 
     @Autowired
     private AsesoriaRepository asesoriaRepository;
 
     @Autowired
     private ProfesorRepository profesorRepository;
 
     /**
      * Crea una asesoría y la asigna a un profesor.
      *
      * @param profesorId ID del profesor.
      * @param asesoria   Entidad de la asesoría a crear.
      * @return Asesoría creada y asignada al profesor.
      */
     @Transactional
     public AsesoriaEntity crearAsesoriaParaProfesor(Long profesorId, AsesoriaEntity asesoria) throws EntityNotFoundException {
         ProfesorEntity profesor = profesorRepository.findById(profesorId)
                 .orElseThrow(() -> new EntityNotFoundException("El profesor no existe"));
 
         asesoria.setProfesor(profesor);
         return asesoriaRepository.save(asesoria);
     }
 
     /**
      * Lista todas las asesorías asignadas a un profesor,en este cso seria el get.
      *
      * @param profesorId ID del profesor.
      * @return Lista de asesorías del profesor.
      */
     @Transactional
     public List<AsesoriaEntity> listarAsesoriasDeProfesor(Long profesorId) {
         return asesoriaRepository.findByProfesorId(profesorId);
     }
 
     /**
      * Actualiza una asesoría de un profesor.
      *
      * @param profesorId   ID del profesor.
      * @param asesoriaId   ID de la asesoría a actualizar.
      * @param nuevaAsesoria Datos nuevos de la asesoría.
      * @return Asesoría actualizada.
      */
     @Transactional
     public AsesoriaEntity actualizarAsesoriaDeProfesor(Long profesorId, Long asesoriaId, AsesoriaEntity nuevaAsesoria) throws EntityNotFoundException {
         AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new EntityNotFoundException("La asesoría no existe"));
 
         if (!asesoria.getProfesor().getId().equals(profesorId)) {
             throw new EntityNotFoundException("La asesoría no pertenece a este profesor");
         }
 
         asesoria.setDuracion(nuevaAsesoria.getDuracion());
         asesoria.setTematica(nuevaAsesoria.getTematica());
         asesoria.setTipo(nuevaAsesoria.getTipo());
         asesoria.setArea(nuevaAsesoria.getArea());
         return asesoriaRepository.save(asesoria);
     }
 
     /**
      * Elimina una asesoría de un profesor.
      *
      * @param profesorId ID del profesor.
      * @param asesoriaId ID de la asesoría a eliminar.
      */
     @Transactional
     public void eliminarAsesoriaDeProfesor(Long profesorId, Long asesoriaId) throws EntityNotFoundException{
         AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new EntityNotFoundException("La asesoría no existe"));
 
         if (!asesoria.getProfesor().getId().equals(profesorId)) {
             throw new EntityNotFoundException("La asesoría no pertenece a este profesor");
         }
 
         asesoriaRepository.delete(asesoria);
     }
 }
 