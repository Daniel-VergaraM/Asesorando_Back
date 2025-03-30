package co.edu.uniandes.dse.asesorando.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;

@Repository
public interface AsesoriaRepository extends JpaRepository<AsesoriaEntity, Long> {

    List<AsesoriaEntity> findByTematica(String tematica);

    List<AsesoriaEntity> findByProfesorId(Long profesorId);

    List<AsesoriaEntity> findByCalendarioId(Long calendarioId);

    List<AsesoriaEntity> findByArea(String area);

    List<AsesoriaEntity> findByCompletada(Boolean completada);

}
