package co.edu.uniandes.dse.asesorando.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import java.util.*;

public interface EstudianteRepository extends JpaRepository<EstudianteEntity, Long>{

    /*Obtiene un estudiante específico buscado por tipo. */
    <T extends EstudianteEntity> List<T> findByTipo(String tipo);
    /*Obtiene un estudiante específico buscado por correo. */
    <T extends EstudianteEntity> Optional<T> findByCorreo(String correo);
    /*Obtiene un estudiante específico buscado por nombre. */
    <T extends EstudianteEntity> Optional<T> findByNombre(String nombre);
}