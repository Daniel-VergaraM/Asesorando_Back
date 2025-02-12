package co.edu.uniandes.dse.asesorando.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;


@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
    ReservaEntity completarReserva(String estado);
    ReservaEntity cancelarReserva(boolean cancelada);
}
