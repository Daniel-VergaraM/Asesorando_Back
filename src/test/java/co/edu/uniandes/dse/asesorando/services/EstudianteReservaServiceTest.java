package co.edu.uniandes.dse.asesorando.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ EstudianteReservaService.class, EstudianteService.class, ReservaService.class })
class EstudianteReservaServiceTest {

    @Autowired
    private EstudianteReservaService estudianteReservaService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private EstudianteEntity estudiante;
    private ReservaEntity reserva;

    @BeforeEach
    void setUp() {
        clearData();

        estudiante = factory.manufacturePojo(EstudianteEntity.class);
        estudiante.setId(null);

        if (estudiante.getComentarios() != null) {
            estudiante.getComentarios().clear();
        }

        entityManager.persist(estudiante);
        entityManager.flush();

        reserva = factory.manufacturePojo(ReservaEntity.class);
        reserva.setId(null);
        reserva.setFechaReserva(LocalDateTime.now().plusDays(2));
        reserva.setEstudiante(estudiante);

        entityManager.persist(reserva);
        entityManager.flush();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReservaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstudianteEntity").executeUpdate();
    }

    @Test
    void testCrearReserva() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        nuevaReserva.setFechaReserva(LocalDateTime.now().plusDays(2));
        nuevaReserva.setEstudiante(null);

        ReservaEntity result = estudianteReservaService.crearReserva(estudiante.getId(), nuevaReserva);

        assertNotNull(result);
        assertEquals(nuevaReserva.getFechaReserva(), result.getFechaReserva());
        assertEquals(estudiante.getId(), result.getEstudiante().getId());
    }

    @Test
    void testCrearReservaConEstudianteInexistente() {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);

        assertThrows(EntityNotFoundException.class, () -> {
            estudianteReservaService.crearReserva(999L, nuevaReserva);
        });
    }

    @Test
    void testListarReservasPorEstudiante() {
        List<ReservaEntity> reservas = estudianteReservaService.listarReservasPorEstudiante(estudiante.getId());

        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
    }

    @Test
    void testListarReservasPorEstudianteSinReservas() {
        EstudianteEntity estudianteSinReservas = factory.manufacturePojo(EstudianteEntity.class);
        estudianteSinReservas.setId(null);
        entityManager.persist(estudianteSinReservas);
        entityManager.flush();

        List<ReservaEntity> reservas = estudianteReservaService
                .listarReservasPorEstudiante(estudianteSinReservas.getId());

        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
    }

    @Test
    void testActualizarReserva() throws EntityNotFoundException {
        ReservaEntity reservaActualizada = factory.manufacturePojo(ReservaEntity.class);
        reservaActualizada.setId(null);
        reservaActualizada.setFechaReserva(LocalDateTime.now().plusDays(5));

        ReservaEntity result = estudianteReservaService.actualizarReserva(reserva.getId(), reservaActualizada);

        assertNotNull(result);
        assertEquals(reservaActualizada.getFechaReserva(), result.getFechaReserva());
    }

    @Test
    void testActualizarReservaInexistente() {
        ReservaEntity reservaActualizada = factory.manufacturePojo(ReservaEntity.class);

        assertThrows(EntityNotFoundException.class, () -> {
            estudianteReservaService.actualizarReserva(999L, reservaActualizada);
        });
    }

    @Test
    void testEliminarReserva() throws EntityNotFoundException {
        estudianteReservaService.eliminarReserva(reserva.getId());

        Optional<ReservaEntity> deleted = reservaRepository.findById(reserva.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testEliminarReservaInexistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            estudianteReservaService.eliminarReserva(999L);
        });
    }
}
