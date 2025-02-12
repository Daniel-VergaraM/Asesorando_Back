package co.edu.uniandes.dse.asesorando.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import java.util.List;
import java.util.Date;


public interface CalendarioRepository extends JpaRepository<CalendarioEntity, Long> {

List<CalendarioEntity> findByFechaInicio(Date fechaInicio);
List<CalendarioEntity> findByFechaFin(Date fechaFin);
List<CalendarioEntity> findByFechaInicioGreaterThan(Date fechaInicio);
List<CalendarioEntity> findByFechaInicioLessThan(Date fechaInicio);
List<CalendarioEntity> creaCalendarioEntities(List<CalendarioEntity> calendarioEntities);
}
