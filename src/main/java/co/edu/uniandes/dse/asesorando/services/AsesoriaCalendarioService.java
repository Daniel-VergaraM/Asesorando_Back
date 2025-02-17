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
 import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
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
      */
     @Transactional
     public AsesoriaEntity crearAsesoriaEnCalendario(Long calendarioId, AsesoriaEntity asesoria) {
         CalendarioEntity calendario = calendarioRepository.findById(calendarioId)
                 .orElseThrow(() -> new IllegalArgumentException("El calendario no existe"));
 
         asesoria.setCalendario(calendario);
         return asesoriaRepository.save(asesoria);
     }
 
     /**
      * Lista todas las asesorías asociadas a un calendario.
      *
      * @param calendarioId ID del calendario.
      * @return Lista de asesorías en el calendario.
      */
     @Transactional
     public List<AsesoriaEntity> listarAsesoriasDeCalendario(Long calendarioId) {
         return asesoriaRepository.findByCalendarioId(calendarioId);
     }
 
     /**
      * Actualiza una asesoría dentro de un calendario.
      *
      * @param calendarioId  ID del calendario.
      * @param asesoriaId    ID de la asesoría a actualizar.
      * @param nuevaAsesoria Datos nuevos de la asesoría.
      * @return Asesoría actualizada.
      */
     @Transactional
     public AsesoriaEntity actualizarAsesoriaEnCalendario(Long calendarioId, Long asesoriaId, AsesoriaEntity nuevaAsesoria) {
         AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new IllegalArgumentException("La asesoría no existe"));
 
         if (!asesoria.getCalendario().getId().equals(calendarioId)) {
             throw new IllegalArgumentException("La asesoría no pertenece a este calendario");
         }
 
         asesoria.setDuracion(nuevaAsesoria.getDuracion());
         asesoria.setTematica(nuevaAsesoria.getTematica());
         asesoria.setTipo(nuevaAsesoria.getTipo());
         asesoria.setArea(nuevaAsesoria.getArea());
         return asesoriaRepository.save(asesoria);
     }
 
     /**
      * Elimina una asesoría de un calendario.
      *
      * @param calendarioId ID del calendario.
      * @param asesoriaId   ID de la asesoría a eliminar.
      */
     @Transactional
     public void eliminarAsesoriaDeCalendario(Long calendarioId, Long asesoriaId) {
         AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId)
                 .orElseThrow(() -> new IllegalArgumentException("La asesoría no existe"));
 
         if (!asesoria.getCalendario().getId().equals(calendarioId)) {
             throw new IllegalArgumentException("La asesoría no pertenece a este calendario");
         }
 
         asesoriaRepository.delete(asesoria);
     }
 }
 