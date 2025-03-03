package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReservaService.class)
class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaRepository reservaRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private EstudianteEntity estudiante;
    private AsesoriaEntity asesoria;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager().createQuery("DELETE FROM ReservaEntity").executeUpdate();

        estudiante = factory.manufacturePojo(EstudianteEntity.class);
        asesoria = factory.manufacturePojo(AsesoriaEntity.class);
        
        entityManager.persist(estudiante);
        entityManager.persist(asesoria);
    }

    @Test
    void testCrearReserva() {
        try {
            LocalDate fecha = LocalDate.now();
            ReservaEntity reserva = reservaService.crearReserva(fecha, estudiante, asesoria);
            
            assertNotNull(reserva);
            assertEquals(fecha, reserva.getFechaReserva());
            assertEquals(estudiante, reserva.getEstudiante());
            assertEquals(asesoria, reserva.getAsesoria());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testCrearReservaConDatosNulos() {
        assertThrows(EntityNotFoundException.class, () -> reservaService.crearReserva(null, estudiante, asesoria));
        assertThrows(EntityNotFoundException.class, () -> reservaService.crearReserva(LocalDate.now(), null, asesoria));
        assertThrows(EntityNotFoundException.class, () -> reservaService.crearReserva(LocalDate.now(), estudiante, null));
    }

    @Test
    void testListarReservas() {
        try {
            LocalDate fecha = LocalDate.now();
            reservaService.crearReserva(fecha, estudiante, asesoria);
            
            List<ReservaEntity> reservas = reservaService.listarReservas();
            assertFalse(reservas.isEmpty());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testActualizarReserva() {
        try {
            LocalDate fecha = LocalDate.now();
            ReservaEntity reserva = reservaService.crearReserva(fecha, estudiante, asesoria);
            
            LocalDate nuevaFecha = fecha.plusDays(3);
            EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
            AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
            
            entityManager.persist(nuevoEstudiante);
            entityManager.persist(nuevaAsesoria);
            
            ReservaEntity actualizada = reservaService.updateReserva(reserva.getId(), nuevaFecha, nuevoEstudiante, nuevaAsesoria);
            
            assertNotNull(actualizada);
            assertEquals(nuevaFecha, actualizada.getFechaReserva());
            assertEquals(nuevoEstudiante, actualizada.getEstudiante());
            assertEquals(nuevaAsesoria, actualizada.getAsesoria());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testEliminarReserva() {
        try {
            LocalDate fecha = LocalDate.now();
            ReservaEntity reserva = reservaService.crearReserva(fecha, estudiante, asesoria);
            
            reservaService.eliminarReserva(reserva.getId());
            assertFalse(reservaRepository.existsById(reserva.getId()));
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }
}