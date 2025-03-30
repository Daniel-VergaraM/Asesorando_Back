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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorPresencialEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorVirtualEntity;
import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase que prueba el servicio de Usuario
 *
 * @author Daniel-VergaraM
 */
@DataJpaTest
@Transactional
@Import(UsuarioService.class)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private List<UsuarioEntity> data = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
        data.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
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
    public void testCreateUsuario() throws EntityNotFoundException {
        UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);

        UsuarioEntity result = usuarioService.createUsuario(entity);

        assertNotNull(result);

        UsuarioEntity entityInDB = entityManager.find(UsuarioEntity.class, result.getId());

        assertNotNull(entityInDB);

        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.createUsuario(entity);
        });
    }

    @Test
    public void testGetUsuarios() {
        List<UsuarioEntity> list = usuarioService.obtenerUsuarios();
        assertNotNull(list);

        for (UsuarioEntity entity : data) {
            boolean found = false;
            for (UsuarioEntity storedEntity : list) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"PROFESOR", "PROFESORVIRTUAL", "PROFESORPRESENCIAL", "ESTUDIANTE", "RANDOM"})
    public void testGetUsuariosPorTipo(String tipo) {
        UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(entity);

        if (List.of("PROFESOR", "PROFESORVIRTUAL", "PROFESORPRESENCIAL", "ESTUDIANTE").contains(tipo)) {
            entity.setTipo(tipo);
            List<UsuarioEntity> result = usuarioService.obtenerUsuariosPorTipo(tipo);
            assertNotNull(result);
        } else {
            assertThrows(IllegalArgumentException.class, () -> {
                usuarioService.obtenerUsuariosPorTipo(tipo);
            });
        }
    }

    @Test
    public void testGetUsuario() throws EntityNotFoundException {
        UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(entity);
        UsuarioEntity result = usuarioService.getUsuario(entity.getId());
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.getUsuario(factory.manufacturePojo(Long.class));
        });
    }

    @Test
    public void testUpdateUsuario() throws EntityNotFoundException {
        UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(entity);
        UsuarioEntity result = usuarioService.updateUsuario(entity.getId(), entity);
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.updateUsuario(factory.manufacturePojo(Long.class), entity);
        });

        ProfesorEntity p1 = factory.manufacturePojo(ProfesorEntity.class);
        ProfesorVirtualEntity p2 = factory.manufacturePojo(ProfesorVirtualEntity.class);
        ProfesorPresencialEntity p3 = factory.manufacturePojo(ProfesorPresencialEntity.class);
        EstudianteEntity e1 = factory.manufacturePojo(EstudianteEntity.class);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.persist(e1);

        UsuarioEntity r1 = usuarioService.updateUsuario(entity.getId(), p1);
        assertNotNull(r1);

        UsuarioEntity r2 = usuarioService.updateUsuario(entity.getId(), p2);
        assertNotNull(r2);

        UsuarioEntity r3 = usuarioService.updateUsuario(entity.getId(), p3);
        assertNotNull(r3);

        UsuarioEntity r4 = usuarioService.updateUsuario(entity.getId(), e1);
        assertNotNull(r4);

        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.updateUsuario(factory.manufacturePojo(Long.class), p1);
        });
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.updateUsuario(factory.manufacturePojo(Long.class), p2);
        });
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.updateUsuario(factory.manufacturePojo(Long.class), p3);
        });
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.updateUsuario(factory.manufacturePojo(Long.class), e1);
        });
    }

    @Test
    public void testDeleteUsuario() throws EntityNotFoundException {
        UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(entity);
        usuarioService.deleteUsuario(entity.getId());
        UsuarioEntity entityInDB = entityManager.find(UsuarioEntity.class, entity.getId());
        assertNull(entityInDB);
    }

    @Test
    public void testGetUsuarioByCorreo() throws EntityNotFoundException {
        UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(entity);
        UsuarioEntity result = usuarioService.getUsuarioByCorreo(entity.getCorreo());
        assertNotNull(result);

        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.getUsuarioByCorreo(factory.manufacturePojo(String.class));
        });
    }

}
