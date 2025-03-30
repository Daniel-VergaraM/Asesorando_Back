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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.dto.AsesoriaDTO;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.AsesoriaService;

/**
 * Clase que implementa el recurso "asesorias".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/asesorias")
public class AsesoriaController {

    @Autowired
    private AsesoriaService asesoriaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todas las asesorías registradas en la aplicación.
     *
     * @return JSONArray {@link AsesoriaEntity} - Lista de asesorías encontradas. 
     *         Si no hay ninguna, retorna una lista vacía.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AsesoriaDTO> findAll() {
        List<AsesoriaEntity> asesorias = asesoriaService.getAllAsesorias();
        return modelMapper.map(asesorias, new TypeToken<List<AsesoriaDTO>>() {}.getType());
    }



    /**
     * Busca la asesoría con el ID recibido en la URL y la devuelve.
     *
     * @param id Identificador de la asesoría que se está buscando.
     * @return JSON {@link AsesoriaEntity} - La asesoría encontrada.
     */
    @GetMapping(value = "/{id:[0-9]+}") // Solo acepta números como ID
    @ResponseStatus(code = HttpStatus.OK)
    public AsesoriaDTO findOne(@PathVariable Long id) throws IllegalOperationException {
        AsesoriaEntity asesorias =  asesoriaService.getAsesoriaEntity(id);
            return modelMapper.map(asesorias, AsesoriaDTO.class);
        }
    

   
    /**
     * Busca las asesorías por área de conocimiento.
     *
     * @param area Área de conocimiento de la asesoría.
     * @return Lista de asesorías en esa área.
     */
    @GetMapping(value = "/area/{area}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AsesoriaDTO> findByArea(@PathVariable String area) throws IllegalOperationException {
        List<AsesoriaEntity> asesorias = asesoriaService.getAsesoriasByArea(area);
        return modelMapper.map(asesorias, new TypeToken<List<AsesoriaDTO>>() {}.getType());
    }

        /**
     * Busca asesorías filtradas por su estado de completada y profesorId.
     *
     * @param estado Estado de completada (true o false).
     * @param profesorId Identificador del profesor.
     * @return Lista de asesorías según el estado proporcionado.
     */
    @GetMapping(value = "/completada")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AsesoriaDTO> findByCompletada(@RequestParam(required = false) Boolean estado, @RequestParam long profesorId) throws IllegalOperationException {
        if (estado == null) {
            throw new IllegalArgumentException("El parámetro 'estado' es obligatorio.");
        }
    
        List<AsesoriaEntity> asesorias = asesoriaService.getAsesoriasByCompletada(estado, profesorId);
    
        return modelMapper.map(Optional.ofNullable(asesorias).orElse(Collections.emptyList()), new TypeToken<List<AsesoriaDTO>>() {}.getType());
    }
    
    
    /**
     * Crea una nueva asesoría con la información recibida en el cuerpo de la petición.
     *
     * @param asesoria {@link AsesoriaEntity} - La asesoría que se desea guardar.
     * @return JSON {@link AsesoriaEntity} - La asesoría guardada con el ID generado.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AsesoriaDTO create(@RequestBody AsesoriaDTO asesoria) throws IllegalOperationException {
        AsesoriaEntity convertirDTO = modelMapper.map(asesoria, AsesoriaEntity.class);
        AsesoriaEntity createdAsesoria = asesoriaService.createAsesoria(convertirDTO, asesoria.getProfesorId());
        return modelMapper.map(createdAsesoria, AsesoriaDTO.class);
    }

    /**
     * Actualiza la asesoría con el ID recibido en la URL con la información proporcionada.
     *
     * @param id       Identificador de la asesoría a actualizar.
     * @param asesoria {@link AsesoriaEntity} - La asesoría con los datos actualizados.
     * @return La asesoría actualizada.
     */
    @PutMapping(value = "/{id:[0-9]+}")
    @ResponseStatus(code = HttpStatus.OK)
    public AsesoriaDTO update(@PathVariable Long id, @RequestBody AsesoriaDTO asesoria)
            throws EntityNotFoundException, IllegalOperationException {
        AsesoriaEntity asesorias = asesoriaService.updateAsesoriaEntity(id, modelMapper.map(asesoria, AsesoriaEntity.class));
        return modelMapper.map(asesorias, AsesoriaDTO.class);
    }

    /**
     * Borra la asesoría con el ID recibido en la URL.
     *
     * @param id Identificador de la asesoría que se desea eliminar.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws IllegalOperationException {
        asesoriaService.deleteAsesoriaEntity(id);
        
    
}}