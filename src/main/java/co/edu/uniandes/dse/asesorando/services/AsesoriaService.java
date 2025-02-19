package co.edu.uniandes.dse.asesorando.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsesoriaService {

    @Autowired
    AsesoriaRepository asesoriaRepository;  


    @Transactional
    public AsesoriaEntity createAsesoria(AsesoriaEntity asesoria) throws IllegalOperationException {
        log.info("Inicia proceso de creación de asesoria");
    
        if (asesoria == null || asesoria.getDuracion() == null || asesoria.getDuracion().isBlank() ||
            asesoria.getTematica() == null || asesoria.getTematica().isBlank() ||
            asesoria.getTipo() == null || asesoria.getTipo().isBlank() ||
            asesoria.getArea() == null || asesoria.getArea().isBlank()) {
            throw new IllegalOperationException("La asesoria no tiene la información necesaria para ser creada");
        }
    
        asesoria.setCompletada(false);
        
        return asesoriaRepository.save(asesoria);
    }

    @Transactional
    public AsesoriaEntity getAsesoriaEntity(Long asesoriaId) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesoria con id = {}", asesoriaId);
        
        return asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new IllegalOperationException("La asesoria con el id proporcionado no esta en el sistema"));
    }
                                    
    @Transactional
    public AsesoriaEntity updateAsesoriaEntity(Long asesoriaId, AsesoriaEntity asesoria) throws IllegalOperationException {
        log.info("Inicia proceso de actualización de asesoria con id = {}", asesoriaId);
        
        if (asesoria == null || asesoria.getDuracion() == null || asesoria.getDuracion().isBlank() ||
            asesoria.getTematica() == null || asesoria.getTematica().isBlank() ||
            asesoria.getTipo() == null || asesoria.getTipo().isBlank() ||
            asesoria.getArea() == null || asesoria.getArea().isBlank()) {
            throw new IllegalOperationException("La asesoria no tiene la información necesaria para ser actualizada");
        }
        
        AsesoriaEntity asesoriaEntity = asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new IllegalOperationException("La asesoria con el id proporcionado no esta en el sistema"));
        
        asesoriaEntity.setDuracion(asesoria.getDuracion());
        asesoriaEntity.setTematica(asesoria.getTematica());
        asesoriaEntity.setTipo(asesoria.getTipo());
        asesoriaEntity.setArea(asesoria.getArea());
        
        return asesoriaRepository.save(asesoriaEntity);
    }
    

    @Transactional
    public AsesoriaEntity deleteAsesoriaEntity(Long asesoriaId) throws IllegalOperationException {
        log.info("Inicia proceso de eliminación de asesoria con id = {}", asesoriaId);
        
        AsesoriaEntity asesoriaEntity = asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new IllegalOperationException("La asesoria con el id proporcionado no esta en el sistema"));
        
        asesoriaRepository.delete(asesoriaEntity);
        
        return asesoriaEntity;
    }
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByTematica(String tematica) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con tematica = {}", tematica);
        
        return asesoriaRepository.findByTematica(tematica);
    }
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByArea(String area) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con area = {}", area);
        
        return asesoriaRepository.findByArea(area);
    }
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByCompletada(Boolean completada) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con completada = {}", completada);
        
        return asesoriaRepository.findByCompletada(completada);
    }
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByProfesorId(Long profesorId) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con profesorId = {}", profesorId);
        
        return asesoriaRepository.findByProfesorId(profesorId);
    }
    @Transactional
    public List <AsesoriaEntity> getAsesoriasByCalendarioId(Long calendarioId) throws IllegalOperationException {
        log.info("Inicia proceso de consulta de asesorias con calendarioId = {}", calendarioId);
        
        return asesoriaRepository.findByCalendarioId(calendarioId);
    }
    

    
}
