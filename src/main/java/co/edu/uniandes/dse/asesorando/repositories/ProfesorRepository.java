package co.edu.uniandes.dse.asesorando.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;

@Repository
public interface ProfesorRepository extends JpaRepository<ProfesorEntity, Long>{
    ProfesorEntity findByNombre(String nombre);
    ProfesorEntity findByTipo(String tipo);
    Set<TematicaEntity> findByTematica(String tematica);
}

