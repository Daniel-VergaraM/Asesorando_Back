package co.edu.uniandes.dse.asesorando.services;

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
    void testCrearComentario() throws EntityNotFoundException {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
        ComentarioEntity resultado = reservaComentarioService.crearComentario(reserva.getId(), nuevoComentario);

        assertNotNull(resultado);
        assertEquals(nuevoComentario.getComentario(), resultado.getComentario());
        assertEquals(nuevoComentario.getCalificacion(), resultado.getCalificacion());
        assertEquals(reserva, resultado.getReserva());
    }

    @Test
    void testCrearComentarioReservaNoExiste() throws EntityNotFoundException {
        ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
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
    void testActualizarComentarioReservaNoExiste() {
        ComentarioEntity comentarioActualizado = factory.manufacturePojo(ComentarioEntity.class);
        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.actualizarComentario(999L, comentarioActualizado);
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

        ComentarioEntity resultado = reservaComentarioService.crearComentarioEnReserva(reserva.getId(), comentario.getId());

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
    // Crear y persistir una reserva
    ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
    entityManager.persist(nuevaReserva);

    // Crear y persistir un comentario
    ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
    entityManager.persist(nuevoComentario);

    // Asociar el comentario a la reserva
    ComentarioEntity resultado = reservaComentarioService.asociarComentarioAReserva(nuevaReserva.getId(), nuevoComentario.getId());

    // Verificar que la asociación fue exitosa
    assertNotNull(resultado);
    assertEquals(nuevoComentario.getComentario(), resultado.getComentario());
    assertEquals(nuevoComentario.getCalificacion(), resultado.getCalificacion());
    assertEquals(nuevaReserva.getId(), resultado.getReserva().getId());
}

@Test
void testAsociarComentarioAReservaYaTieneComentario() throws EntityNotFoundException {
    // Crear y persistir una reserva con un comentario ya asociado
    ReservaEntity nuevaReserva = factory.manufacturePojo(ReservaEntity.class);
    ComentarioEntity comentarioExistente = factory.manufacturePojo(ComentarioEntity.class);
    nuevaReserva.setComentario(comentarioExistente);
    comentarioExistente.setReserva(nuevaReserva);
    entityManager.persist(nuevaReserva);
    entityManager.persist(comentarioExistente);

    // Crear y persistir un nuevo comentario
    ComentarioEntity nuevoComentario = factory.manufacturePojo(ComentarioEntity.class);
    entityManager.persist(nuevoComentario);

    // Asociar el nuevo comentario a la reserva (debe actualizar el existente)
    ComentarioEntity resultado = reservaComentarioService.asociarComentarioAReserva(nuevaReserva.getId(), nuevoComentario.getId());

    // Verificar que el comentario existente fue actualizado
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
        entityManager.persist(nuevaReserva);

        assertThrows(EntityNotFoundException.class, () -> {
            reservaComentarioService.asociarComentarioAReserva(nuevaReserva.getId(), 999L);
        });
    }

}
