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
@Import({EstudianteReservaService.class, EstudianteService.class, ReservaService.class})
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
        estudiante = factory.manufacturePojo(EstudianteEntity.class);
        
        // Persistir comentarios si la entidad los tiene
        if (estudiante.getComentarios() != null) {
            estudiante.getComentarios().forEach(entityManager::persist);
        }

        entityManager.persist(estudiante);
        entityManager.flush();

        reserva = factory.manufacturePojo(ReservaEntity.class);
        reserva.setFechaReserva(LocalDateTime.now().plusDays(2));;
        reserva.setEstudiante(estudiante);
        entityManager.persist(reserva);
        entityManager.flush();
    }

    @Test
    void testCrearReserva() {
        try {
            ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
            nuevaReserva.setFechaReserva(LocalDateTime.now().plusDays(2));
            
            ReservaEntity result = estudianteReservaService.crearReserva(estudiante.getId(), nuevaReserva);
            
            assertNotNull(result);
            assertEquals(nuevaReserva.getFechaReserva(), result.getFechaReserva());
            assertEquals(estudiante.getId(), result.getEstudiante().getId());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
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
    void testActualizarReserva() {
        try {
            ReservaEntity reservaActualizada = factory.manufacturePojo(ReservaEntity.class);
            reservaActualizada.setFechaReserva(LocalDateTime.now().plusDays(2));
            
            ReservaEntity result = estudianteReservaService.actualizarReserva(reserva.getId(), reservaActualizada);
            
            assertNotNull(result);
            assertEquals(reservaActualizada.getFechaReserva(), result.getFechaReserva());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testEliminarReserva() {
        try {
            estudianteReservaService.eliminarReserva(reserva.getId());
            
            Optional<ReservaEntity> deleted = reservaRepository.findById(reserva.getId());
            assertTrue(deleted.isEmpty());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }
}
