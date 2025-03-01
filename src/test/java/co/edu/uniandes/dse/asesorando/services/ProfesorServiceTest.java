/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package co.edu.uniandes.dse.asesorando.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase que prueba el servicio de Profesor
 *
 * @author Daniel-VergaraM
 */
@DataJpaTest
@Transactional
@Import(ProfesorService.class)
public class ProfesorServiceTest {

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private static final String BASE_PROFESOR = "PROFESOR";
    private static final String BASE_PROFESOR_VIRTUAL = "PROFESORVIRTUAL";
    private static final String BASE_PROFESOR_PRESENCIAL = "PROFESORPRESENCIAL";

    private List<ProfesorEntity> data = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ProfesorEntity").executeUpdate();
        data.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
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
    public void testCreateProfesorObject() throws EntityNotFoundException {
        ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
        if (entity.getId() == null) {
            entity.setId(factory.manufacturePojo(Long.class));
        }
        ProfesorEntity result = profesorService.createProfesor(entity, BASE_PROFESOR);
        assertNotNull(result);

        ProfesorEntity entityInDB = entityManager.find(ProfesorEntity.class, result.getId());

        assertNotNull(entityInDB);

        assertThrows(EntityNotFoundException.class, () -> {
            profesorService.createProfesor(result, BASE_PROFESOR);
        });
    }

    @Test
    public void testCreateProfesorParams() throws EntityNotFoundException {
        ProfesorEntity entity = profesorService.createProfesor("nombre", "correo", "contrasena", BASE_PROFESOR);

        ProfesorEntity entityInDB = entityManager.find(ProfesorEntity.class, entity.getId());
        assertNotNull(entityInDB);

        assertThrows(EntityNotFoundException.class, () -> {
            profesorService.createProfesor("nombre", "correo", "contrasena", BASE_PROFESOR);
        });

    }

    @Test
    public void testGetProfesores() throws EntityNotFoundException {
        List<ProfesorEntity> list = (List<ProfesorEntity>) profesorService.getProfesores();
        assertNotNull(list);

        for (ProfesorEntity entity : data) {
            boolean found = false;
            for (ProfesorEntity storedEntity : list) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertFalse(found);
        }
    }

    @Test
    public void testGetProfesor() throws EntityNotFoundException {
        ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(entity);
        ProfesorEntity result = profesorService.getProfesor(entity.getId());
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            profesorService.getProfesor(factory.manufacturePojo(Long.class));
        });
    }

    @Test
    public void testUpdateProfesor() throws EntityNotFoundException {
        ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(entity);
        ProfesorEntity result = profesorService.updateProfesor(entity.getId(), entity);
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            profesorService.updateProfesor(factory.manufacturePojo(Long.class), entity);
        });
    }

    @Test
    public void testDeleteProfesor() throws EntityNotFoundException {
        ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(entity);
        profesorService.deleteProfesor(entity.getId());
        ProfesorEntity entityInDB = entityManager.find(ProfesorEntity.class, entity.getId());
        assertNull(entityInDB);
    }

    @ParameterizedTest
    @ValueSource(strings = {BASE_PROFESOR, BASE_PROFESOR_VIRTUAL, BASE_PROFESOR_PRESENCIAL, "RANDOM"})
    public void testGetProfesoresPorTipo(String tipo) throws EntityNotFoundException {
        ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(entity);

        if (List.of(BASE_PROFESOR, BASE_PROFESOR_VIRTUAL, BASE_PROFESOR_PRESENCIAL).contains(tipo)) {
            entity.setTipo(tipo);
            List<ProfesorEntity> result = profesorService.getProfesoresPorTipo(tipo);
            assertNotNull(result);
        } else {
            assertThrows(EntityNotFoundException.class, () -> {
                profesorService.getProfesoresPorTipo(tipo);
            });
        }

    }

}
