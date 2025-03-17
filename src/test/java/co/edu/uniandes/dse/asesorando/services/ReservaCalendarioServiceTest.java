package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReservaCalendarioService.class)
public class ReservaCalendarioServiceTest {

    @Autowired
    private ReservaCalendarioService reservaCalendarioService;


    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CalendarioEntity calendario;
    private ReservaEntity reserva;

    @BeforeEach
    void setUp() {
        calendario = factory.manufacturePojo(CalendarioEntity.class);
        entityManager.persist(calendario);
        entityManager.flush();

        reserva = factory.manufacturePojo(ReservaEntity.class);
        reserva.setFechaReserva(LocalDate.now().plusDays(3));
        entityManager.persist(reserva);
        entityManager.flush();
    }


    @Test
    void testAsociarReservaACalendarioReservaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.asociarReservaACalendario(999L, calendario.getId());
        });
    }

    @Test
    void testAsociarReservaACalendarioCalendarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.asociarReservaACalendario(reserva.getId(), 999L);
        });
    }


    @Test
    void testObtenerReservasPorCalendarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.obtenerReservasPorCalendario(999L);
        });
    }


    @Test
    void testEliminarReservaDeCalendarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.eliminarReservaDeCalendario(calendario.getId(), 999L);
        });
    }


}