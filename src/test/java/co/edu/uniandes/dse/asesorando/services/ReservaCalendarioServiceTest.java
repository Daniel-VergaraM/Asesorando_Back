package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach
    void setUp() {
        calendario = factory.manufacturePojo(CalendarioEntity.class);
        entityManager.persist(calendario);
        entityManager.flush();

        reserva = factory.manufacturePojo(ReservaEntity.class);
        reserva.setFechaReserva(LocalDate.now().plusDays(3));
        reserva.setCalendario(calendario);
        entityManager.persist(reserva);
        entityManager.flush();
    }

    @Test
    void testCrearReservaEnCalendario() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setFechaReserva(LocalDate.now().plusDays(5));
        
        ReservaEntity result = reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), nuevaReserva);

        assertNotNull(result);
        assertEquals(nuevaReserva.getFechaReserva(), result.getFechaReserva());
        assertEquals(calendario.getId(), result.getCalendario().getId());
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
    void testActualizarReservaEnCalendario() throws EntityNotFoundException {
        ReservaEntity reservaActualizada = factory.manufacturePojo(ReservaEntity.class);
        reservaActualizada.setFechaReserva(LocalDate.now().plusDays(6));

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
}