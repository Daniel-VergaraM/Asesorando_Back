package co.edu.uniandes.dse.asesorando.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;

import java.util.List;
import java.util.Date;

@Repository
public interface CalendarioRepository extends JpaRepository<CalendarioEntity, Long> {

List<CalendarioEntity> findByFechaInicio(Date fechaInicio);
List<CalendarioEntity> findByFechaFin(Date fechaFin);
List<CalendarioEntity> findByFechaInicioLessThan(Date fechaInicio);
List<CalendarioEntity> findByFechaInicioBetween(Date fechaInicio, Date fechaFin);
List<CalendarioEntity> findByProfesor(ProfesorEntity profesor);


}
