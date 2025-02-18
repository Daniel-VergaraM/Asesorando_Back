package co.edu.uniandes.dse.asesorando.services;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.EstudianteRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
@DataJpaTest
@Transactional
@Import(ComentarioEstudianteService.class)
public class ComentarioEstudianteServiceTest {

    @Autowired
    private ComentarioEstudianteService comentarioEstudianteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private EstudianteEntity estudiante = new EstudianteEntity();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstudianteEntity").executeUpdate();
    }

    private void insertData() {
        estudiante = factory.manufacturePojo(EstudianteEntity.class);
        entityManager.persist(estudiante);

        for (int i = 0; i < 3; i++) {
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(entity);
            comentarioList.add(entity);
            estudiante.getComentarios().add(entity);
        }
    }

    @Test
    void testAddComentario() throws EntityNotFoundException {
        ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
        entityManager.persist(newComentario);

        ComentarioEntity comentarioEntity = comentarioEstudianteService.addcomentario(newComentario.getId(), estudiante.getId());
        assertNotNull(comentarioEntity);

        assertEquals(comentarioEntity.getId(), newComentario.getId());
    }

    @Test
    void testAddComentarioInvalidEstudiante() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(newComentario);
            comentarioEstudianteService.addcomentario(newComentario.getId(), 0L);
        });
    }

    @Test
    void testAddInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioEstudianteService.addcomentario(0L, estudiante.getId());
        });
    }

    @Test
    void testGetComentarios() throws EntityNotFoundException {
        List<ComentarioEntity> comentarioEntities = comentarioEstudianteService.getcomentarios(estudiante.getId());

        assertEquals(comentarioList.size(), comentarioEntities.size());

        for (ComentarioEntity comentario : comentarioList) {
            assertTrue(comentarioEntities.contains(comentario));
        }
    }

    @Test
    void testGetComentariosInvalidEstudiante() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioEstudianteService.getcomentarios(0L);
        });
    }

    @Test
    void testGetComentario() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity comentarioEntity = comentarioList.get(0);
        ComentarioEntity comentario = comentarioEstudianteService.getcomentario(estudiante.getId(), comentarioEntity.getId());
        assertNotNull(comentario);

        assertEquals(comentarioEntity.getId(), comentario.getId());
    }

    @Test
    void testGetComentarioInvalidEstudiante() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity comentarioEntity = comentarioList.get(0);
            comentarioEstudianteService.getcomentario(0L, comentarioEntity.getId());
        });
    }

    @Test
    void testGetInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioEstudianteService.getcomentario(estudiante.getId(), 0L);
        });
    }

    @Test
    void testGetComentarioNotAssociatedEstudiante() {
        assertThrows(IllegalOperationException.class, () -> {
            EstudianteEntity estudianteEntity = factory.manufacturePojo(EstudianteEntity.class);
            entityManager.persist(estudianteEntity);

            ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentarioEntity);

            comentarioEstudianteService.getcomentario(estudianteEntity.getId(), comentarioEntity.getId());
        });
    }

    @Test
    void testAddComentarios() throws EntityNotFoundException {
        List<ComentarioEntity> nuevaLista = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(entity);
            nuevaLista.add(entity);
        }

        comentarioEstudianteService.addcomentarios(estudiante.getId(), nuevaLista);

        List<ComentarioEntity> comentarioEntities = entityManager.find(EstudianteEntity.class, estudiante.getId()).getComentarios();
        for (ComentarioEntity item : nuevaLista) {
            assertTrue(comentarioEntities.contains(item));
        }
    }

    @Test
    void testAddComentariosInvalidEstudiante() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<ComentarioEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            comentarioEstudianteService.addcomentarios(0L, nuevaLista);
        });
    }

    @Test
    void testAddInvalidComentarios() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<ComentarioEntity> nuevaLista = new ArrayList<>();
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entity.setId(0L);
            nuevaLista.add(entity);
            comentarioEstudianteService.addcomentarios(estudiante.getId(), nuevaLista);
        });
    }

    @Test
    void testRemoveComentario() throws EntityNotFoundException {
        for (ComentarioEntity comentario : comentarioList) {
            comentarioEstudianteService.removecomentario(estudiante.getId(), comentario.getId());
        }
        assertTrue(comentarioEstudianteService.getcomentarios(estudiante.getId()).isEmpty());
    }

    @Test
    void testRemoveComentarioInvalidEstudiante() {
        assertThrows(EntityNotFoundException.class, () -> {
            for (ComentarioEntity comentario : comentarioList) {
                comentarioEstudianteService.removecomentario(0L, comentario.getId());
            }
        });
    }

    @Test
    void testRemoveInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioEstudianteService.removecomentario(estudiante.getId(), 0L);
        });
    }
}

