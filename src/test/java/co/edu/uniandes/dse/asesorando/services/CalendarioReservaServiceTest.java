package co.edu.uniandes.dse.asesorando.services;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CalendarioReservaService.class)
public class CalendarioReservaServiceTest {

    @Autowired
    private CalendarioReservaService calendarioReservaService;

    @Autowired
    private CalendarioRepository calendarioRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private TestEntityManager entityManager;

    private CalendarioEntity calendario;

    @BeforeEach
    void setUp() {
        calendario = factory.manufacturePojo(CalendarioEntity.class);
        calendario.setFechaInicio(new Date(System.currentTimeMillis())); 
        calendario.setFechaFin(sumarDias(new Date(System.currentTimeMillis()), 5));
        entityManager.persist(calendario);
        entityManager.flush();
    }

    @Test
    void testCrearCalendario() {
        CalendarioEntity nuevoCalendario = factory.manufacturePojo(CalendarioEntity.class);
        nuevoCalendario.setFechaInicio(new Date(System.currentTimeMillis()));
        nuevoCalendario.setFechaFin(sumarDias(new Date(System.currentTimeMillis()), 10));

        CalendarioEntity result = calendarioReservaService.crearCalendario(nuevoCalendario);

        assertNotNull(result);
        assertEquals(nuevoCalendario.getFechaInicio(), result.getFechaInicio());
        assertEquals(nuevoCalendario.getFechaFin(), result.getFechaFin());
    }

    @Test
    void testListarCalendarios() {
        List<CalendarioEntity> calendarios = calendarioReservaService.listarCalendarios();

        assertNotNull(calendarios);
        assertFalse(calendarios.isEmpty());
        assertEquals(1, calendarios.size());
        assertEquals(calendario.getId(), calendarios.get(0).getId());
    }

    @Test
    void testActualizarCalendario() {
        try {
            CalendarioEntity nuevoCalendario = factory.manufacturePojo(CalendarioEntity.class);
            nuevoCalendario.setFechaInicio(new Date(System.currentTimeMillis()));
            nuevoCalendario.setFechaFin(sumarDias(new Date(System.currentTimeMillis()), 15));
            
            CalendarioEntity result = calendarioReservaService.actualizarCalendario(calendario.getId(), nuevoCalendario);
            
            assertNotNull(result);
            assertEquals(nuevoCalendario.getFechaInicio(), result.getFechaInicio());
            assertEquals(nuevoCalendario.getFechaFin(), result.getFechaFin());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testEliminarCalendarioYReservas() {
        try {
            calendarioReservaService.eliminarCalendarioYReservas(calendario.getId());
            
            Optional<CalendarioEntity> deleted = calendarioRepository.findById(calendario.getId());
            assertTrue(deleted.isEmpty());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    private Date sumarDias(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return new Date(calendar.getTimeInMillis());
    }
}
