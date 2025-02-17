package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service

public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    
        @Transactional
        public ReservaEntity crearReserva(LocalDate fechaReserva, EstudianteEntity estudiante, AsesoriaEntity asesoria){ 
               ReservaEntity reserva = new ReservaEntity();
    
               reserva.setFechaReserva(fechaReserva);
               reserva.setEstudiante(estudiante);
               reserva.setAsesoria(asesoria);
    
               ReservaEntity reservaGuardada = reservaRepository.save(reserva);
    
               return reservaGuardada;
        }
    
        @Transactional
        public String toString(LocalDate fechaReserva, EstudianteEntity estudiante, AsesoriaEntity asesoria) {
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
        public void eliminarReserva(Long Id) {
            ReservaEntity reservaEliminar = reservaRepository.findById(Id).orElseThrow(() -> new IllegalArgumentException("Este id no existe."));
            reservaRepository.delete(reservaEliminar);

        }

        @Transactional
        public ReservaEntity updateReserva(Long Id, LocalDate fechaReservaNueva, EstudianteEntity estudianteNueva, AsesoriaEntity asesoriaNueva){
            ReservaEntity reservaUpdate = reservaRepository.findById(Id).orElseThrow(() -> new IllegalArgumentException("Este id no existe."));

            reservaUpdate.setFechaReserva(fechaReservaNueva);
            reservaUpdate.setEstudiante(estudianteNueva);
            reservaUpdate.setAsesoria(asesoriaNueva);

            ReservaEntity reservaGuardada = reservaRepository.save(reservaUpdate);

            return reservaGuardada;
        }
        







    

    }


