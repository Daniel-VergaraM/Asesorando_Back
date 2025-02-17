package co.edu.uniandes.dse.asesorando.services;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import uk.co.jemos.podam.api.PodamFactoryImpl;
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
    void testGetCalendarios(){
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
	void testDeleteInvalidCalendario(){
		assertThrows(EntityNotFoundException.class, ()->{
			calendarioService.deleteCalendario(0L);
		});
	} 
}
