package co.edu.uniandes.dse.asesorando.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;


public interface TematicaRepository extends JpaRepository<TematicaEntity, Long>{
    Set<TematicaEntity> findByTematica(String tematica);
    Set<TematicaEntity> findByArea(String area);
}
