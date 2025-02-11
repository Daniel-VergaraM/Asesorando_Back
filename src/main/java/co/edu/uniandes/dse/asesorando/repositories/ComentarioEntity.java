package co.edu.uniandes.dse.asesorando.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ComentarioEntity extends  JpaRepository<ComentarioEntity, Long> {
    
    
}
