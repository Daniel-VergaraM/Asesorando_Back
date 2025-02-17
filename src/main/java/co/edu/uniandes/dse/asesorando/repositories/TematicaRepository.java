package co.edu.uniandes.dse.asesorando.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;

/**
 * Interfaz que define las operaciones sobre la tabla de tematicas
 * 
 * @author @Daniel-VergaraM
 */
public interface TematicaRepository extends JpaRepository<TematicaEntity, Long> {

    /**
     * Obtiene una tematica por su tema
     * @param tema
     * @return
     */
    Optional<TematicaEntity> findByTema(String tema);

    /**
     * Obtiene todas las tematicas por su area
     * @param area
     * @return
     */
    Set<TematicaEntity> findByArea(String area);

    /**
     * Obtiene una tematica por su tema y area
     * @param tema
     * @param area
     * @return
     */
    Optional<TematicaEntity> findByTemaAndArea(String tema, String area);
}
