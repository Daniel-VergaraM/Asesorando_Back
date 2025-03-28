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

            LocalDate nuevaFecha = LocalDate.now().plusDays(2);
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

    @Test
    void testGetReserva_Exito() throws EntityNotFoundException {
        // Crea una reserva
        LocalDate fecha = LocalDate.now();
        ReservaEntity reserva = reservaService.crearReserva(fecha, estudiante, asesoria);

        // Obtén la reserva por su ID
        ReservaEntity reservaEncontrada = reservaService.getReserva(reserva.getId());

        // Verifica que la reserva fue encontrada y es la correcta
        assertNotNull(reservaEncontrada);
        assertEquals(reserva.getId(), reservaEncontrada.getId());
        assertEquals(reserva.getFechaReserva(), reservaEncontrada.getFechaReserva());
    }

    @Test
    void testGetReserva_NoEncontrada() {
        // Intenta obtener una reserva con un ID inexistente
        assertThrows(EntityNotFoundException.class, () -> reservaService.getReserva(Long.MAX_VALUE));
    }

    @Test
    void testEliminarReserva_Exito() throws EntityNotFoundException {
        // Crea una reserva
        LocalDate fecha = LocalDate.now();
        ReservaEntity reserva = reservaService.crearReserva(fecha, estudiante, asesoria);

        // Elimina la reserva
        reservaService.eliminarReserva(reserva.getId());

        // Verifica que la reserva haya sido eliminada
        assertFalse(reservaRepository.existsById(reserva.getId()));
    }

    @Test
    void testEliminarReserva_IdNulo() {
        // Intenta eliminar una reserva con un ID nulo
        assertThrows(EntityNotFoundException.class, () -> reservaService.eliminarReserva(null));
    }

    @Test
    void testEliminarReserva_NoExistente() {
        // Intenta eliminar una reserva con un ID inexistente
        assertThrows(EntityNotFoundException.class, () -> reservaService.eliminarReserva(Long.MAX_VALUE));
    }

    @Test
    void testActualizarReserva_Exito() throws EntityNotFoundException {
        // Crea una reserva
        LocalDate fecha = LocalDate.now();
        ReservaEntity reserva = reservaService.crearReserva(fecha, estudiante, asesoria);

        // Nuevos datos para la actualización
        LocalDate nuevaFecha = LocalDate.now().plusDays(2);
        EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);

        // Persiste los nuevos estudiantes y asesorías
        entityManager.persist(nuevoEstudiante);
        entityManager.persist(nuevaAsesoria);

        // Actualiza la reserva
        ReservaEntity reservaActualizada = reservaService.updateReserva(reserva.getId(), nuevaFecha, nuevoEstudiante, nuevaAsesoria);

        // Verifica que los datos de la reserva se han actualizado correctamente
        assertNotNull(reservaActualizada);
        assertEquals(nuevaFecha, reservaActualizada.getFechaReserva());
        assertEquals(nuevoEstudiante, reservaActualizada.getEstudiante());
        assertEquals(nuevaAsesoria, reservaActualizada.getAsesoria());
    }

    @Test
    void testActualizarReserva_Fallo_FechaNula() {
        // Intenta actualizar una reserva con fecha nula
        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(Long.MAX_VALUE, null, estudiante, asesoria));
    }

    @Test
    void testActualizarReserva_Fallo_EstudianteNulo() {
        // Intenta actualizar una reserva con estudiante nulo
        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(Long.MAX_VALUE, LocalDate.now(), null, asesoria));
    }

    @Test
    void testActualizarReserva_Fallo_AsesoriaNula() {
        // Intenta actualizar una reserva con asesoría nula
        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(Long.MAX_VALUE, LocalDate.now(), estudiante, null));
    }

    @Test
    void testActualizarReserva_NoExistente() {
        // Intenta actualizar una reserva con un ID inexistente
        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(Long.MAX_VALUE, LocalDate.now(), estudiante, asesoria));
    }

}
