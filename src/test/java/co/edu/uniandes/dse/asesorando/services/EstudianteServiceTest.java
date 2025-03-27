package co.edu.uniandes.dse.asesorando.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase que prueba el servicio de Estudiante
 *
 * @author NicoParraZ
 */
@DataJpaTest
@Transactional
@Import(EstudianteService.class)

public class EstudianteServiceTest {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private List<EstudianteEntity> data = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EstudianteEntity").executeUpdate();
        data.clear();
    }

    private void insertData() {
        for(int i = 0; i < 3; i++) {
            EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @BeforeEach
    public void setUp() {
        clearData();
        insertData();
    }

    @Test
    public void testCreateEstudianteByAtributes() throws EntityNotFoundException {
        EstudianteEntity entity = estudianteService.createEstudianteByAtributes("nombre", "correo", "contrasena");

        EstudianteEntity entityInDB = entityManager.find(EstudianteEntity.class, entity.getId());
        assertNotNull(entityInDB);

        assertThrows(EntityNotFoundException.class, () -> {estudianteService.createEstudianteByAtributes("nombre", "correo", "contrasena");});
    }

    @Test
    public void testCreateEstudianteByObject() throws EntityNotFoundException {
        EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
        if (entity.getId() == null) {
            entity.setId(factory.manufacturePojo(Long.class));
        }
        EstudianteEntity result = estudianteService.createEstudianteByObject(entity);
        assertNotNull(result);

        EstudianteEntity entityInDB = entityManager.find(EstudianteEntity.class, result.getId());
        assertNotNull(entityInDB);

        assertThrows(EntityNotFoundException.class, () -> {estudianteService.createEstudianteByObject(result);});
    }

    @Test
    public void testGetProfesor() throws EntityNotFoundException {
        EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
        entityManager.persist(entity);
        EstudianteEntity result = estudianteService.getEstudiante(entity.getId());
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {estudianteService.getEstudiante(factory.manufacturePojo(Long.class));});
    }

    @Test
    public void testGetEstudiantes() {
        List<EstudianteEntity> list = (List<EstudianteEntity>) estudianteService.getEstudiantes();

        List<EstudianteEntity> listInDB = entityManager.getEntityManager().createQuery("select s from EstudianteEntity s", EstudianteEntity.class).getResultList();

        assertNotNull(list);
        assertNotNull(listInDB);
        assertNotEquals(list, listInDB);
    }

    @Test
    public void testUpdateEstudianteById() throws EntityNotFoundException {
        EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
        entityManager.persist(entity);
        EstudianteEntity result = estudianteService.updateEstudianteById(entity.getId(), entity);
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {estudianteService.updateEstudianteById(factory.manufacturePojo(Long.class), entity);});
    }

    @Test
    public void testDeleteEstudiante() throws EntityNotFoundException{
        EstudianteEntity entity = factory.manufacturePojo(EstudianteEntity.class);
        entityManager.persist(entity);
        estudianteService.deleteEstudiante(entity.getId());
        EstudianteEntity entityInDB = entityManager.find(EstudianteEntity.class, entity.getId());
        assertNull(entityInDB);
    }
}
