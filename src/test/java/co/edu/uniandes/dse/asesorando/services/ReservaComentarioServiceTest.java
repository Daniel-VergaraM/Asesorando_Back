package co.edu.uniandes.dse.asesorando.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReservaComentarioService.class)
class ReservaComentarioServiceTest {

    @Autowired
    private ReservaComentarioService reservaComentarioService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private ReservaEntity reserva;
    private ComentarioEntity comentario;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager().createQuery("DELETE FROM ComentarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM ReservaEntity").executeUpdate();

        reserva = factory.manufacturePojo(ReservaEntity.class);
        reserva.setId(null);
        comentario = factory.manufacturePojo(ComentarioEntity.class);
        comentario.setId(null);

        entityManager.persist(reserva);
    }

    @Test
    void testCrearComentario() throws EntityNotFoundException {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        nuevoComentario.setId(null);
        ComentarioEntity resultado = reservaComentarioService.crearComentario(reserva.getId(), nuevoComentario);

        assertNotNull(resultado);
        assertEquals(nuevoComentario.getComentario(), resultado.getComentario());
        assertEquals(nuevoComentario.getCalificacion(), resultado.getCalificacion());
        assertEquals(reserva, resultado.getReserva());
    }

    @Test
    void testCrearComentarioReservaNoExiste() {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        nuevoComentario.setId(null);
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.crearComentario(999L, nuevoComentario);
        });
    }

    @Test
    void testObtenerComentarioReservaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.obtenerComentarioPorReserva(999L);
        });
    }

    @Test
    void testEliminarComentarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.eliminarComentario(999L);
        });
    }

    @Test
    void testEliminarReservaYComentario() throws EntityNotFoundException {
        comentario.setReserva(reserva);
        reserva.setComentario(comentario);
        entityManager.persist(reserva);
        entityManager.persist(comentario);

        reservaComentarioService.eliminarReservaYComentario(reserva.getId());

        assertFalse(reservaRepository.existsById(reserva.getId()));
        assertFalse(comentarioRepository.existsById(comentario.getId()));
    }

    @Test
    void testEliminarReservaYComentarioReservaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.eliminarReservaYComentario(999L);
        });
    }

    @Test
    void testCrearComentarioEnReserva() throws EntityNotFoundException, IllegalOperationException {
        entityManager.persist(comentario);

        ComentarioEntity resultado = reservaComentarioService.crearComentarioEnReserva(reserva.getId(),
                comentario.getId());

        assertNotNull(resultado);
        assertEquals(comentario.getComentario(), resultado.getComentario());
        assertEquals(comentario.getCalificacion(), resultado.getCalificacion());
        assertEquals(reserva.getId(), resultado.getReserva().getId());
    }

    @Test
    void testCrearComentarioEnReservaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.crearComentarioEnReserva(999L, comentario.getId());
        });
    }

    @Test
    void testAsociarComentarioAReserva() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        entityManager.persist(nuevaReserva);

        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        nuevoComentario.setId(null);
        entityManager.persist(nuevoComentario);

        ComentarioEntity resultado = reservaComentarioService.asociarComentarioAReserva(nuevaReserva.getId(),
                nuevoComentario.getId());

        assertNotNull(resultado);
        assertEquals(nuevoComentario.getComentario(), resultado.getComentario());
        assertEquals(nuevoComentario.getCalificacion(), resultado.getCalificacion());
        assertEquals(nuevaReserva.getId(), resultado.getReserva().getId());
    }

    @Test
    void testAsociarComentarioAReservaYaTieneComentario() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        ComentarioEntity comentarioExistente = factory.manufacturePojo(ComentarioEntity.class);
        comentarioExistente.setId(null);
        nuevaReserva.setComentario(comentarioExistente);
        comentarioExistente.setReserva(nuevaReserva);
        entityManager.persist(nuevaReserva);
        entityManager.persist(comentarioExistente);

        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        nuevoComentario.setId(null);
        entityManager.persist(nuevoComentario);

        ComentarioEntity resultado = reservaComentarioService.asociarComentarioAReserva(nuevaReserva.getId(),
                nuevoComentario.getId());

        assertNotNull(resultado);
        assertEquals(nuevoComentario.getComentario(), resultado.getComentario());
        assertEquals(nuevoComentario.getCalificacion(), resultado.getCalificacion());
        assertEquals(nuevaReserva.getId(), resultado.getReserva().getId());
    }

    @Test
    void testAsociarComentarioAReservaNoExiste() {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        entityManager.persist(nuevoComentario);

        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.asociarComentarioAReserva(999L, nuevoComentario.getId());
        });
    }

    @Test
    void testAsociarComentarioAReservaComentarioNoExiste() {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        entityManager.persist(nuevaReserva);

        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.asociarComentarioAReserva(nuevaReserva.getId(), 999L);
        });
    }

    @Test
    void testActualizarComentarioReservaNoExiste() {
        ComentarioEntity comentarioActualizado = factory.manufacturePojo(ComentarioEntity.class);
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.actualizarComentario(999L, comentarioActualizado);
        });
    }

    @Test
    void testActualizarComentarioComentarioNoExiste() {
        // Crear una reserva sin comentario
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        entityManager.persist(nuevaReserva);

        // Intentar actualizar el comentario cuando no existe
        ComentarioEntity comentarioActualizado = factory.manufacturePojo(ComentarioEntity.class);
        comentarioActualizado.setId(null);
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.actualizarComentario(nuevaReserva.getId(), comentarioActualizado);
        });
    }

    @Test
    void testActualizarComentarioExitoso() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        ComentarioEntity comentarioExistente = factory.manufacturePojo(ComentarioEntity.class);
        comentarioExistente.setId(null);
        nuevaReserva.setComentario(comentarioExistente);
        comentarioExistente.setReserva(nuevaReserva);
        entityManager.persist(nuevaReserva);
        entityManager.persist(comentarioExistente);

        ComentarioEntity comentarioActualizado = factory.manufacturePojo(ComentarioEntity.class);
        comentarioActualizado.setId(null);
        comentarioActualizado.setComentario("Nuevo Comentario");
        comentarioActualizado.setCalificacion(5);

        ComentarioEntity resultado = reservaComentarioService.actualizarComentario(nuevaReserva.getId(),
                comentarioActualizado);

        assertNotNull(resultado);
        assertEquals("Nuevo Comentario", resultado.getComentario());
        assertEquals(5, resultado.getCalificacion());
        assertEquals(nuevaReserva.getId(), resultado.getReserva().getId());
    }

    @Test
    void testEliminarComentarioReservaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.eliminarComentario(999L);
        });
    }

    @Test
    void testEliminarComentarioComentarioNoExiste() { // Crear una reserva sin comentario
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        entityManager.persist(nuevaReserva);

        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.eliminarComentario(nuevaReserva.getId());
        });
    }

    @Test
    void testEliminarComentarioExitoso() throws EntityNotFoundException {
        ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
        nuevaReserva.setId(null);
        ComentarioEntity comentarioExistente = factory.manufacturePojo(ComentarioEntity.class);
        comentarioExistente.setId(null);
        nuevaReserva.setComentario(comentarioExistente);
        comentarioExistente.setReserva(nuevaReserva);
        entityManager.persist(nuevaReserva);
        entityManager.persist(comentarioExistente);

        reservaComentarioService.eliminarComentario(nuevaReserva.getId());

        assertFalse(comentarioRepository.existsById(comentarioExistente.getId()));
        assertNull(nuevaReserva.getComentario());
    }

}
