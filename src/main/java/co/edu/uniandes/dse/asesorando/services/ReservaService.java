package co.edu.uniandes.dse.asesorando.services;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

        @Transactional
        public ReservaEntity crearReserva(LocalDate fechaReserva, EstudianteEntity estudiante, AsesoriaEntity asesoria) throws EntityNotFoundException { 
               
            if (fechaReserva == null) {
                throw new EntityNotFoundException("La fecha de la reserva no puede ser nula");
            }
            if (estudiante == null) {
                throw new EntityNotFoundException("El estudiante no puede ser nulo");
            }
            if (asesoria == null) {
                throw new EntityNotFoundException("La asesoría no puede ser nula");
            }
    
            ReservaEntity reserva = new ReservaEntity();
    
            reserva.setFechaReserva(fechaReserva);
            reserva.setEstudiante(estudiante);
            reserva.setAsesoria(asesoria);
        
            return reservaRepository.save(reserva);
        }

        @Transactional
        public ReservaEntity getReserva(Long id) throws EntityNotFoundException {
            return reservaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("No se encontró la reserva con el ID: " + id));
        }
    
        @Transactional
        public List<ReservaEntity> listarReservas() {
            return reservaRepository.findAll();
        }   

        @Transactional
        public void eliminarReserva(Long id) throws EntityNotFoundException {

            if (id == null) {
                throw new EntityNotFoundException("El ID de la reserva no puede ser nulo");
            }

            ReservaEntity reservaEliminar = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Este id no existe."));
            reservaRepository.delete(reservaEliminar);
    }


        @Transactional
        public ReservaEntity updateReserva(Long id, LocalDate fechaReservaNueva, EstudianteEntity estudianteNuevo, AsesoriaEntity asesoriaNueva) throws EntityNotFoundException {
            
            ReservaEntity reservaUpdate = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Este id no existe."));

            if (id == null) {throw new EntityNotFoundException("El ID de la reserva no puede ser nulo");}
            if (fechaReservaNueva == null) {throw new EntityNotFoundException("La nueva fecha de la reserva no puede ser nula");}
            if (estudianteNuevo == null) {throw new EntityNotFoundException("El nuevo estudiante no puede ser nulo");}
            if (asesoriaNueva == null) {throw new EntityNotFoundException("La nueva asesoría no puede ser nula");}

            reservaUpdate.setFechaReserva(fechaReservaNueva);
            reservaUpdate.setEstudiante(estudianteNuevo);
            reservaUpdate.setAsesoria(asesoriaNueva);

            return reservaRepository.save(reservaUpdate);
        }


    }


