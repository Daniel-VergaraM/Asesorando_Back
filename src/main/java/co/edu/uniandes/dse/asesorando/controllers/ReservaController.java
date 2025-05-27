package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.dto.ReservaDTO;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.services.ReservaService;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservaDTO> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            // Llamar al servicio pasando el DTO completo (con IDs)
            ReservaEntity nuevaReserva = reservaService.crearReserva(reservaDTO);

            // Convertir Entity a DTO para la respuesta, usando modelMapper o manual
            ReservaDTO nuevaReservaDTO = modelMapper.map(nuevaReserva, ReservaDTO.class);

            // Si el modelMapper no mapea bien los IDs de las entidades relacionadas, setéalos manualmente:
            nuevaReservaDTO.setEstudianteId(nuevaReserva.getEstudiante().getId());
            nuevaReservaDTO.setAsesoriaId(nuevaReserva.getAsesoria().getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReservaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservaEntity getReserva(@PathVariable Long id) throws EntityNotFoundException {
        return reservaService.getReserva(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReserva(@PathVariable Long id) throws EntityNotFoundException {
        reservaService.eliminarReserva(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservaEntity> listarReservas() {
        return reservaService.listarReservas();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReservaDTO> updateReserva(@PathVariable Long id, 
                                                    @RequestBody ReservaDTO reservaDTO) {
        try {
            // Mapear el DTO a una entidad
            ReservaEntity reservaEntity = modelMapper.map(reservaDTO, ReservaEntity.class);
    
            // Llamar al servicio para actualizar la reserva
            ReservaEntity reservaActualizada = reservaService.updateReserva(id, 
                                                                            reservaEntity.getFechaReserva(), 
                                                                            reservaEntity.getEstudiante(), 
                                                                            reservaEntity.getAsesoria());
    
            // Mapear la entidad actualizada de vuelta a DTO
            ReservaDTO reservaActualizadaDTO = modelMapper.map(reservaActualizada, ReservaDTO.class);
    
            return ResponseEntity.ok(reservaActualizadaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/estado")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReservaDTO> marcarReservaComoCompletada(@PathVariable Long id) {
        try {
            ReservaEntity reservaActualizada = reservaService.marcarComoCompletada(id); 
            ReservaDTO reservaDTO = modelMapper.map(reservaActualizada, ReservaDTO.class);
            return ResponseEntity.ok(reservaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cancelada")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReservaDTO> marcarReservaComoCancelada(@PathVariable Long id) {
        try {
            ReservaEntity reservaActualizada = reservaService.marcarComoCancelada(id); 
            ReservaDTO reservaDTO = modelMapper.map(reservaActualizada, ReservaDTO.class);
            return ResponseEntity.ok(reservaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
    