package co.edu.uniandes.dse.asesorando.services;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.dto.ReservaDTO;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import co.edu.uniandes.dse.asesorando.repositories.EstudianteRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ReservaService {


    @Autowired
    private ReservaRepository reservaRepository;

     @Autowired
    private EstudianteRepository estudianteRepository;


    @Autowired
    private AsesoriaRepository asesoriaRepository;



        @Transactional
        public ReservaEntity crearReserva(ReservaDTO reservaDTO) throws EntityNotFoundException {
        ReservaEntity reserva = new ReservaEntity();

        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setCancelada(reservaDTO.getCancelada() != null ? reservaDTO.getCancelada() : false);
        reserva.setEstado(reservaDTO.getEstado() != null ? reservaDTO.getEstado() : "noCompletada");

        // Buscar y asignar estudiante
        EstudianteEntity estudiante = estudianteRepository.findById(reservaDTO.getEstudianteId())
            .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));
        reserva.setEstudiante(estudiante);

        // Buscar y asignar asesoria
        AsesoriaEntity asesoria = asesoriaRepository.findById(reservaDTO.getAsesoriaId())
            .orElseThrow(() -> new EntityNotFoundException("Asesoria no encontrada"));
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
        public ReservaEntity updateReserva(Long id, LocalDateTime fechaReservaNueva, EstudianteEntity estudianteNuevo, AsesoriaEntity asesoriaNueva) throws EntityNotFoundException {
            
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

        public ReservaEntity marcarComoCompletada(Long id) throws EntityNotFoundException {
            ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));
            
            reserva.setEstado("Completada");
            return reservaRepository.save(reserva);
        }

        public ReservaEntity marcarComoCancelada(Long id) throws EntityNotFoundException {
            ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));
            
            reserva.setCancelada(true);
            return reservaRepository.save(reserva);
        }


    }


