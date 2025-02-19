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
import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.AsesoriaCalendarioService;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de lógica de AsesoriaCalendarioService mediante PODAM
 */
@DataJpaTest
@Transactional
@Import({AsesoriaCalendarioService.class})
public class AsesoriaCalendarioServiceTest {
    
    @Autowired
    private AsesoriaCalendarioService asesoriaCalendarioService;
    
    @Autowired
    private AsesoriaRepository asesoriaRepository;
    
    @Autowired
    private CalendarioRepository calendarioRepository;
    
    @Autowired  
    private TestEntityManager entityManager;
    
    private PodamFactory factory = new PodamFactoryImpl();
    
    private CalendarioEntity calendario;
    private List<AsesoriaEntity> asesoriaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    
    /**
     * Limpia los datos  que usamos en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM AsesoriaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM CalendarioEntity").executeUpdate();
    }

    /**
     * Creacion de casos iniciales mediante PODAM, crea 3 asesorias y un calendario.
     */
    private void insertData() {
        calendario = factory.manufacturePojo(CalendarioEntity.class);
        entityManager.persist(calendario);

        for (int i = 0; i < 3; i++) {
            AsesoriaEntity asesoria = factory.manufacturePojo(AsesoriaEntity.class);
            asesoria.setCalendario(calendario);
            entityManager.persist(asesoria);
            asesoriaList.add(asesoria);
        }
    }



    /**
     * Método para crear una asesoría dentro de un calendario.
     *
     * Verifica que la asesoría se cree correctamente y que sus atributos 
     * (temática, duración, tipo y área) coincidan con los valores esperados. 
     * Además, comprueba que la asesoría creada no sea nula.
     * Parametros que arrojan excepciones: calendarioId, asesoria
     * @throws EntityNotFoundException   Si el calendario no existe en la base de datos.
     * @throws IllegalOperationException Si la asesoría ya está asignada a otro calendario.
     */
    @Test
    void testCrearAsesoriaEnCalendario() throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        AsesoriaEntity resultado = asesoriaCalendarioService.crearAsesoriaEnCalendario(calendario.getId(), nuevaAsesoria);
        
        assertNotNull(resultado);
        assertEquals(nuevaAsesoria.getTematica(), resultado.getTematica());
        assertEquals(nuevaAsesoria.getDuracion(), resultado.getDuracion());
        assertEquals(nuevaAsesoria.getTipo(), resultado.getTipo());
        assertEquals(nuevaAsesoria.getArea(), resultado.getArea());
    }
    
    /**
     * Método para listar todas las asesorías de un calendario.
     *
     * Verifica que la cantidad de asesorías obtenidas sea igual al número de asesorías 
     * previamente creadas y asociadas al calendario.
     *
     * @throws EntityNotFoundException Si el calendario no existe en la base de datos.
     */
    @Test
    void testListarAsesoriasDeCalendario() throws EntityNotFoundException {
        List<AsesoriaEntity> resultado = asesoriaCalendarioService.listarAsesoriasDeCalendario(calendario.getId());
        assertEquals(asesoriaList.size(), resultado.size());
    }
    /**
     * Método para actualizar una asesoría dentro de un calendario.
     *
     * Verifica que la asesoría seleccionada se actualice correctamente y 
     * que sus atributos (temática, duración, tipo y área) coincidan con los nuevos valores.
     * También comprueba que la asesoría actualizada no sea nula.
     *
     * @throws EntityNotFoundException   Si el calendario o la asesoría no existen en la base de datos.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */

    @Test
    void testActualizarAsesoriaEnCalendario() throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesoria = asesoriaList.get(0);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        
        AsesoriaEntity resultado = asesoriaCalendarioService.actualizarAsesoriaEnCalendario(calendario.getId(), asesoria.getId(), nuevaAsesoria);
        
        assertNotNull(resultado);
        assertEquals(nuevaAsesoria.getTematica(), resultado.getTematica());
        assertEquals(nuevaAsesoria.getDuracion(), resultado.getDuracion());
        assertEquals(nuevaAsesoria.getTipo(), resultado.getTipo());
        assertEquals(nuevaAsesoria.getArea(), resultado.getArea());
    }
    
    /**
     * Método para eliminar una asesoría de un calendario.
     *
     * Verifica que la asesoría seleccionada sea eliminada correctamente y 
     * que ya no esté presente en la base de datos.
     *
     * @throws EntityNotFoundException   Si el calendario o la asesoría no existen en la base de datos.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */
    @Test
    void testEliminarAsesoriaDeCalendario() throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesoria = asesoriaList.get(0);
        asesoriaCalendarioService.eliminarAsesoriaDeCalendario(calendario.getId(), asesoria.getId());
        assertFalse(asesoriaRepository.findById(asesoria.getId()).isPresent());
    }
}
