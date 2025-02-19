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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase que prueba el servicio de la relación entre Tematica y Profesor
 *
 * @author Daniel-VergaraM
 */
@DataJpaTest
@Transactional
@Import(TematicaProfesorService.class)
public class TematicaProfesorServiceTest {

    @Autowired
    private TematicaProfesorService service;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private List<TematicaEntity> tematicas = new ArrayList<>();

    private List<ProfesorEntity> profesores = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TematicaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ProfesorEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TematicaEntity tematica = factory.manufacturePojo(TematicaEntity.class);
            entityManager.persist(tematica);
            tematicas.add(tematica);
        }
        for (int i = 0; i < 3; i++) {
            ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
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
    public void addProfesorTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematica(profesor.getId(), tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(IllegalArgumentException.class, () -> {
            service.agregarProfesorATematica(id, tematica.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.agregarProfesorATematica(profesor2.getId(), id);
        });
    }

    @Test
    public void getProfesoresTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        ProfesorEntity result = service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        assertNotNull(result);
        List<ProfesorEntity> profesoresTematica = service.obtenerProfesores(tematica.getId());
        assertTrue(profesoresTematica.size() == 1);
    }

    @Test
    public void getTematicasTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        List<TematicaEntity> tematicasProfesor = service.obtenerTematicas(profesor.getId());
        assertTrue(tematicasProfesor.size() == 1);

        Long id = factory.manufacturePojo(Long.class);
        assertThrows(IllegalArgumentException.class, () -> {
            service.obtenerTematicas(id);
        });
    }

    @Test
    public void removeProfesorTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        service.eliminarProfesorDeTematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematica(profesor.getId(), tematica.getId());
        assertFalse(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(IllegalArgumentException.class, () -> {
            service.eliminarProfesorDeTematica(id, tematica.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.eliminarProfesorDeTematica(profesor2.getId(), id);
        });
    }

    @Test
    public void profesorPoseeTematicaTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematica(profesor.getId(), tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(IllegalArgumentException.class, () -> {
            service.profesorPoseeTematica(id, tematica.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.profesorPoseeTematica(profesor2.getId(), id);
        });

    }

    @Test
    public void tematicaPoseeProfesorTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.tematicaPoseeProfesor(profesor.getId(), tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(IllegalArgumentException.class, () -> {
            service.tematicaPoseeProfesor(id, tematica.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.tematicaPoseeProfesor(profesor2.getId(), id);
        });

    }

    @Test
    public void profesorPoseeTematicasTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematicas(profesor.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        assertThrows(IllegalArgumentException.class, () -> {
            service.profesorPoseeTematicas(id);
        });
    }

    @Test
    public void tematicaPoseeProfesoresTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.tematicaPoseeProfesores(tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        assertThrows(IllegalArgumentException.class, () -> {
            service.tematicaPoseeProfesores(id);
        });
    }

    @Test
    public void eliminarProfesorDeTematicasTest() {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        service.eliminarProfesorDeTematicas(profesor.getId());
        Boolean si = service.profesorPoseeTematicas(profesor.getId());
        assertFalse(si);

        Long id = factory.manufacturePojo(Long.class);

        assertThrows(IllegalArgumentException.class, () -> {
            service.eliminarProfesorDeTematicas(id);
        });
    }

    @Test
    public void eliminarTematicaProfesorTest() {

        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        service.eliminarTematicaProfesor(profesor.getId(), tematica.getId());
        Boolean si = service.tematicaPoseeProfesores(tematica.getId());
        assertFalse(si);

        Long id = factory.manufacturePojo(Long.class);

        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            service.eliminarTematicaProfesor(id, tematica.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.eliminarTematicaProfesor(profesor.getId(), id);
        });

    }

}
