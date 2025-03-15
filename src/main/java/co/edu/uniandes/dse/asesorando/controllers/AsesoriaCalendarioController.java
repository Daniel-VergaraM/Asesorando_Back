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

package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.asesorando.dto.AsesoriaDTO;
import co.edu.uniandes.dse.asesorando.dto.AsesoriaDetail;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.AsesoriaCalendarioService;

/**
 * Clase que implementa el recurso "asesorias/calendario".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/asesorias")
public class AsesoriaCalendarioController {

    @Autowired
    private AsesoriaCalendarioService asesoriaCalendarioService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las asesorías asociadas a un calendario específico.
     *
     * @param calendarioId ID del calendario.
     * @return Lista de asesorías asociadas al calendario.
     * @throws EntityNotFoundException Si el calendario no existe.
     */
    @GetMapping("/calendario/{calendarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AsesoriaDetail> listarAsesoriasDeCalendario(@PathVariable Long calendarioId) throws EntityNotFoundException {
        List<AsesoriaEntity> asesorias = asesoriaCalendarioService.getAsesoriasByCalendarioId(calendarioId);
        return modelMapper.map(asesorias, new TypeToken<List<AsesoriaDetail>>() {}.getType());
    }
    

    /**
     * Crea una nueva asesoría dentro de un calendario.
     *
     * @param calendarioId ID del calendario.
     * @param asesoria     Asesoría a agregar.
     * @return La asesoría creada.
     * @throws EntityNotFoundException   Si el calendario no existe.
     * @throws IllegalOperationException Si la asesoría ya está asignada a otro calendario.
     */
    @PostMapping("/{asesoriaId}/calendario/{calendarioId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AsesoriaDetail crearAsesoriaEnCalendario(@PathVariable Long calendarioId, @PathVariable Long asesoriaId)
            throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesorias = asesoriaCalendarioService.crearAsesoriaEnCalendario(calendarioId, asesoriaId);
        return modelMapper.map(asesorias, AsesoriaDetail.class);
    }

   /**
     * Actualiza una asesoría dentro de un calendario.
     *
     * @param calendarioId ID del calendario.
     * @param asesoriaId   ID de la asesoría a actualizar.
     * @param asesoria     Nueva información de la asesoría.
     * @return La asesoría actualizada.
     * @throws EntityNotFoundException   Si el calendario o la asesoría no existen.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */
    @PutMapping("/calendario/{calendarioId}/asesorias/{asesoriaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AsesoriaDetail actualizarAsesoriaEnCalendario(@PathVariable Long calendarioId, @PathVariable Long asesoriaId,
            @RequestBody AsesoriaEntity asesoria) throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesorias = asesoriaCalendarioService.updateAsesoriaInCalendario(calendarioId, asesoriaId,asesoria);
        return modelMapper.map(asesorias, AsesoriaDetail.class);
    }

    /**
     * Elimina una asesoría de un calendario.
     *
     * @param calendarioId ID del calendario.
     * @param asesoriaId   ID de la asesoría a eliminar.
     * @throws EntityNotFoundException   Si el calendario o la asesoría no existen.
     * @throws IllegalOperationException Si la asesoría no pertenece al calendario especificado.
     */
    @DeleteMapping("/calendario/{calendarioId}/asesorias/{asesoriaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarAsesoriaDeCalendario(@PathVariable Long calendarioId, @PathVariable Long asesoriaId)
        throws EntityNotFoundException, IllegalOperationException {
    asesoriaCalendarioService.deleteAsesoriaFromCalendario(calendarioId, asesoriaId);
}

}
