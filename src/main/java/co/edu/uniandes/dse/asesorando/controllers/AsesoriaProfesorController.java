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

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.uniandes.dse.asesorando.dto.AsesoriaDTO;
import co.edu.uniandes.dse.asesorando.dto.AsesoriaDetail;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.AsesoriaProfesorService;
import org.modelmapper.TypeToken;

/**
 * Clase que implementa el recurso "asesorias/profesor".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/asesorias/profesor")
public class AsesoriaProfesorController {

    @Autowired
    private AsesoriaProfesorService asesoriaProfesorService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obtiene todas las asesorías de un profesor específico.
     *
     * @param profesorId ID del profesor.
     * @return Lista de asesorías asociadas al profesor.
     * @throws EntityNotFoundException Si el profesor no existe.
     */
    @GetMapping("/{profesorId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AsesoriaDetail> listarAsesoriasDeProfesor(@PathVariable Long profesorId) throws EntityNotFoundException {
        List<AsesoriaEntity> asesorias =asesoriaProfesorService.listarAsesoriasDeProfesor(profesorId);
        return modelMapper.map(asesorias, new TypeToken<List<AsesoriaDetail>>() {}.getType());
        
    }
        
    

    /**
     * Crea una nueva asesoría y la asigna a un profesor.
     *
     * @param profesorId ID del profesor.
     * @param asesoria   Asesoría a crear.
     * @return La asesoría creada.
     * @throws EntityNotFoundException Si el profesor no existe.
     */
    @PostMapping("/{profesorId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AsesoriaDetail crearAsesoriaParaProfesor(@PathVariable Long profesorId, @Valid @RequestBody AsesoriaEntity asesoria)
            throws EntityNotFoundException {
        AsesoriaEntity asesorias = asesoriaProfesorService.crearAsesoriaParaProfesor(profesorId, asesoria.getId());
        return modelMapper.map(asesorias, AsesoriaDetail.class);
    }

    /**
     * Actualiza una asesoría de un profesor.
     *
     * @param profesorId ID del profesor.
     * @param asesoriaId ID de la asesoría a actualizar.
     * @param asesoria   Nueva información de la asesoría.
     * @return La asesoría actualizada.
     * @throws EntityNotFoundException Si el profesor o la asesoría no existen.
     */
    @PutMapping("/{profesorId}/{asesoriaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AsesoriaDetail actualizarAsesoriaDeProfesor(@PathVariable Long profesorId, @PathVariable Long asesoriaId,
            @Valid @RequestBody AsesoriaEntity asesoria) throws EntityNotFoundException {
        AsesoriaEntity asesorias = asesoriaProfesorService.actualizarAsesoriaDeProfesor(profesorId, asesoriaId, asesoria);
        return modelMapper.map(asesorias, AsesoriaDetail.class);
    }

    /**
    * Elimina una asesoría de un profesor.
     *
     * @param profesorId ID del profesor.
     * @param asesoriaId ID de la asesoría a eliminar.
     * @throws EntityNotFoundException Si el profesor o la asesoría no existen.
     */
    @DeleteMapping("/{profesorId}/{asesoriaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarAsesoriaDeProfesor(@PathVariable Long profesorId, @PathVariable Long asesoriaId)
            throws EntityNotFoundException {
        asesoriaProfesorService.eliminarAsesoriaDeProfesor(profesorId, asesoriaId);
    }
}
