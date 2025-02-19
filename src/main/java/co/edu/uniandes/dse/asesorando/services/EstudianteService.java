package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import java.util.*;
import co.edu.uniandes.dse.asesorando.repositories.EstudianteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Transactional
    public EstudianteEntity createEstudianteByAtributes(String nombre, String correo, String contrasena){
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);
        estudiante.setContrasena(contrasena);

        Optional<EstudianteEntity> estudianteExistente = estudianteRepository.findByCorreo(correo);

        if (estudianteExistente.isPresent()) {
            throw new IllegalArgumentException("El estudiante ya se encuentra registrado.");
        }

        log.info("Estudiante creado exitosamente.");
        return estudianteRepository.save(estudiante);
    }

    @Transactional
    public EstudianteEntity createEstudianteByObject(EstudianteEntity estudiante){
        log.info("Se está registrando un estudiante nuevo...");

        Optional<EstudianteEntity> estudianteExistente = estudianteRepository.findById(estudiante.getId());

        if (estudianteExistente.isPresent()) {
            log.error("El estudiante ya se encuentra registrado.");
            throw new IllegalArgumentException("El estudiante ya se encuentra registrado.");
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
    public EstudianteEntity getEstudiante(Long estudianteId) {
        log.info("Se está obteniendo un estudiante...");

        Optional<EstudianteEntity> estudianteExistente = estudianteRepository.findById(estudianteId);

        if (estudianteExistente.isEmpty()) {
            throw new IllegalArgumentException("El estudiante que se quiere obtener no existe.");
        }

        log.info("Estudiante obtenido exitosamente.");
        return estudianteExistente.get();
    }

    @Transactional
    public <T extends EstudianteEntity> Iterable<T> getEstudiantes() {
        log.info("Se están obteniendo todos los estudiantes...");

        Set<T> estudiantes = Set.of();
        estudiantes.addAll(estudianteRepository.findByTipo("ESTUDIANTE"));

        log.info("Estudiantes obtenidos exitosamente.");
        return estudiantes;
    }

    @Transactional
    public EstudianteEntity updateEstudianteById(Long id, EstudianteEntity estudiante){
        log.info("Se está actualizando un estudiante...");

        EstudianteEntity estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El estudiante que se quiere actualizar no existe."));

        estudianteExistente.setNombre(estudiante.getNombre());
        estudianteExistente.setCorreo(estudiante.getCorreo());
        estudianteExistente.setContrasena(estudiante.getContrasena());

        log.info("Estudiante actualizado exitosamente.");
        return estudianteRepository.save(estudianteExistente);
    }

    @Transactional
    public void deleteEstudiante(Long estudianteId) {
        log.info("Se está eliminando un estudiante...");

        EstudianteEntity estudianteExistente = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("El estudiante que se quiere eliminar no existe."));

        log.info("Estudiante eliminado exitosamente.");
        estudianteRepository.deleteById(estudianteExistente.getId());
    }
}
