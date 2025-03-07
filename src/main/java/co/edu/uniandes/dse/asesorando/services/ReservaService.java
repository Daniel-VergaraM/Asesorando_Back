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
    
            ReservaEntity reservaGuardada = reservaRepository.save(reserva);
    
            return reservaGuardada;
        }
    
        @Transactional
        public String toString(LocalDate fechaReserva, EstudianteEntity estudiante, AsesoriaEntity asesoria) throws EntityNotFoundException {
            return "ReservaEntity{" +
                "fechaReserva='" + fechaReserva + '\'' +
                ", estudiante=" + (estudiante != null ? estudiante : "N/A") +
                ", asesoria=" + (asesoria != null ? asesoria : "N/A") +
                '}';
        }
    
        @Transactional
        public List<ReservaEntity> listarReservas() {
            return reservaRepository.findAll();
        }   

        @Transactional
        public void eliminarReserva(Long Id) throws EntityNotFoundException {

            if (Id == null) {
                throw new EntityNotFoundException("El ID de la reserva no puede ser nulo");
            }

            ReservaEntity reservaEliminar = reservaRepository.findById(Id).orElseThrow(() -> new EntityNotFoundException("Este id no existe."));
            reservaRepository.delete(reservaEliminar);
    }


        @Transactional
        public ReservaEntity updateReserva(Long Id, LocalDate fechaReservaNueva, EstudianteEntity estudianteNuevo, AsesoriaEntity asesoriaNueva) throws EntityNotFoundException {
            
            ReservaEntity reservaUpdate = reservaRepository.findById(Id).orElseThrow(() -> new EntityNotFoundException("Este id no existe."));

            if (Id == null) {throw new EntityNotFoundException("El ID de la reserva no puede ser nulo");}
            if (fechaReservaNueva == null) {throw new EntityNotFoundException("La nueva fecha de la reserva no puede ser nula");}
            if (estudianteNuevo == null) {throw new EntityNotFoundException("El nuevo estudiante no puede ser nulo");}
            if (asesoriaNueva == null) {throw new EntityNotFoundException("La nueva asesoría no puede ser nula");}

            reservaUpdate.setFechaReserva(fechaReservaNueva);
            reservaUpdate.setEstudiante(estudianteNuevo);
            reservaUpdate.setAsesoria(asesoriaNueva);

            ReservaEntity reservaGuardada = reservaRepository.save(reservaUpdate);

            return reservaGuardada;
        }


    }


