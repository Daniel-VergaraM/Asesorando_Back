package co.edu.uniandes.dse.asesorando.repositories;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;


@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
    List<ReservaEntity> findByfechaReserva(LocalDate fecha);
    List<ReservaEntity> findByEstado(String estado);
    List<ReservaEntity> findByCalendarioId(Long calendarioId);
    
}
