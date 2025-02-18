package co.edu.uniandes.dse.asesorando.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;

@DataJpaTest
@Transactional
@Import({ ComentarioService.class, EstudianteService.class, ReservaService.class })
public class ComentarioServiceTest {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EstudianteEntity> estudianteList = new ArrayList<>();
    private List<ReservaEntity> reservaList = new ArrayList<>();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentarioEntity);
            comentarioList.add(comentarioEntity);
        }
    }

    @Test
    void testCrearComentario() {
        ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
        ComentarioEntity result = comentarioService.crearComentario(newEntity);
        assertNotNull(result);
        ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
        assertEquals(newEntity.getComentario(), entity.getComentario());
        assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
    }

    @Test
    void testCrearComentarioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setComentario(null);
            comentarioService.crearComentario(newEntity);
        });
    }

    @Test
    void testLeerComentario() {
        ComentarioEntity entity = comentarioList.get(0);
        ComentarioEntity resultEntity = comentarioService.leer_comentario(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getComentario(), resultEntity.getComentario());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
    }

    @Test
    void testLeerComentarioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            comentarioService.leer_comentario(0L);
        });
    }

    @Test
    void testGetComentarios() {
        List<ComentarioEntity> list = comentarioService.getComentarios();
        assertEquals(comentarioList.size(), list.size());
        for (ComentarioEntity entity : list) {
            boolean found = false;
            for (ComentarioEntity storedEntity : comentarioList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testActualizarComentario() {
        ComentarioEntity entity = comentarioList.get(0);
        ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

        pojoEntity.setId(entity.getId());

        comentarioService.actualizar_comentario(entity.getId(), pojoEntity);

        ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());

        assertEquals(pojoEntity.getComentario(), resp.getComentario());
        assertEquals(pojoEntity.getCalificacion(), resp.getCalificacion());
    }

    @Test
    void testActualizarComentarioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setComentario(null);
            comentarioService.actualizar_comentario(0L, pojoEntity);
        });
    }

    @Test
    void testEliminarComentario() {
        ComentarioEntity entity = comentarioList.get(0);
        comentarioService.eliminar_comentario(entity.getId());
        ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testEliminarComentarioInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            comentarioService.eliminar_comentario(0L);
        });
    }
}
