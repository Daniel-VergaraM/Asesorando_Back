package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;

import java.util.*;
import co.edu.uniandes.dse.asesorando.repositories.EstudianteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    private String exceptionPartString = "El estudiante ya se encuentra registrado.";

    @Transactional
    public EstudianteEntity createEstudianteByAtributes(String nombre, String correo, String contrasena) throws EntityNotFoundException {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);
        estudiante.setContrasena(contrasena);

        Optional<EstudianteEntity> estudianteExistente = estudianteRepository.findByCorreo(correo);

        if (estudianteExistente.isPresent()) {
            throw new EntityNotFoundException(exceptionPartString);
        }

        log.info("Estudiante creado exitosamente.");
        return estudianteRepository.save(estudiante);
    }

    @Transactional
    public EstudianteEntity createEstudianteByObject(EstudianteEntity estudiante) throws EntityNotFoundException {
        log.info("Se está registrando un estudiante nuevo...");

        Optional<EstudianteEntity> estudianteExistente = estudianteRepository.findById(estudiante.getId());

        if (estudianteExistente.isPresent()) {
            log.error(exceptionPartString);
            throw new EntityNotFoundException(exceptionPartString);
        }

        log.info("Estudiante creado exitosamente.");
        return estudianteRepository.save(estudiante);
    }

    @Transactional
	public List<EstudianteEntity> getEstudiante() {
		log.info("Inicia proceso de consultar todos los estudiantes");
		return estudianteRepository.findAll();
	}

    @Transactional
    public EstudianteEntity getEstudiante(Long estudianteId) throws EntityNotFoundException {
        log.info("Se está obteniendo un estudiante...");

        Optional<EstudianteEntity> estudianteExistente = estudianteRepository.findById(estudianteId);

        if (estudianteExistente.isEmpty()) {
            throw new EntityNotFoundException("El estudiante que se quiere obtener no existe.");
        }

        log.info("Estudiante obtenido exitosamente.");
        return estudianteExistente.get();
    }

    @Transactional
    public List<EstudianteEntity> getEstudiantes() {
        log.info("Se están obteniendo todos los estudiantes...");

        List<EstudianteEntity> estudiantes =  new ArrayList<>();
        estudiantes.addAll(estudianteRepository.findByTipo("ESTUDIANTE"));

        log.info("Estudiantes obtenidos exitosamente.");
        return estudiantes;
    }

    @Transactional
    public EstudianteEntity updateEstudianteById(Long id, EstudianteEntity estudiante) throws EntityNotFoundException{
        log.info("Se está actualizando un estudiante...");

        EstudianteEntity estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El estudiante que se quiere actualizar no existe."));

        estudianteExistente.setNombre(estudiante.getNombre());
        estudianteExistente.setCorreo(estudiante.getCorreo());
        estudianteExistente.setContrasena(estudiante.getContrasena());

        log.info("Estudiante actualizado exitosamente.");
        return estudianteRepository.save(estudianteExistente);
    }

    @Transactional
    public void deleteEstudiante(Long estudianteId) throws EntityNotFoundException {
        log.info("Se está eliminando un estudiante...");

        EstudianteEntity estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("El estudiante que se quiere eliminar no existe."));

        log.info("Estudiante eliminado exitosamente.");
        estudianteRepository.deleteById(estudianteExistente.getId());
    }
}
