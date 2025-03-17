package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class CalendarioService {
    
    @Autowired
    CalendarioRepository calendarioRepository;

  @Transactional
    public CalendarioEntity createCalendario(CalendarioEntity calendarioEntity) throws IllegalOperationException {
    log.info("Inicia proceso de creación del calendario");
    if (calendarioEntity.getFechaInicio() == null || calendarioEntity.getFechaFin() == null) {
        throw new IllegalOperationException("El calendario debe tener una fecha de inicio y una fecha de fin");
    }
    log.info("Termina proceso de creación del calendario");
    return calendarioRepository.save(calendarioEntity);

}

@Transactional
    public CalendarioEntity getCalendario(Long id) throws EntityNotFoundException {
    log.info("Inicia proceso de consultar el calendario con id = {0}", id);
    if(calendarioRepository.findById(id).isEmpty()){
        throw new EntityNotFoundException("No se encontró el calendario con el id = {0}");
    }
    CalendarioEntity calendarioEntity = calendarioRepository.findById(id).get();
    log.info("Termina proceso de consultar el calendario con id = {0}", id);
    return calendarioEntity;

    }
@Transactional
    public List<CalendarioEntity> getCalendarios() {
    log.info("Inicia proceso de consultar todos los calendarios");
    List<CalendarioEntity> calendarioEntity = calendarioRepository.findAll();
    log.info("Termina proceso de consultar todos los calendarios");
    return calendarioEntity;
    }
  @Transactional
    public CalendarioEntity updateCalendario(CalendarioEntity calendarioEntity) throws IllegalOperationException {
    log.info("Inicia proceso de actualización del calendario");
    if (calendarioEntity.getFechaInicio() == null || calendarioEntity.getFechaFin() == null) {
        throw new IllegalOperationException("El calendario debe tener una fecha de inicio y una fecha de fin");
    }
    log.info("Termina proceso de actualización del calendario");
    return calendarioRepository.save(calendarioEntity);
}


@Transactional
public void deleteCalendario(Long id) throws EntityNotFoundException {
    log.info("Inicia proceso de borrar calendario con id = {}", id);
    
    Optional<CalendarioEntity> optionalCalendario = calendarioRepository.findById(id);
    
    if (!optionalCalendario.isPresent()) {
        throw new EntityNotFoundException("No se encontró el calendario con id = " + id);
    }

    CalendarioEntity calendarioEntity = optionalCalendario.get(); 

    calendarioRepository.delete(calendarioEntity);

    log.info("Termina proceso de borrar el calendario con id = {}", id);
}



@Transactional
    public CalendarioEntity getCalendarioByFechaInicio(Date fechaInicio) throws IllegalOperationException {
    log.info("Inicia proceso de consultar el calendario con fecha de inicio = {0}", fechaInicio);
    if (calendarioRepository.findByFechaInicio(fechaInicio).isEmpty()) {
        throw new IllegalOperationException("No se encontró el calendario con la fecha de inicio = {0}");
    }
    CalendarioEntity calendarioEntity = calendarioRepository.findByFechaInicio(fechaInicio).get(0);
    log.info("Termina proceso de consultar el calendario con fecha de inicio = {0}", fechaInicio);
    return calendarioEntity;
}


@Transactional 
    public CalendarioEntity getCalendarioByFechaFin(Date fechaFin) throws IllegalOperationException {
    log.info("Inicia proceso de consultar el calendario con fecha de fin = {0}", fechaFin);
    if (calendarioRepository.findByFechaFin(fechaFin).isEmpty()) {
        throw new IllegalOperationException("No se encontró el calendario con la fecha de fin = {0}");
    }
    CalendarioEntity calendarioEntity = calendarioRepository.findByFechaFin(fechaFin).get(0);
    log.info("Termina proceso de consultar el calendario con fecha de fin = {0}", fechaFin);
    return calendarioEntity;

    }
  
@Transactional
    public List<CalendarioEntity> getCalendarioByFechaInicioLessThan(Date fechaInicio) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar calendarios con fecha de inicio menor a {}", fechaInicio);
        List<CalendarioEntity> calendarioEntity = calendarioRepository.findByFechaInicioLessThan(fechaInicio);
        if (calendarioEntity == null || calendarioEntity.isEmpty()) {
            log.warn("No se encontraron calendarios con fecha de inicio menor a {}", fechaInicio);
            throw new EntityNotFoundException("No se encontró el calendario con la fecha de inicio menor a {0}");
        }
        log.info("Finaliza proceso de consulta");
        return calendarioEntity;
    }
@Transactional
public List<CalendarioEntity> getCalendarioByFechaInicioBetween(Date fechaInicio, Date fechaFin) throws EntityNotFoundException {
    log.info("Inicia proceso de consultar el calendario con fecha de inicio entre {0} y {1}", fechaInicio, fechaFin);
    List<CalendarioEntity> calendarioEntity = calendarioRepository.findByFechaInicioBetween(fechaInicio, fechaFin);
    if (calendarioEntity.isEmpty() || calendarioEntity == null) {
        log.warn("No se encontraron calendarios con fecha de inicio entre {0} y {1}", fechaInicio, fechaFin);
        throw new EntityNotFoundException("No se encontró el calendario con la fecha de inicio entre {0} y {1}");
    }
    log.info("Termina proceso de consultar el calendario con fecha de inicio entre {0} y {1}", fechaInicio, fechaFin);
    return calendarioEntity;
}
@Transactional
public List<CalendarioEntity> findByProfesor(ProfesorEntity profesor){
    log.info("Inicia proceso de consultar el calendario con profesor = {0}", profesor);
    List<CalendarioEntity> calendarioEntity = calendarioRepository.findByProfesor(profesor);
    log.info("Termina proceso de consultar el calendario con profesor = {0}", profesor);
    return calendarioEntity;
}
}