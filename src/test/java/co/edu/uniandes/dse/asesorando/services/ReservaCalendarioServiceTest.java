package co.edu.uniandes.dse.asesorando.services;

import java.util.ArrayList;
import java.util.Date;
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
        reserva.setFechaReserva((new Date(System.currentTimeMillis() + (3 * 1000 * 60 * 60 * 20))));
        reserva.setCalendario(calendario);
        entityManager.persist(reserva);
        entityManager.flush();

        reserva2 = factory.manufacturePojo(ReservaEntity.class);
        reserva2.setFechaReserva((new Date(System.currentTimeMillis() + (4 * 1000 * 60 * 60 * 20))));
        entityManager.persist(reserva2);
        entityManager.flush();
    }

    @Test
    void testCrearReservaEnCalendario() throws EntityNotFoundException, IllegalOperationException {
        CalendarioEntity c = factory.manufacturePojo(CalendarioEntity.class);
        c.setReservas(new ArrayList<>());
        entityManager.persist(c);
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        entityManager.persist(nuevaReserva);

        nuevaReserva.setFechaReserva((new Date(System.currentTimeMillis() + (5 * 1000 * 60 * 60 * 20))));

        ReservaEntity result = reservaCalendarioService.crearReservaEnCalendario(c.getId(), nuevaReserva.getId());

        assertNotNull(result);
        assertEquals(nuevaReserva.getFechaReserva(), result.getFechaReserva());
        assertEquals(c.getId(), result.getCalendario().getId());
    }

    @Test
    void testListarReservasDeCalendario() throws EntityNotFoundException {
        List<ReservaEntity> reservas = reservaCalendarioService.listarReservasDeCalendario(calendario.getId());

        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
    }

    @Test
    void testActualizarReservaEnCalendario() throws EntityNotFoundException, IllegalOperationException {
        ReservaEntity reservaActualizada = factory.manufacturePojo(ReservaEntity.class);
        reservaActualizada.setFechaReserva((new Date(System.currentTimeMillis() + (6 * 1000 * 60 * 60 * 20))));

        ReservaEntity result = reservaCalendarioService.actualizarReservaEnCalendario(calendario.getId(), reserva.getId(), reservaActualizada);

        assertNotNull(result);
        assertEquals(reservaActualizada.getFechaReserva(), result.getFechaReserva());
    }

    @Test
    void testEliminarReservaDeCalendario() throws EntityNotFoundException {
        reservaCalendarioService.eliminarReservaDeCalendario(calendario.getId(), reserva.getId());
        Optional<ReservaEntity> deleted = reservaRepository.findById(reserva.getId());
        assertTrue(deleted.isEmpty());
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

        List<ReservaEntity> reservas = reservaCalendarioService.obtenerReservasPorCalendario(c.getId());
        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
        assertEquals(0, reservas.size());
    }

    @Test
    void testCrearReservaEnCalendario_Exito() throws EntityNotFoundException, IllegalOperationException {
        // Llamada al servicio para crear una reserva en el calendario

        ReservaEntity resultado = reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva2.getId());

        // Verificación de que la reserva ha sido asociada al calendario
        assertNotNull(resultado);
        assertNotNull(resultado.getCalendario()); // Verificamos que la reserva ahora tiene un calendario asociado
        assertEquals(calendario.getId(), resultado.getCalendario().getId()); // Verificamos que el calendario es el correcto
    }

    @Test
    void testCrearReservaEnCalendario_CalendarioYaTieneReserva() throws EntityNotFoundException, IllegalOperationException {
        // Asocia previamente la reserva al calendario
        reserva.setCalendario(calendario);
        entityManager.persist(reserva);
        entityManager.flush();

        // Verifica que se lanza IllegalOperationException
        assertThrows(IllegalOperationException.class, () -> {
            reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva.getId());
        });
    }

    @Test
    void testCrearReservaEnCalendario_ListaDeReservasActualizada() throws EntityNotFoundException, IllegalOperationException {
        // Llamada al servicio para crear una reserva en el calendario
        reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva2.getId());

        // Volver a obtener el calendario desde la base de datos para verificar la lista actualizada
        CalendarioEntity calendarioPersistido = entityManager.find(CalendarioEntity.class, calendario.getId());

        // Verificación de que el calendario contiene la reserva en su lista
        assertTrue(calendarioPersistido.getReservas().contains(reserva2), "El calendario no contiene la reserva asociada.");
    }
}
