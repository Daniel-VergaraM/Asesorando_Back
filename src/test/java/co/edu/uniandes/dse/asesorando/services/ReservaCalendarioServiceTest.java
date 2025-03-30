package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
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
    private ReservaRepository reservaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CalendarioEntity calendario;
    private ReservaEntity reserva;
    private ReservaEntity reserva2;

    @BeforeEach
    void setUp() {
        calendario = factory.manufacturePojo(CalendarioEntity.class);
        calendario.setReservas(new ArrayList<>());
        entityManager.persist(calendario);
        entityManager.flush();

        reserva = factory.manufacturePojo(ReservaEntity.class);
        reserva.setFechaReserva(LocalDate.now().plusDays(2));
        reserva.setCalendario(calendario);
        entityManager.persist(reserva);
        entityManager.flush();

        reserva2 = factory.manufacturePojo(ReservaEntity.class);
        reserva2.setFechaReserva(LocalDate.now().plusDays(3));
        entityManager.persist(reserva2);
        entityManager.flush();
    }

    @Test
    void testEliminarReservaDeCalendario() throws EntityNotFoundException {
        reservaCalendarioService.eliminarReservaDeCalendario(calendario.getId(), reserva.getId());

        // Forzar la sincronización
        entityManager.flush();
        entityManager.clear();

        Optional<ReservaEntity> deleted = reservaRepository.findById(reserva.getId());
        assertTrue(deleted.isEmpty(), "La reserva aún existe en la base de datos.");
    }

    @Test
    void testCrearReservaEnCalendario_CalendarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.crearReservaEnCalendario(999L, reserva.getId());
        });
    }

    @Test
    void testCrearReservaEnCalendario_ReservaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), 999L);
        });
    }

    @Test
    void testCrearReservaEnCalendario_ReservaYaAsociada() {
        reserva.setCalendario(calendario); // Asocia la reserva al calendario
        entityManager.persist(reserva);

        assertThrows(IllegalOperationException.class, () -> {
            reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva.getId());
        });
    }

    @Test
    void testObtenerReservasPorCalendario_CalendarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaCalendarioService.obtenerReservasPorCalendario(999L);
        });
    }

    @Test
    void testObtenerReservasPorCalendario_Exito() throws EntityNotFoundException, IllegalOperationException {
        // Llamada al servicio para obtener reservas por calendario
        CalendarioEntity c = factory.manufacturePojo(CalendarioEntity.class);
        c.setReservas(new ArrayList<>());
        entityManager.persist(c);
        reservaCalendarioService.crearReservaEnCalendario(c.getId(), reserva2.getId());

        List<ReservaEntity> reservas = reservaCalendarioService.obtenerReservasPorCalendario(c.getId());
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        assertEquals(1, reservas.size());
    }

    @Test
    void testCrearReservaEnCalendario_CalendarioYaTieneReserva() {
        // Asocia previamente la reserva al calendario
        reserva.setCalendario(calendario);
        entityManager.persist(reserva);
        entityManager.flush();

        // Verifica que se lanza IllegalOperationException
        assertThrows(IllegalOperationException.class, () -> {
            reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva.getId());
        });
    }

}
