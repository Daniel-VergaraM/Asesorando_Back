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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase que prueba el servicio de Tematica
 *
 * @author Daniel-VergaraM
 */
@DataJpaTest
@Transactional
@Import(TematicaService.class)
public class TematicaServiceTest {

    @Autowired
    private TematicaService tematicaService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private List<TematicaEntity> data = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TematicaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TematicaEntity entity = factory.manufacturePojo(TematicaEntity.class);
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
    public void createTematicaTest() {
        TematicaEntity newEntity = factory.manufacturePojo(TematicaEntity.class);
        TematicaEntity result = tematicaService.createTematica(newEntity);
        assertNotNull(result);
        TematicaEntity entity = entityManager.find(TematicaEntity.class, result.getId());
        assertNotNull(entity);

        assertThrows(IllegalArgumentException.class, () -> {
            tematicaService.createTematica(newEntity);
        });
    }

    @Test
    public void getTematicasTest() {
        List<TematicaEntity> list = tematicaService.getTematicas();
        assertNotNull(list);

        for (TematicaEntity entity : data) {
            boolean found = false;
            for (TematicaEntity storedEntity : list) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assert (found);
        }
    }

    @Test
    public void getTematicaTest() {
        TematicaEntity entity = data.get(0);
        TematicaEntity resultEntity = tematicaService.getTematica(entity.getId());
        assertNotNull(resultEntity);

        Long id = factory.manufacturePojo(Long.class);
        assertThrows(IllegalArgumentException.class, () -> {
            tematicaService.getTematica(id);
        });
    }

    @Test
    public void updateTematicaTest() {
        TematicaEntity entity = data.get(0);
        TematicaEntity pojoEntity = factory.manufacturePojo(TematicaEntity.class);

        pojoEntity.setId(entity.getId());

        tematicaService.updateTematica(pojoEntity.getId(), pojoEntity);

        TematicaEntity resp = entityManager.find(TematicaEntity.class, entity.getId());

        assertEquals(pojoEntity.getId(), resp.getId());
        Long id = factory.manufacturePojo(Long.class);

        assertThrows(IllegalArgumentException.class, () -> {
            tematicaService.updateTematica(id, pojoEntity);
        });
    }

    @Test
    public void deleteTematicaTest() {
        TematicaEntity entity = data.get(0);
        tematicaService.deleteTematica(entity.getId());
        TematicaEntity deleted = entityManager.find(TematicaEntity.class, entity.getId());
        assertNull(deleted);
        assertThrows(IllegalArgumentException.class, () -> {
            tematicaService.deleteTematica(entity.getId());
        });
    }
}
