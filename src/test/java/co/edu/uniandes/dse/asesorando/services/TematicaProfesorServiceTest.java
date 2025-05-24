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
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
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
class TematicaProfesorServiceTest {

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
            tematica.setProfesores(new ArrayList<>());
            entityManager.persist(tematica);
            tematicas.add(tematica);
        }
        for (int i = 0; i < 3; i++) {
            ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
            profesor.setTematicas(new ArrayList<>());
            entityManager.persist(profesor);
            profesores.add(profesor);
        }
    }

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    @Test
    void addProfesorTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematica(profesor.getId(), tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(EntityNotFoundException.class, () -> {
            service.agregarProfesorATematica(id, tematica.getId());
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.agregarProfesorATematica(profesor2.getId(), id);
        });
    }

    @Test
    void getTematicasTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        List<TematicaEntity> tematicasProfesor = service.obtenerTematicas(profesor.getId());
        assertEquals(1, tematicasProfesor.size());

        Long id = factory.manufacturePojo(Long.class);
        assertThrows(EntityNotFoundException.class, () -> {
            service.obtenerTematicas(id);
        });
    }

    @Test
    void removeProfesorTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        service.eliminarProfesorDeTematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematica(profesor.getId(), tematica.getId());
        assertFalse(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(EntityNotFoundException.class, () -> {
            service.eliminarProfesorDeTematica(id, tematica.getId());
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.eliminarProfesorDeTematica(profesor2.getId(), id);
        });
    }

    @Test
    void profesorPoseeTematicaTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematica(profesor.getId(), tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(EntityNotFoundException.class, () -> {
            service.profesorPoseeTematica(id, tematica.getId());
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.profesorPoseeTematica(profesor2.getId(), id);
        });

    }

    @Test
    void tematicaPoseeProfesorTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.tematicaPoseeProfesor(profesor.getId(), tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        ProfesorEntity profesor2 = profesores.get(1);

        assertThrows(EntityNotFoundException.class, () -> {
            service.tematicaPoseeProfesor(id, tematica.getId());
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.tematicaPoseeProfesor(profesor2.getId(), id);
        });

    }

    @Test
    void profesorPoseeTematicasTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.profesorPoseeTematicas(profesor.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        assertThrows(EntityNotFoundException.class, () -> {
            service.profesorPoseeTematicas(id);
        });
    }

    @Test
    void tematicaPoseeProfesoresTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        Boolean si = service.tematicaPoseeProfesores(tematica.getId());
        assertTrue(si);

        Long id = factory.manufacturePojo(Long.class);

        assertThrows(EntityNotFoundException.class, () -> {
            service.tematicaPoseeProfesores(id);
        });
    }

    @Test
    void eliminarProfesorDeTematicasTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        service.eliminarProfesorDeTematicas(profesor.getId());
        Boolean si = service.profesorPoseeTematicas(profesor.getId());
        assertFalse(si);

        Long id = factory.manufacturePojo(Long.class);

        assertThrows(EntityNotFoundException.class, () -> {
            service.eliminarProfesorDeTematicas(id);
        });
    }


    @Test
    void getProfesoresDeTematicaTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        ProfesorEntity profesor = profesores.get(0);
        service.agregarProfesorATematica(profesor.getId(), tematica.getId());
        List<ProfesorEntity> profesoresTematica = service.getProfesoresDeTematica(tematica.getId());
        assertNotNull(profesoresTematica);
        assertEquals(1, profesoresTematica.size());

        Long id = factory.manufacturePojo(Long.class);
        assertThrows(EntityNotFoundException.class, () -> {
            service.getProfesoresDeTematica(id);
        });
    }

    @Test
    void actualizarTematicaProfesorTest() throws EntityNotFoundException {
        TematicaEntity tematica = tematicas.get(0);
        List<ProfesorEntity> nuevosProfesores = new ArrayList<>();
        nuevosProfesores.add(profesores.get(1));
        nuevosProfesores.add(profesores.get(2));
        List<ProfesorEntity> profesoresActualizados = service.actualizarTematicaProfesor(tematica.getId(), nuevosProfesores);
        assertNotNull(profesoresActualizados);
        assertEquals(2, profesoresActualizados.size());
        assertTrue(profesoresActualizados.containsAll(nuevosProfesores));

        Long id = factory.manufacturePojo(Long.class);
        assertThrows(EntityNotFoundException.class, () -> {
            service.actualizarTematicaProfesor(id, nuevosProfesores);
        });
    }

    @Test
    void eliminarProfesoresDeTematicaTest() throws EntityNotFoundException {
        TematicaEntity tematica = factory.manufacturePojo(TematicaEntity.class);

        for (int i = 0; i < 5; i++) {
            ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
            entityManager.persist(profesor);

            tematica.getProfesores().add(profesor);
            profesor.getTematicas().add(tematica);
        }

        entityManager.persist(tematica);
        entityManager.flush();

        service.eliminarProfesoresDeTematica(tematica.getId());

        List<ProfesorEntity> profesores2 = service.getProfesoresDeTematica(tematica.getId());
        assertNotNull(profesores2);
        assertTrue(profesores2.isEmpty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.eliminarProfesoresDeTematica(factory.manufacturePojo(Long.class));
        });

    }

    @Test
    void eliminarTematicasDeProfesorTest() throws EntityNotFoundException {
        ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);

        for (int i = 0; i < 5; i++) {
            TematicaEntity tematica = factory.manufacturePojo(TematicaEntity.class);
            entityManager.persist(tematica);

            profesor.getTematicas().add(tematica);
            tematica.getProfesores().add(profesor);
        }
        entityManager.persist(profesor);
        entityManager.flush();

        service.eliminarTematicasDeProfesor(profesor.getId());
        List<TematicaEntity> tematicas2 = service.obtenerTematicas(profesor.getId());
        assertNotNull(tematicas2);
        assertTrue(tematicas2.isEmpty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.eliminarTematicasDeProfesor(factory.manufacturePojo(Long.class));
        });
    }

    @Test
    void getProfesorDeTematicaTest() throws EntityNotFoundException {
        ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
        TematicaEntity tematica = factory.manufacturePojo(TematicaEntity.class);

        profesor.getTematicas().add(tematica);
        tematica.getProfesores().add(profesor);
        entityManager.persist(profesor);
        entityManager.persist(tematica);

        entityManager.flush();

        ProfesorEntity result = service.getProfesorDeTematica(profesor.getId(), tematica.getId());

        assertNotNull(result);
        assertEquals(result.getId(), profesor.getId());
        assertTrue(result.getTematicas().contains(tematica));
        assertTrue(tematica.getProfesores().contains(profesor));

        assertThrows(EntityNotFoundException.class, () -> {
            service.getProfesorDeTematica(factory.manufacturePojo(Long.class), factory.manufacturePojo(Long.class));
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.getProfesorDeTematica(profesor.getId(), factory.manufacturePojo(Long.class));
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.getProfesorDeTematica(factory.manufacturePojo(Long.class), tematica.getId());
        });

    }

    @Test
    void getTematicaDeProfesorTest() throws EntityNotFoundException {
        ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
        TematicaEntity tematica = factory.manufacturePojo(TematicaEntity.class);

        profesor.getTematicas().add(tematica);
        tematica.getProfesores().add(profesor);
        entityManager.persist(profesor);
        entityManager.persist(tematica);

        entityManager.flush();

        TematicaEntity result = service.getTematicaDeProfesor(tematica.getId(), profesor.getId());
        assertNotNull(result);
        assertEquals(result.getId(), tematica.getId());
        assertTrue(result.getProfesores().contains(profesor));
        assertTrue(profesor.getTematicas().contains(tematica));

        assertThrows(EntityNotFoundException.class, () -> {
            service.getTematicaDeProfesor(factory.manufacturePojo(Long.class), factory.manufacturePojo(Long.class));
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.getTematicaDeProfesor(tematica.getId(), factory.manufacturePojo(Long.class));
        });

        assertThrows(EntityNotFoundException.class, () -> {
            service.getTematicaDeProfesor(factory.manufacturePojo(Long.class), profesor.getId());
        });
    }
}
