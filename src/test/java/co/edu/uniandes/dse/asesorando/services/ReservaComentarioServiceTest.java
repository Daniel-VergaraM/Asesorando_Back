package co.edu.uniandes.dse.asesorando.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReservaComentarioService.class)
public class ReservaComentarioServiceTest {

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
        comentario = factory.manufacturePojo(ComentarioEntity.class);

        entityManager.persist(reserva);
    }

    @Test
    void testCrearComentario() {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        ComentarioEntity resultado = reservaComentarioService.crearComentario(reserva.getId(), nuevoComentario);

        assertNotNull(resultado);
        assertEquals(nuevoComentario.getComentario(), resultado.getComentario());
        assertEquals(nuevoComentario.getCalificacion(), resultado.getCalificacion());
        assertEquals(reserva, resultado.getReserva());
    }

    @Test
    void testCrearComentarioReservaNoExiste() {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        assertThrows(IllegalArgumentException.class, () -> {
            reservaComentarioService.crearComentario(999L, nuevoComentario);
        });
    }

    @Test
    void testObtenerComentarioPorReserva() {
        comentario.setReserva(reserva);
        entityManager.persist(comentario);

        ComentarioEntity obtenido = reservaComentarioService.obtenerComentarioPorReserva(reserva.getId());
        assertNotNull(obtenido);
        assertEquals(comentario.getComentario(), obtenido.getComentario());
    }

    @Test
    void testObtenerComentarioReservaNoExiste() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservaComentarioService.obtenerComentarioPorReserva(999L);
        });
    }

    @Test
    void testActualizarComentario() {
        comentario.setReserva(reserva);
        entityManager.persist(comentario);

        ComentarioEntity comentarioActualizado = factory.manufacturePojo(ComentarioEntity.class);
        ComentarioEntity resultado = reservaComentarioService.actualizarComentario(reserva.getId(), comentarioActualizado);

        assertNotNull(resultado);
        assertEquals(comentarioActualizado.getComentario(), resultado.getComentario());
        assertEquals(comentarioActualizado.getCalificacion(), resultado.getCalificacion());
    }

    @Test
    void testActualizarComentarioReservaNoExiste() {
        ComentarioEntity comentarioActualizado = factory.manufacturePojo(ComentarioEntity.class);
        assertThrows(IllegalArgumentException.class, () -> {
            reservaComentarioService.actualizarComentario(999L, comentarioActualizado);
        });
    }

    @Test
    void testEliminarComentario() {
        comentario.setReserva(reserva);
        entityManager.persist(comentario);

        reservaComentarioService.eliminarComentario(reserva.getId());

        assertFalse(comentarioRepository.existsById(comentario.getId()));
    }

    @Test
    void testEliminarComentarioNoExiste() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservaComentarioService.eliminarComentario(999L);
        });
    }

    @Test
    void testEliminarReservaYComentario() {
        comentario.setReserva(reserva);
        entityManager.persist(comentario);

        reservaComentarioService.eliminarReservaYComentario(reserva.getId());

        assertFalse(reservaRepository.existsById(reserva.getId()));
        assertFalse(comentarioRepository.existsById(comentario.getId()));
    }

    @Test
    void testEliminarReservaYComentarioReservaNoExiste() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservaComentarioService.eliminarReservaYComentario(999L);
        });
    }
}
