package co.edu.uniandes.dse.asesorando.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.dto.ComentarioDTO;
import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.ReservaComentarioService;

@RestController
@RequestMapping("/reservas/{reservaId}/comentarios")
public class ReservaComentarioController {

    @Autowired
    private ReservaComentarioService reservaComentarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Crea un comentario asociado a una reserva.
     * @param reservaId ID de la reserva.
     * @param comentario Información del comentario a crear.
     * @return El comentario creado.
     * @throws EntityNotFoundException Si la reserva no existe.
     */
    @PutMapping("/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)  // Se usa 200 OK en actualizaciones
    public ComentarioDTO asociarComentarioAReserva(@PathVariable Long reservaId, @PathVariable Long comentarioId)
            throws EntityNotFoundException {
        ComentarioEntity comentario = reservaComentarioService.asociarComentarioAReserva(reservaId, comentarioId);
        return modelMapper.map(comentario, ComentarioDTO.class);
    }

    @PostMapping
        @ResponseStatus(code = HttpStatus.CREATED)
        public ComentarioDTO crearYAsociarComentario(
                @PathVariable Long reservaId,
                @RequestBody ComentarioDTO comentarioDTO)
                throws EntityNotFoundException, IllegalOperationException {

            ComentarioEntity comentarioEntity = modelMapper.map(comentarioDTO, ComentarioEntity.class);
            ComentarioEntity comentarioCreado = reservaComentarioService.crearYAsociarComentario(reservaId, comentarioEntity);
            return modelMapper.map(comentarioCreado, ComentarioDTO.class);
        }

    /**
     * Obtiene el comentario asociado a una reserva.
     * @param reservaId ID de la reserva.
     * @return Comentario asociado.
     * @throws EntityNotFoundException Si la reserva no existe o no tiene comentario.
     */
    @GetMapping("/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioEntity obtenerComentario(@PathVariable Long reservaId) throws EntityNotFoundException {
        return reservaComentarioService.obtenerComentarioPorReserva(reservaId);
    }

    /**
     * Elimina el comentario de una reserva.
     * @param reservaId ID de la reserva.
     * @throws EntityNotFoundException Si la reserva o el comentario no existen.
     */
    @DeleteMapping("/{comentarioId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarComentario(@PathVariable Long reservaId) throws EntityNotFoundException {
        reservaComentarioService.eliminarComentario(reservaId);
    }

    @PostMapping("/{comentarioId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ComentarioDTO crearComentarioEnReserva(@PathVariable Long reservaId, @PathVariable Long comentarioId)
            throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity asesorias = reservaComentarioService.crearComentarioEnReserva(reservaId, comentarioId);
        return modelMapper.map(asesorias, ComentarioDTO.class);
    }

}
