package co.edu.uniandes.dse.asesorando.services;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.asesorando.dto.ReservaDTO;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.ReservaRepository;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReservaService.class)
class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaRepository reservaRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private EstudianteEntity estudiante;
    private AsesoriaEntity asesoria;

    @BeforeEach
    void setUp() {
        entityManager.getEntityManager().createQuery("DELETE FROM ReservaEntity").executeUpdate();

        estudiante = factory.manufacturePojo(EstudianteEntity.class);
        asesoria = factory.manufacturePojo(AsesoriaEntity.class);

        entityManager.persist(estudiante);
        entityManager.persist(asesoria);
    }

    @Test
    void testCrearReserva() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        assertNotNull(reserva);
        assertEquals(fecha, reserva.getFechaReserva());
        assertEquals(estudiante.getId(), reserva.getEstudiante().getId());
        assertEquals(asesoria.getId(), reserva.getAsesoria().getId());
    }


    @Test
    void testListarReservas() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        reservaService.crearReserva(dto);

        List<ReservaEntity> reservas = reservaService.listarReservas();
        assertFalse(reservas.isEmpty());
    }

    @Test
    void testActualizarReserva() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(2);
        EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);

        entityManager.persist(nuevoEstudiante);
        entityManager.persist(nuevaAsesoria);

        ReservaEntity actualizada = reservaService.updateReserva(reserva.getId(), nuevaFecha, nuevoEstudiante, nuevaAsesoria);

        assertNotNull(actualizada);
        assertEquals(nuevaFecha, actualizada.getFechaReserva());
        assertEquals(nuevoEstudiante.getId(), actualizada.getEstudiante().getId());
        assertEquals(nuevaAsesoria.getId(), actualizada.getAsesoria().getId());
    }

    @Test
    void testEliminarReserva() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        reservaService.eliminarReserva(reserva.getId());
        assertFalse(reservaRepository.existsById(reserva.getId()));
    }

    @Test
    void testGetReserva_Exito() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        ReservaEntity reservaEncontrada = reservaService.getReserva(reserva.getId());

        assertNotNull(reservaEncontrada);
        assertEquals(reserva.getId(), reservaEncontrada.getId());
        assertEquals(reserva.getFechaReserva(), reservaEncontrada.getFechaReserva());
    }

    @Test
    void testGetReserva_NoEncontrada() {
        assertThrows(EntityNotFoundException.class, () -> reservaService.getReserva(Long.MAX_VALUE));
    }

    @Test
    void testEliminarReserva_IdNulo() {
        assertThrows(EntityNotFoundException.class, () -> reservaService.eliminarReserva(null));
    }

    @Test
    void testEliminarReserva_NoExistente() {
        assertThrows(EntityNotFoundException.class, () -> reservaService.eliminarReserva(Long.MAX_VALUE));
    }

    @Test
    void testActualizarReserva_Fallo_FechaNula() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);

        entityManager.persist(nuevoEstudiante);
        entityManager.persist(nuevaAsesoria);

        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(reserva.getId(), null, nuevoEstudiante, nuevaAsesoria));
    }

    @Test
    void testActualizarReserva_Fallo_EstudianteNulo() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);
        entityManager.persist(nuevaAsesoria);

        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(reserva.getId(), LocalDateTime.now(), null, nuevaAsesoria));
    }

    @Test
    void testActualizarReserva_Fallo_AsesoriaNula() throws EntityNotFoundException {
        LocalDateTime fecha = LocalDateTime.now();

        ReservaDTO dto = new ReservaDTO();
        dto.setFechaReserva(fecha);
        dto.setEstudianteId(estudiante.getId());
        dto.setAsesoriaId(asesoria.getId());

        ReservaEntity reserva = reservaService.crearReserva(dto);

        EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        entityManager.persist(nuevoEstudiante);

        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(reserva.getId(), LocalDateTime.now(), nuevoEstudiante, null));
    }

    @Test
    void testActualizarReserva_NoExistente() {
        EstudianteEntity nuevoEstudiante = factory.manufacturePojo(EstudianteEntity.class);
        AsesoriaEntity nuevaAsesoria = factory.manufacturePojo(AsesoriaEntity.class);

        entityManager.persist(nuevoEstudiante);
        entityManager.persist(nuevaAsesoria);

        assertThrows(EntityNotFoundException.class, () -> reservaService.updateReserva(Long.MAX_VALUE, LocalDateTime.now(), nuevoEstudiante, nuevaAsesoria));
    }
}
