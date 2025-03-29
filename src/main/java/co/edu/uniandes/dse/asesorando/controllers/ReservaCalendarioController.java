package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.asesorando.dto.ReservaDTO;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.ReservaCalendarioService;


@RestController
@RequestMapping("/calendars/{calendarioId}/reservas")
public class ReservaCalendarioController {

    @Autowired
    private ReservaCalendarioService reservaCalendarioService;


    @Autowired
    private ModelMapper modelMapper;
    

    @PutMapping("/{reservaId}")
    @ResponseStatus(code = HttpStatus.OK)  // Se usa 200 OK en actualizaciones
    public ReservaDTO asociarReservaACalendario(@PathVariable Long reservaId, @PathVariable Long calendarioId)
            throws EntityNotFoundException {
        ReservaEntity calendario = reservaCalendarioService.asociarReservaACalendario(reservaId, calendarioId);
        return modelMapper.map(calendario, ReservaDTO.class);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ReservaEntity> obtenerReservasPorCalendario(@PathVariable Long calendarioId) throws EntityNotFoundException {
        return reservaCalendarioService.obtenerReservasPorCalendario(calendarioId);
    }

    @PostMapping("/{reservaId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ReservaDTO crearReservaEnCalendario(@PathVariable Long calendarioId, @PathVariable Long reservaId)
            throws EntityNotFoundException, IllegalOperationException {
        ReservaEntity asesorias = reservaCalendarioService.crearReservaEnCalendario(calendarioId, reservaId);
        return modelMapper.map(asesorias, ReservaDTO.class);
    }

    // Eliminar una reserva de un calendario
    @DeleteMapping("/{reservaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void eliminarReserva(@PathVariable Long calendarioId, 
                                @PathVariable Long reservaId) throws EntityNotFoundException {
        reservaCalendarioService.eliminarReservaDeCalendario(calendarioId, reservaId);
    }
}
