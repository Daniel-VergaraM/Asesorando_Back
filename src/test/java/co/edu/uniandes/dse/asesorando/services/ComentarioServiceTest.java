package co.edu.uniandes.dse.asesorando.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ ComentarioService.class, EstudianteService.class, ReservaService.class })
public class ComentarioServiceTest {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private final List<ComentarioEntity> comentarioList = new ArrayList<>();

    
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
        try {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            if (newEntity.getCalificacion() < 0 || newEntity.getCalificacion() > 5) {
                newEntity.setCalificacion(3);
            }
            ComentarioEntity result = comentarioService.crearComentario(newEntity);
            assertNotNull(result);
            ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
            assertEquals(newEntity.getComentario(), entity.getComentario());
            assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testCrearComentarioInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setComentario(null);
            comentarioService.crearComentario(newEntity);
        });
    }

    @Test
    void testLeerComentario() {
        try {
            ComentarioEntity entity = comentarioList.get(0);
            ComentarioEntity resultEntity = comentarioService.leerComentario(entity.getId());
            assertNotNull(resultEntity);
            assertEquals(entity.getComentario(), resultEntity.getComentario());
            assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testLeerComentarioInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.leerComentario(0L);
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
        try {
            ComentarioEntity entity = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            
            pojoEntity.setId(entity.getId());
            
            if (pojoEntity.getCalificacion() < 0 || pojoEntity.getCalificacion() > 5) {
                pojoEntity.setCalificacion(3);
            }
            
            comentarioService.actualizarComentario(entity.getId(), pojoEntity);
            
            ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
            
            assertEquals(pojoEntity.getComentario(), resp.getComentario());
            assertEquals(pojoEntity.getCalificacion(), resp.getCalificacion());
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testActualizarComentarioInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setComentario(null);
            comentarioService.actualizarComentario(0L, pojoEntity);
        });
    }

    @Test
    void testEliminarComentario() {
        try {
            ComentarioEntity entity = comentarioList.get(0);
            comentarioService.eliminarComentario(entity.getId());
            ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
            assertNull(deleted);
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    void testEliminarComentarioInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.eliminarComentario(0L);
        });
    }

    @Test
    void testCrearComentarioCalificacionInvalida() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(-1);
            comentarioService.crearComentario(newEntity);
        });
    }

    @Test
    void testActualizarComentarioCalificacionInvalida() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(6);
            comentarioService.actualizarComentario(0L, pojoEntity);
        });
    }
}
