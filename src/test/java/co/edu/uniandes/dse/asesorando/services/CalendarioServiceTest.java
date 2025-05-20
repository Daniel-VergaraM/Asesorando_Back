package co.edu.uniandes.dse.asesorando.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Calendario
 *
 * @author jmrojasp1
 */
@DataJpaTest
@Transactional
@Import({ CalendarioService.class })
class CalendarioServiceTest {

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CalendarioEntity> calendarioList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CalendarioEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            CalendarioEntity entity = factory.manufacturePojo(CalendarioEntity.class);
            entityManager.persist(entity);
            calendarioList.add(entity);
        }
    }

    /**
     * Prueba para crear un Calendario.
     */
    @Test
    void testCreateCalendario() throws IllegalOperationException {
        CalendarioEntity entity = factory.manufacturePojo(CalendarioEntity.class);
        if (entity.getId() == null) {
            entity.setId(factory.manufacturePojo(Long.class));
        }
        CalendarioEntity result = calendarioService.createCalendario(entity);
        assertNotNull(result);

        CalendarioEntity entityInDatabase = entityManager.find(CalendarioEntity.class, result.getId());
        assertNotNull(entityInDatabase);
    }    /**
     * Prueba para crear un Calendario con fechaInicio repetida.
     */
    @Test
    void testCreateCalendarioWithSameDate() {
        CalendarioEntity newEntity1 = factory.manufacturePojo(CalendarioEntity.class);
        newEntity1.setFechaInicio(calendarioList.get(0).getFechaInicio());
        
        assertThrows(IllegalOperationException.class, () -> {
            calendarioService.createCalendario(newEntity1);
        });

    }

    /**
     * Prueba para consultar la lista de Calendarios.
     */
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

    /**
     * Prueba para consultar un Calendario
     */
    @Test
    void testGetCalendario() throws EntityNotFoundException {
        CalendarioEntity entity = calendarioList.get(0);
        CalendarioEntity resultEntity = calendarioService.getCalendario(entity.getId());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaInicio(), resultEntity.getFechaInicio());
        assertEquals(entity.getFechaFin(), resultEntity.getFechaFin());
    }

    /**
     * Prueba para consultar un Calendario que no existe.
     */
    @Test
    void testGetInvalidCalendario() {
        assertThrows(EntityNotFoundException.class, () -> {
            calendarioService.getCalendario(0L);
        });
    }

    /**
     * Prueba para actualizar un Calendario.
     */
    @Test
    void testUpdateCalendario() throws IllegalOperationException {
        CalendarioEntity entity = calendarioList.get(0);

        calendarioService.updateCalendario(entity);

        CalendarioEntity resp = entityManager.find(CalendarioEntity.class, entity.getId());
        assertEquals(entity.getId(), resp.getId());
        assertEquals(entity.getFechaInicio(), resp.getFechaInicio());
        assertEquals(entity.getFechaFin(), resp.getFechaFin());

    }

    /**
     * Prueba para actualizar un Calendario que no existe.
     */
    @Test
    void testUpdateCalendarioInvalid() {
        assertThrows(IllegalOperationException.class, () -> {
            CalendarioEntity entity = factory.manufacturePojo(CalendarioEntity.class);
            entity.setFechaInicio(null);
            entity.setFechaFin(null);
            calendarioService.updateCalendario(entity);
        });

    }

    /**
     * Prueba para eliminar un Calendario.
     */
    @Test
    void testDeleteCalendario() throws EntityNotFoundException {
        CalendarioEntity entity = calendarioList.get(0);
        calendarioService.deleteCalendario(entity.getId());
        CalendarioEntity deleted = entityManager.find(CalendarioEntity.class, entity.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar un Calendario que no existe.
     */
    @Test
    void testDeleteInvalidCalendario() {
        assertThrows(EntityNotFoundException.class, () -> {
            calendarioService.deleteCalendario(0L);
        });
    }

    /**
     * Prueba para buscar un Calendario por su fecha de inicio.
     */
    @Test
    void testGetCalendarioByFechaInicio() throws IllegalOperationException {

        CalendarioEntity entity = calendarioList.get(0);
        CalendarioEntity resultEntity = calendarioService.getCalendarioByFechaInicio(entity.getFechaInicio());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaInicio(), resultEntity.getFechaInicio());
        assertEquals(entity.getFechaFin(), resultEntity.getFechaFin());
    }

    /**
     * Prueba para buscar un Calendario por su fecha de inicio invalida.
     */
    @Test
    void testGetInvalidCalendarioByFechaInicio() {
        assertThrows(IllegalOperationException.class, () -> {
            calendarioService.getCalendarioByFechaInicio(null);
        });
    }

    /**
     * Prueba para buscar un Calendario por su fecha final
     */
    @Test
    void testGetCalendarioByFechaFin() throws IllegalOperationException {

        CalendarioEntity entity = calendarioList.get(0);
        CalendarioEntity resultEntity = calendarioService.getCalendarioByFechaFin(entity.getFechaFin());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getFechaInicio(), resultEntity.getFechaInicio());
        assertEquals(entity.getFechaFin(), resultEntity.getFechaFin());
    }

    /**
     * Prueba para buscar un Calendario por su fecha de fin invalida.
     */
    @Test
    void testGetInvalidCalendarioByFechaFin() {
        assertThrows(IllegalOperationException.class, () -> {
            calendarioService.getCalendarioByFechaFin(null);
        });
    }

    /**
     * Prueba para buscar un Calendario por su fecha de inicio menor a la dada.
     */
    @Test
    void testGetCalendarioByFechaInicioLessThan() {
        CalendarioEntity entity = calendarioList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            calendarioService.getCalendarioByFechaInicioLessThan(entity.getFechaInicio());
        });
    }

    /**
     * Prueba para buscar un Calendario por su fecha de inicio menor a la dada
     * invalida.
     */
    @Test
    void testGetInvalidCalendarioByFechaInicioLessThan() {
        assertThrows(EntityNotFoundException.class, () -> {
            calendarioService.getCalendarioByFechaInicioLessThan(null).get(0);
        });
    }

    /**
     * Prueba para buscar un Calendario por su fecha de inicio y fecha de fin dadas.
     */
    @Test
    void testGetCalendarioByFechaInicioBetween() throws EntityNotFoundException {
        CalendarioEntity entityInicio = calendarioList.get(0);
        CalendarioEntity entityFin = calendarioList.get(calendarioList.size() - 1);

        List<CalendarioEntity> resultEntities = calendarioService
                .getCalendarioByFechaInicioBetween(entityInicio.getFechaInicio(), entityFin.getFechaInicio());

        assertNotNull(resultEntities);
        assertFalse(resultEntities.isEmpty());

        for (CalendarioEntity resultEntity : resultEntities) {
            assertTrue(resultEntity.getFechaInicio().after(entityInicio.getFechaInicio())
                    || resultEntity.getFechaInicio().equals(entityInicio.getFechaInicio()));
            assertTrue(resultEntity.getFechaInicio().before(entityFin.getFechaInicio())
                    || resultEntity.getFechaInicio().equals(entityFin.getFechaInicio()));
        }

    }

    /**
     * Prueba para buscar un Calendario por su fecha de inicio y fecha de fin
     * invalidas dadas.
     */
    @Test
    void testGetInvalidCalendarioByFechaInicioBetween() {

        Date fechaInicio = new Date(0);
        Date fechaFin = new Date(1);

        assertThrows(EntityNotFoundException.class, () -> {
            calendarioService.getCalendarioByFechaInicioBetween(fechaInicio, fechaFin);
        });
    }
}
