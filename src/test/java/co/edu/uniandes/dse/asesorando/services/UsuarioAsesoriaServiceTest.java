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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Clase de pruebas para el servicio de UsuarioAsesoria
 * 
 * @author Daniel-VergaraM
 */
@DataJpaTest
@Transactional
@Import(UsuarioAsesoriaService.class)
public class UsuarioAsesoriaServiceTest {
    @Autowired
    private UsuarioAsesoriaService service;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private List<UsuarioEntity> dataUsuario = new ArrayList<>();
    private List<AsesoriaEntity> dataAsesoria = new ArrayList<>();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AsesoriaEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuario);
            dataUsuario.add(usuario);
        }
        for (int i = 0; i < 3; i++) {
            AsesoriaEntity asesoria = factory.manufacturePojo(AsesoriaEntity.class);
            entityManager.persist(asesoria);
            dataAsesoria.add(asesoria);
        }
    }

    @BeforeEach
    public void setUp() {
        clearData();
        insertData();
    }

    @Test
    public void addAsesoriaTest() throws EntityNotFoundException {
        UsuarioEntity usuario = dataUsuario.get(0);
        AsesoriaEntity asesoria = dataAsesoria.get(0);
        UsuarioEntity result = service.addAsesoria(usuario.getId(), asesoria.getId());
        assertTrue(result.getAsesoriasCompletadas().contains(asesoria));

        assertThrows(EntityNotFoundException.class, () -> {
            service.addAsesoria(factory.manufacturePojoWithFullData(Long.class), factory.manufacturePojoWithFullData(Long.class));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            service.addAsesoria(usuario.getId(), asesoria.getId());
        });
    }

    @Test
    public void removeAsesoriaTest() throws EntityNotFoundException {
        UsuarioEntity usuario = dataUsuario.get(0);
        AsesoriaEntity asesoria = dataAsesoria.get(0);
        usuario.getAsesoriasCompletadas().add(asesoria);
        entityManager.persist(usuario);
        UsuarioEntity result = service.removeAsesoria(usuario.getId(), asesoria.getId());
        assertFalse(result.getAsesoriasCompletadas().contains(asesoria));

        assertThrows(EntityNotFoundException.class, () -> {
            service.removeAsesoria(factory.manufacturePojoWithFullData(Long.class), factory.manufacturePojoWithFullData(Long.class));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.removeAsesoria(usuario.getId(), asesoria.getId());
        });
    }

    @Test
    public void getAsesoriasCompletadasTest() throws EntityNotFoundException {
        UsuarioEntity usuario = dataUsuario.get(0);
        AsesoriaEntity asesoria = dataAsesoria.get(0);
        usuario.getAsesoriasCompletadas().add(asesoria);
        entityManager.persist(usuario);
        Iterable<AsesoriaEntity> result = service.getAsesoriasCompletadas(usuario.getId());
        assertTrue(result.iterator().hasNext());

        assertThrows(EntityNotFoundException.class, () -> {
            service.getAsesoriasCompletadas(factory.manufacturePojoWithFullData(Long.class));
        });
    }

    
}
