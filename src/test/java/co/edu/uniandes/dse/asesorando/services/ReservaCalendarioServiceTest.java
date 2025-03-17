package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDate;
import java.util.List;

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
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
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
    void testObtenerReservasPorCalendario_Exito() throws EntityNotFoundException {
        // Llamada al servicio para obtener reservas por calendario
        List<ReservaEntity> reservas = reservaCalendarioService.obtenerReservasPorCalendario(calendario.getId());
        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
        assertEquals(0, reservas.size());
    }


    @Test
    void testCrearReservaEnCalendario_Exito() throws EntityNotFoundException, IllegalOperationException {
        // Llamada al servicio para crear una reserva en el calendario
        ReservaEntity resultado = reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva.getId());

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
        reservaCalendarioService.crearReservaEnCalendario(calendario.getId(), reserva.getId());

        // Volver a obtener el calendario desde la base de datos para verificar la lista actualizada
        CalendarioEntity calendarioPersistido = entityManager.find(CalendarioEntity.class, calendario.getId());

        // Verificación de que el calendario contiene la reserva en su lista
        assertTrue(calendarioPersistido.getReservas().contains(reserva), "El calendario no contiene la reserva asociada.");
    }

        

}