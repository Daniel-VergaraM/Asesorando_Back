package co.edu.uniandes.dse.asesorando.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.dto.AsesoriaDTO;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.ReservaAsesoriaService;

@RestController
@RequestMapping("/reservas/{reservaId}")
public class ReservaAsesoriaController {

    @Autowired
    private ReservaAsesoriaService reservaAsesoriaService;

    @Autowired
    private ModelMapper modelMapper;

    @PutMapping("/asesorias/{asesoriaId}")
    @ResponseStatus(code = HttpStatus.OK) 
    public AsesoriaDTO asociarAsesoriaAReserva(@PathVariable Long reservaId, @PathVariable Long asesoriaId)
            throws EntityNotFoundException {
        AsesoriaEntity calendario = reservaAsesoriaService.asociarAsesoriaAReserva(reservaId, asesoriaId);
        return modelMapper.map(calendario, AsesoriaDTO.class);
    }


    /**
     * Obtiene la asesoría asociada a una reserva.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public AsesoriaEntity obtenerAsesoriaPorReserva(@PathVariable Long reservaId) throws EntityNotFoundException {
        return reservaAsesoriaService.obtenerAsesoriaPorReserva(reservaId);
    }

    @PostMapping("/asesorias/{asesoriaId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AsesoriaDTO crearComentarioEnReserva(@PathVariable Long reservaId, @PathVariable Long asesoriaId)
            throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesorias = reservaAsesoriaService.crearAsesoriaEnReserva(reservaId, asesoriaId);
        return modelMapper.map(asesorias, AsesoriaDTO.class);
    }

    /**
     * Elimina la asesoría asociada a una reserva.
     */

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarAsesoria(@PathVariable Long reservaId) throws EntityNotFoundException {
        reservaAsesoriaService.eliminarAsesoria(reservaId);
    }
}
