package co.edu.uniandes.dse.asesorando.services;


import java.util.List;
import java.util.Optional;
import java.util.Date;

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
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
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
        reserva.setFechaReserva((new Date(System.currentTimeMillis() + (3*1000*60*60*20))));
        reserva.setCalendario(calendario);
        entityManager.persist(reserva);
        entityManager.flush();
    }

    @Test
    void testCrearReservaEnCalendario() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setFechaReserva((new Date(System.currentTimeMillis() + (5*1000*60*60*20))));
        
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
        reservaActualizada.setFechaReserva((new Date(System.currentTimeMillis() + (6*1000*60*60*20))));

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