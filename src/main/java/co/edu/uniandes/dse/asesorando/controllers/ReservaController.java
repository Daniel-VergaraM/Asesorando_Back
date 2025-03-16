package co.edu.uniandes.dse.asesorando.controllers;

import co.edu.uniandes.dse.asesorando.dto.ReservaDTO;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import java.util.List;

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
            // Convertir DTO a Entity
            ReservaEntity reservaEntity = modelMapper.map(reservaDTO, ReservaEntity.class);

            // Llamar al servicio para crear la reserva
            ReservaEntity nuevaReserva = reservaService.crearReserva(
                reservaEntity.getFechaReserva(),
                reservaEntity.getEstudiante(),
                reservaEntity.getAsesoria()
            );

            // Convertir Entity a DTO y devolver la respuesta
            ReservaDTO nuevaReservaDTO = modelMapper.map(nuevaReserva, ReservaDTO.class);
            return ResponseEntity.ok(nuevaReservaDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservaEntity getReserva(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        return reservaService.getReserva(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReserva(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
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
}
    