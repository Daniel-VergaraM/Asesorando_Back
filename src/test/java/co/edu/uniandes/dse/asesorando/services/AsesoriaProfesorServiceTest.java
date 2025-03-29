package co.edu.uniandes.dse.asesorando.services;
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
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de lógica de AsesoriaProfesorService
 */
@DataJpaTest
@Transactional
@Import({AsesoriaProfesorService.class})
public class AsesoriaProfesorServiceTest {
    
    @Autowired
    private AsesoriaProfesorService asesoriaProfesorService;
    
    @Autowired
    private AsesoriaRepository asesoriaRepository;
    
    
    @Autowired  
    private TestEntityManager entityManager;
    
    private PodamFactory factory = new PodamFactoryImpl();
    
    private ProfesorEntity profesor;
    private List<AsesoriaEntity> asesoriaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    
    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM AsesoriaEntity");
        entityManager.getEntityManager().createQuery("DELETE FROM ProfesorEntity");
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas yy persiste al profesor
     */
    private void insertData() {
        profesor = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(profesor);

        for (int i = 0; i < 3; i++) {
            AsesoriaEntity asesoria = factory.manufacturePojo(AsesoriaEntity.class);
            asesoria.setProfesor(profesor);
            entityManager.persist(asesoria);
            asesoriaList.add(asesoria);
        }
    }
    /**
     * Método para crear una asesoría y asignarla a un profesor.
     *
     * Verifica que la asesoría se cree correctamente y que sus atributos 
     * (temática, duración, tipo y área) coincidan con los valores esperados.
     */
    @Test
    void testCrearAsesoriaParaProfesor() throws EntityNotFoundException {
    AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
    entityManager.persist(nuevaAsesoria); // Persistimos la asesoría antes de asignarla
    entityManager.flush(); // Forzamos que se genere un ID válido

    AsesoriaEntity resultado = asesoriaProfesorService.crearAsesoriaParaProfesor(profesor.getId(), nuevaAsesoria.getId());

    assertNotNull(resultado);
    assertEquals(nuevaAsesoria.getTematica(), resultado.getTematica());
}

    


    /**
     * Método para listar todas las asesorías de un profesor.
     *
     * Verifica que la cantidad de asesorías obtenidas coincida con el número 
     * de asesorías creadas en la base de datos para el profesor.
     */

     @Test
     void testListarAsesoriasDeProfesor() {
         try {List<AsesoriaEntity> resultado = asesoriaProfesorService.listarAsesoriasDeProfesor(profesor.getId());
            assertEquals(asesoriaList.size(), resultado.size());} 
            catch (EntityNotFoundException ex) { fail("EntityNotFoundException was thrown");
}
     }

    /**
     * Método para actualizar una asesoría de un profesor.
     *
     * Verifica que la asesoría actualizada mantenga la relación con el profesor 
     * y que sus atributos (temática, duración, tipo y área) coincidan con los valores 
     * de la asesoría modificada.
     */
    @Test
    void testActualizarAsesoriaDeProfesor() throws EntityNotFoundException {
        AsesoriaEntity asesoria = asesoriaList.get(0);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        nuevaAsesoria.setId(asesoria.getId());
        AsesoriaEntity resultado = asesoriaProfesorService.actualizarAsesoriaDeProfesor(profesor.getId(), asesoria.getId(), nuevaAsesoria);
        assertNotNull(resultado);
        assertEquals(nuevaAsesoria.getTematica(), resultado.getTematica());
    }
    

    /**
     * Método para eliminar una asesoría de un profesor.
     *
     * Verifica que la asesoría sea eliminada correctamente y que no se encuentre en la base de datos.
     */

     @Test
     void testEliminarAsesoriaDeProfesor() throws EntityNotFoundException {
         AsesoriaEntity asesoria = asesoriaList.get(0);
         asesoriaProfesorService.eliminarAsesoriaDeProfesor(profesor.getId(), asesoria.getId());
         assertFalse(asesoriaRepository.findById(asesoria.getId()).isPresent());
     }
        /**
     * Método para probar qué pasa si intentamos crear una asesoría con un profesor que no existe.
     *
     * Debería lanzar un error porque el profesor con ese ID no está en la base de datos.
     */
    @Test
    void testCrearAsesoriaParaProfesorConProfesorNoExistente() {
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        entityManager.persist(nuevaAsesoria);
        entityManager.flush();
        assertThrows(EntityNotFoundException.class, () -> {asesoriaProfesorService.crearAsesoriaParaProfesor(9999L, nuevaAsesoria.getId());});
    }

    /**
     * Método para probar qué pasa si intentamos crear una asesoría con un ID de asesoría que no existe.
     *
     * También debería lanzar un error porque esa asesoría no está en la base de datos.
     */
    @Test
    void testCrearAsesoriaParaProfesorConAsesoriaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {asesoriaProfesorService.crearAsesoriaParaProfesor(profesor.getId(), 9999L);});
    }

    /**
     * Método para probar la actualización de una asesoría que no existe.
     *
     * Si intentamos actualizar algo que no está en la base de datos, debería fallar con un error.
     */
    @Test
    void testActualizarAsesoriaDeProfesorConAsesoriaNoExistente() {
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        assertThrows(EntityNotFoundException.class, () -> {asesoriaProfesorService.actualizarAsesoriaDeProfesor(profesor.getId(), 9999L, nuevaAsesoria);});
    }

    /**
     * Método para probar qué pasa si un profesor intenta actualizar una asesoría que no le pertenece.
     *
     * Debería fallar porque solo el dueño de la asesoría puede modificarla.
     */
    @Test
    void testActualizarAsesoriaDeProfesorConProfesorIncorrecto() {
        ProfesorEntity otroProfesor = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(otroProfesor);
        AsesoriaEntity asesoria = asesoriaList.get(0);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        nuevaAsesoria.setId(asesoria.getId());
        assertThrows(EntityNotFoundException.class, () -> {asesoriaProfesorService.actualizarAsesoriaDeProfesor(otroProfesor.getId(), asesoria.getId(), nuevaAsesoria);});
    }

    /**
     * Método para probar la eliminación de una asesoría que no existe.
     *
     * Como no está en la base de datos, debería lanzar un error.
     */
    @Test
    void testEliminarAsesoriaDeProfesorConAsesoriaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {asesoriaProfesorService.eliminarAsesoriaDeProfesor(profesor.getId(), 9999L);});
    }

    /**
     * Método para probar qué pasa si un profesor intenta eliminar una asesoría que no le pertenece.
     *
     * Debería fallar porque solo el dueño de la asesoría puede eliminarla.
     */
    @Test
    void testEliminarAsesoriaDeProfesorConProfesorIncorrecto() {
        ProfesorEntity otroProfesor = factory.manufacturePojo(ProfesorEntity.class);
        entityManager.persist(otroProfesor);
        AsesoriaEntity asesoria = asesoriaList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {asesoriaProfesorService.eliminarAsesoriaDeProfesor(otroProfesor.getId(), asesoria.getId());});
    }
}
