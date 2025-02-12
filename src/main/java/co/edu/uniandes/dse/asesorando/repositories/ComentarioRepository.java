package co.edu.uniandes.dse.asesorando.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;


@Repository
public interface ComentarioRepository extends  JpaRepository<ComentarioEntity, Long> {
    
    
}
