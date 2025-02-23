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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase que prueba la asociación entre Profesor y Calendario
 *
 * @author Daniel-VergaraM
 */
@DataJpaTest
@Transactional
@Import(ProfesorCalendarioService.class)
public class ProfesorCalendarioServiceTest {

    @Autowired
    private ProfesorCalendarioService service;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private ProfesorEntity profesor;
    private CalendarioEntity calendario;

    private List<CalendarioEntity> calendarios = new ArrayList<>();
    private List<ProfesorEntity> profesores = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CalendarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ProfesorEntity").executeUpdate();
        calendarios.clear();
        profesores.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            calendario = factory.manufacturePojo(CalendarioEntity.class);
            entityManager.persist(calendario);
            calendarios.add(calendario);
        }
        for (int i = 0; i < 3; i++) {
            profesor = factory.manufacturePojo(ProfesorEntity.class);
            entityManager.persist(profesor);
            profesores.add(profesor);
        }

    }

    @BeforeEach
    public void setUp() {
        clearData();
        insertData();
    }

    @Test
    public void testAddCalendario() {
        try {
            ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
            CalendarioEntity calendario = factory.manufacturePojo(CalendarioEntity.class);
            entityManager.persist(calendario);
            entityManager.persist(entity);
            service.addCalendario(entity.getId(), calendario.getId());

            ProfesorEntity entityInDB = entityManager.find(ProfesorEntity.class, entity.getId());
            assertEquals(calendario, entityInDB.getCalendario().get(0));

        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }

    }

    @Test
    public void testRemoveCalendario() {
        try {
            ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
            CalendarioEntity calendario = factory.manufacturePojo(CalendarioEntity.class);
            entityManager.persist(calendario);
            entityManager.persist(entity);
            service.addCalendario(entity.getId(), calendario.getId());
            service.removeCalendario(entity.getId(), calendario.getId());
            ProfesorEntity entityInDB = entityManager.find(ProfesorEntity.class, entity.getId());
            assertEquals(entityInDB.getCalendario().size(), 0);
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testGetCalendarios() {
        List<CalendarioEntity> calendario;
        for (int i = 0; i < profesores.size(); i++) {
            try {
                calendario = service.getCalendarios(profesores.get(i).getId());
                assertEquals(calendario, profesores.get(i).getCalendario());
            } catch (EntityNotFoundException ex) {
                assertNotNull(ex);
            }
        }
    }

    @Test
    public void testGetCalendario() {
        try {
            ProfesorEntity entity = factory.manufacturePojo(ProfesorEntity.class);
            CalendarioEntity calendario = factory.manufacturePojo(CalendarioEntity.class);
            entityManager.persist(calendario);
            entityManager.persist(entity);
            service.addCalendario(entity.getId(), calendario.getId());
            CalendarioEntity result = service.getCalendario(entity.getId(), calendario.getId());
            assertEquals(calendario, result);
        } catch (EntityNotFoundException ex) {
            assertNotNull(ex);
        }
    }
}
