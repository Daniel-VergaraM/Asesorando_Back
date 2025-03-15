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
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de AsesoriaService
 *
 * @author Juan Caicedo
 */
@DataJpaTest
@Transactional
@Import(AsesoriaService.class)
public class AsesoriaTest {
    @Autowired
	private AsesoriaService AsesoriaService;
	@Autowired
	private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<AsesoriaEntity> asesorias = new ArrayList<>();

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

	    entityManager.getEntityManager().createQuery("delete from AsesoriaEntity");
        entityManager.getEntityManager().createQuery("delete from ProfesorEntity");


		
	}
    /**
    * Método para insertar datos de prueba en la base de datos.
    * Crea y persiste tres entidades de tipo AsesoriaEntity utilizando un PODAM.
    */

    private void insertData() { 
        for (int i = 0; i < 3; i++) {
            ProfesorEntity profesor = factory.manufacturePojo(ProfesorEntity.class);
            entityManager.persist(profesor);
            AsesoriaEntity asesoria = factory.manufacturePojo(AsesoriaEntity.class);
            asesoria.setProfesor(profesor);
            entityManager.persist(asesoria);
            asesorias.add(asesoria);
        }
    }
    
    /**
    * Prueba unitaria para la creación de una asesoría.
    * Se crea un profesor en la base de datos y se usa su ID para registrar una nueva asesoría a través del servicio.
    * Despues , se verifica que la asesoría creada no sea nula y se consulta en la base de datos para confirmar su existencia.
    * Compatacion  la temática de la asesoría creada con la esperada para asegurar que los datos fueron almacenados correctamente.
    * Parámetros:
    * @throws EntityNotFoundException si el profesor no se encuentra en la base de datos.
    * @throws IllegalOperationException si la operación no es válida según las reglas del negocio.
    */



    @Test
    void createAsesoriaTest() throws EntityNotFoundException, IllegalOperationException {
        ProfesorEntity profesor = entityManager.persist(factory.manufacturePojo(ProfesorEntity.class));
        AsesoriaEntity asesoria = new AsesoriaEntity();
        asesoria.setDuracion("60 minutos");
        asesoria.setTematica("Matemáticas");
        asesoria.setArea("Ciencias");
        AsesoriaEntity result = AsesoriaService.createAsesoria(asesoria, profesor.getId());
        assertNotNull(result);
        AsesoriaEntity entity = entityManager.find(AsesoriaEntity.class, result.getId());
        assertEquals("Matemáticas", entity.getTematica());
}

    /**
    * Prueba unitaria para obtener una asesoría existente en la base de datos.
    * Se recupera una asesoría de la lista de asesorías y se consulta su información en el servicio, parecida a la implementacion anterior
    * Se verifica que la entidad obtenida no sea nula y que su ID coincida con la esperada.
    *Parametros que arroja:
    * @throws EntityNotFoundException si la entidad no se encuentra en la base de datos.
    * @throws IllegalOperationException si la operación no es válida según las reglas del negocio.
    */
    @Test
    void getAsesoriaTest() throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesoria = asesorias.get(0);
        AsesoriaEntity result = AsesoriaService.getAsesoriaEntity(asesoria.getId());
        assertNotNull(result);
        assertEquals(asesoria.getId(), result.getId());
    }
    /**
    * Prueba unitaria para actualizar una asesoría existente.
    * Se toma una asesoría de la lista, se genera una nueva instancia con datos modificados y se actualiza.
    * Despues se obtiene la entidad actualizada y se verifica que su ID coincida con la esperada.
    * Parametros que arroja:
    * @throws EntityNotFoundException si la entidad no se encuentra en la base de datos.
    * @throws IllegalOperationException si la operación no es válida según las reglas del negocio.
    */


    @Test
    void updateAsesoriaTest() throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesoria = asesorias.get(0);
        AsesoriaEntity newAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        newAsesoria.setId(asesoria.getId());
        AsesoriaService.updateAsesoriaEntity(asesoria.getId(), newAsesoria);
        AsesoriaEntity entity = entityManager.find(AsesoriaEntity.class, asesoria.getId());
        assertEquals(newAsesoria.getId(), entity.getId());
    }
    /**
    * Prueba unitaria para eliminar una asesoría existente.
    *Se toma una asesoría de la lista, se elimina a través del servicio en la posicion 0 y  se verifica que la entidad ya no exista en la base de datos.
    *Parametros:
    * @throws EntityNotFoundException si la entidad no se encuentra en la base de datos.
    * @throws IllegalOperationException si la operación no es válida según las reglas del negocio.
    */
    @Test
    void deleteAsesoriaTest() throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesoria = asesorias.get(0);
        AsesoriaService.deleteAsesoriaEntity(asesoria.getId());
        AsesoriaEntity deleted = entityManager.find(AsesoriaEntity.class, asesoria.getId());
        assertNull(deleted);
    
    }

   

    /**
     * Prueba unitaria para el método getAsesoriasByArea del servicio AsesoriaService.
     * 
     * Se crea una instancia de AsesoriaEntity con el área "Ciencias" y se persiste en la base de datos.
     * Luego, se invoca el método getAsesoriasByArea con el parámetro "Ciencias" y se verifica que:
     * - El resultado no sea nulo.
     * - La lista de asesorías no esté vacía.
     * - El área de la primera asesoría en la lista sea "Ciencias".
     * 
     * @throws IllegalOperationException si ocurre una operación ilegal durante la ejecución del método.
     */
    @Test
    void testGetAsesoriasByArea() throws IllegalOperationException {
        AsesoriaEntity asesoria = entityManager.persist(factory.manufacturePojo(AsesoriaEntity.class));
        asesoria.setArea("Ciencias");
        entityManager.persist(asesoria);

        List<AsesoriaEntity> result = AsesoriaService.getAsesoriasByArea("Ciencias");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Ciencias", result.get(0).getArea());
    }


    /**
     * Prueba unitaria para el método getAsesoriasByCompletada del servicio AsesoriaService.
     * 
     * Se crea una instancia de AsesoriaEntity con el estado completada en true y se persiste en la base de datos.
     * Luego, se invoca el método getAsesoriasByCompletada con el parámetro true y se verifica que:
     * - El resultado no sea nulo.
     * - La lista de asesorías no esté vacía.
     * - La primera asesoría en la lista tenga el estado completada en true.
     * 
     * @throws IllegalOperationException si ocurre una operación ilegal durante la ejecución del método.
     */
    @Test
    void testGetAsesoriasByCompletada() throws IllegalOperationException {
        ProfesorEntity profesor = entityManager.persist(factory.manufacturePojo(ProfesorEntity.class));
        AsesoriaEntity asesoria = factory.manufacturePojo(AsesoriaEntity.class);
        asesoria.setProfesor(profesor);
        asesoria.setCompletada(true);
        entityManager.persist(asesoria);

        List<AsesoriaEntity> result = AsesoriaService.getAsesoriasByCompletada(true, profesor.getId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getCompletada());
    }
    @Test
    void testGetAsesoriasByProfesorId() throws IllegalOperationException {
        ProfesorEntity profesor = entityManager.persist(factory.manufacturePojo(ProfesorEntity.class));
        AsesoriaEntity asesoria = entityManager.persist(factory.manufacturePojo(AsesoriaEntity.class));
        asesoria.setProfesor(profesor);
        entityManager.persist(asesoria);

        List<AsesoriaEntity> result = AsesoriaService.getAsesoriasByProfesorId(profesor.getId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(profesor.getId(), result.get(0).getProfesor().getId());
    }    
}


