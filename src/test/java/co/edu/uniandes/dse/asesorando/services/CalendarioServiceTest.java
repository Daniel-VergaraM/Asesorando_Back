package co.edu.uniandes.dse.asesorando.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({CalendarioService.class})
public class CalendarioServiceTest {

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CalendarioEntity> calendarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CalendarioEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CalendarioEntity entity = factory.manufacturePojo(CalendarioEntity.class);
            entityManager.persist(entity);
            calendarioList.add(entity);
        }
    }

    @Test
    void testCreateCalnedario() throws IllegalOperationException {
        CalendarioEntity newEntity = factory.manufacturePojo(CalendarioEntity.class);
        CalendarioEntity result = calendarioService.createCalendario(newEntity);
        assertNotNull(result);
        CalendarioEntity entity = entityManager.find(CalendarioEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getFechaInicio(), entity.getFechaInicio());
        assertEquals(newEntity.getFechaFin(), entity.getFechaFin());
    }

    @Test
    void testCreateOrganizationWithSameDate() throws IllegalOperationException {
        {
            CalendarioEntity newEntity = factory.manufacturePojo(CalendarioEntity.class);
            newEntity.setFechaInicio(calendarioList.get(0).getFechaInicio());
            calendarioService.createCalendario(newEntity);
        };
    }

    @Test
    void testGetCalendarios() {
        List<CalendarioEntity> list = calendarioService.getCalendarios();
        assertEquals(calendarioList.size(), list.size());
        for (CalendarioEntity calendarioEntity : list) {
            boolean found = false;
            for (CalendarioEntity entity : calendarioList) {
                if (calendarioEntity.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetCalendario() {
        CalendarioEntity entity = calendarioList.get(0);
        CalendarioEntity resultEntity = calendarioService.getCalendario(entity.getId());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaInicio(), resultEntity.getFechaInicio());
        assertEquals(entity.getFechaFin(), resultEntity.getFechaFin());
    }

    @Test
    void testGetInvalidCalendario() {
        assertThrows(NoSuchElementException.class, () -> {
            calendarioService.getCalendario(0L);
        });
    }

    @Test
    void testUpdateCalendario() throws EntityNotFoundException, IllegalOperationException {
        CalendarioEntity entity = calendarioList.get(0);

        calendarioService.updateCalendario(entity);

        CalendarioEntity resp = entityManager.find(CalendarioEntity.class, entity.getId());
        assertEquals(entity.getId(), resp.getId());
        assertEquals(entity.getFechaInicio(), resp.getFechaInicio());
        assertEquals(entity.getFechaFin(), resp.getFechaFin());

    }

    @Test
    void testUpdateCalendarioInvalid() {
        assertThrows(IllegalOperationException.class, () -> {
            CalendarioEntity entity = factory.manufacturePojo(CalendarioEntity.class);
            entity.setFechaInicio(null);
            entity.setFechaFin(null);
            calendarioService.updateCalendario(entity);
        });
    }

    @Test
    void testDeleteCalendario() throws EntityNotFoundException {
        CalendarioEntity entity = calendarioList.get(0);
        calendarioService.deleteCalendario(entity.getId());
        CalendarioEntity deleted = entityManager.find(CalendarioEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidCalendario() {
        assertThrows(EntityNotFoundException.class, () -> {
            calendarioService.deleteCalendario(0L);
        });
    }

    @Test
    void testGetCalendarioByFechaInicio() {

        CalendarioEntity entity = calendarioList.get(0);
        CalendarioEntity resultEntity = calendarioService.getCalendarioByFechaInicio(entity.getFechaInicio());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaInicio(), resultEntity.getFechaInicio());
        assertEquals(entity.getFechaFin(), resultEntity.getFechaFin());
    }

    @Test
    void testGetInvalidCalendarioByFechaInicio() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            calendarioService.getCalendarioByFechaInicio(null);
        });
    }

    @Test
    void testGetCalendarioByFechaFin() {

        CalendarioEntity entity = calendarioList.get(0);
        CalendarioEntity resultEntity = calendarioService.getCalendarioByFechaFin(entity.getFechaFin());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaInicio(), resultEntity.getFechaInicio());
        assertEquals(entity.getFechaFin(), resultEntity.getFechaFin());
    }

    @Test
    void testGetInvalidCalendarioByFechaFin() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            calendarioService.getCalendarioByFechaFin(null);
        });
    }

    @Test
    void testGetCalendarioByFechaInicioGreaterThan() {
        CalendarioEntity entity = calendarioList.get(0);
        List<CalendarioEntity> resultEntities = calendarioService.getCalendarioByFechaInicioGreaterThan(entity.getFechaInicio());

        assertNotNull(resultEntities);
        assertFalse(resultEntities.isEmpty(), "La lista de calendarios devuelta está vacía");

        for (CalendarioEntity resultEntity : resultEntities) {
            assertTrue(resultEntity.getFechaInicio().after(entity.getFechaInicio()),
                    "Se encontró un calendario con fecha de inicio menor o igual a la dada");
        }
    }

    @Test
    void testGetInvalidCalendarioByFechaInicioGreaterThan() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            calendarioService.getCalendarioByFechaInicioGreaterThan(null).get(0);
        });
    }

    @Test
    void testGetCalendarioByFechaInicioLessThan() {
        CalendarioEntity entity = calendarioList.get(calendarioList.size() - 1); // Tomamos el último elemento

        List<CalendarioEntity> resultEntities = calendarioService.getCalendarioByFechaInicioLessThan(entity.getFechaInicio());

        assertNotNull(resultEntities);
        assertFalse(resultEntities.isEmpty(), "La lista de calendarios devuelta está vacía");

        for (CalendarioEntity resultEntity : resultEntities) {
            assertTrue(resultEntity.getFechaInicio().before(entity.getFechaInicio()),
                    "Se encontró un calendario con fecha de inicio mayor o igual a la dada");
        }
    }

    @Test
    void testGetInvalidCalendarioByFechaInicioLessThan() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            calendarioService.getCalendarioByFechaInicioLessThan(null).get(0);
        });
    }

    @Test
    void testGetCalendarioByFechaInicioBetween() {
        CalendarioEntity entityInicio = calendarioList.get(0);
        CalendarioEntity entityFin = calendarioList.get(calendarioList.size() - 1);

        List<CalendarioEntity> resultEntities = calendarioService.getCalendarioByFechaInicioBetween(entityInicio.getFechaInicio(), entityFin.getFechaInicio());

        assertNotNull(resultEntities);
        assertFalse(resultEntities.isEmpty(), "La lista de calendarios devuelta está vacía");

        for (CalendarioEntity resultEntity : resultEntities) {
            assertTrue(resultEntity.getFechaInicio().after(entityInicio.getFechaInicio()) || resultEntity.getFechaInicio().equals(entityInicio.getFechaInicio()),
                    "Se encontró un calendario con fecha de inicio fuera del rango (menor a la esperada)");
            assertTrue(resultEntity.getFechaInicio().before(entityFin.getFechaInicio()) || resultEntity.getFechaInicio().equals(entityFin.getFechaInicio()),
                    "Se encontró un calendario con fecha de inicio fuera del rango (mayor a la esperada)");
        }
    }

    @Test
    void testGetInvalidCalendarioByFechaInicioBetween() {
        Date fechaInicio = new Date(Long.MAX_VALUE);
        Date fechaFin = new Date(Long.MAX_VALUE);

        List<CalendarioEntity> resultEntities = calendarioService.getCalendarioByFechaInicioBetween(fechaInicio, fechaFin);

        assertNotNull(resultEntities);
        assertTrue(resultEntities.isEmpty(), "Se esperaba una lista vacía, pero se encontraron resultados");
    }

    @Test
    void testFindByProfesor() {
        ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(profesor);

        List<CalendarioEntity> resultEntities = calendarioService.findByProfesor(profesor);

        assertNotNull(resultEntities);
        assertFalse(resultEntities.isEmpty(), "La lista de calendarios devuelta está vacía");

        for (CalendarioEntity resultEntity : resultEntities) {
            assertEquals(profesor.getId(), resultEntity.getProfesor().getId(), "El calendario devuelto no pertenece al profesor esperado");
        }
    }

    @Test
    void testFindByInvalidProfesor() {
        ProfesorEntity profesorInexistente = new ProfesorEntity();
        profesorInexistente.setId(999L);
        List<CalendarioEntity> resultEntities = calendarioService.findByProfesor(profesorInexistente);
        assertNotNull(resultEntities);
        assertTrue(resultEntities.isEmpty(), "Se esperaba una lista vacía, pero se encontraron resultados");
    }
}
