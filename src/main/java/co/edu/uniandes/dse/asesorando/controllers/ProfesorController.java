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
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.dto.ProfesorDTO;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.ProfesorService;

/**
 * Clase que realiza las operaciones CRUD para los profesores
 *
 * @author Daniel-VergaraM
 */
@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private ModelMapper modelMapper;

    private static final List<String> tiposValidos = List.of("PROFESOR", "PROFESORVIRTUAL", "PROFESORPRESENCIAL");

    private static final List<String> filtrosValidos = List.of("nombre", "tematica", "tipo");

    /**
     * Retrieves a list of all ProfesorDTO objects.
     *
     * @return a list of ProfesorDTO objects.
     * @throws EntityNotFoundException if no ProfesorEntity objects are found.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProfesorDTO> findAll() throws EntityNotFoundException {
        List<ProfesorEntity> profesores = (List<ProfesorEntity>) profesorService.getProfesores();
        return modelMapper.map(profesores, new TypeToken<List<ProfesorDTO>>() {
        }.getType());
    }

    /**
     * Retrieves a professor by their ID.
     *
     * @param id The ID of the professor to retrieve.
     * @return A ProfesorDTO object representing the professor.
     * @throws EntityNotFoundException if no professor with the given ID is
     * found.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfesorDTO getProfesor(@PathVariable Long id) throws EntityNotFoundException {
        ProfesorEntity profesor = profesorService.getProfesor(id);
        return modelMapper.map(profesor, ProfesorDTO.class);
    }

    /**
     * Retrieves a list of ProfesorDTO based on the provided filters in the
     * request body. The filters can be "nombre", "tematica", "tipo", or a
     * combination of "tematica" and "tipo".
     *
     * @param json a map containing the filter criteria for retrieving
     * professors. Possible keys are "nombre", "tematica", and "tipo".
     * @return a list of ProfesorDTO that match the provided filter criteria.
     * @throws EntityNotFoundException if no professors are found with the
     * provided filters.
     * @throws IllegalOperationException if no valid filters are provided in the
     * request body.
     */
    @GetMapping(value = "/filtro")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfesorDTO> getProfesorBy(@RequestBody Map<String, Object> json) throws EntityNotFoundException, IllegalOperationException {

        if (json.keySet().stream().noneMatch(filtrosValidos::contains)) {
            throw new IllegalOperationException("No se proporcionaron filtros válidos.");
        }

        List<ProfesorEntity> profesores = null;

        if (json.containsKey("nombre") && json.get("nombre") != null) {
            String nombre = (String) json.get("nombre");
            ProfesorEntity profesor = profesorService.getProfesorPorNombre(nombre);
            profesores = List.of(profesor);
        } else if (json.containsKey("tematica") && json.get("tematica") != null) {
            String tematica = (String) json.get("tematica");
            profesores = (List<ProfesorEntity>) profesorService.getProfesorPorTematica(tematica);
        } else if (json.containsKey("tipo") && json.get("tipo") != null) {
            String tipo = (String) json.get("tipo");
            if (!tiposValidos.contains(tipo)) {
                throw new IllegalOperationException("El tipo de profesor proporcionado no es válido.");
            }
            profesores = profesorService.getProfesoresPorTipo(tipo);
        } else if (json.containsKey("tematica") && json.containsKey("tipo") && json.get("tematica") != null && json.get("tipo") != null) {
            String tematica = (String) json.get("tematica");
            String tipo = (String) json.get("tipo");
            if (!tiposValidos.contains(tipo)) {
                throw new IllegalOperationException("El tipo de profesor proporcionado no es válido.");
            }
            profesores = (List<ProfesorEntity>) profesorService.getProfesorPorTipoTematica(tematica, tipo);
        } else {
            throw new IllegalOperationException("No se encontraron profesores con los filtros proporcionados.");
        }

        return modelMapper.map(profesores, new TypeToken<List<ProfesorDTO>>() {
        }.getType());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesorDTO create(@RequestBody ProfesorDTO profesor) throws EntityNotFoundException {
        String tipo = profesor.getTipo();

        if (tipo == null) {
            throw new EntityNotFoundException("El tipo del profesor no puede ser nulo");
        } else if (!tiposValidos.contains(tipo)) {
            throw new EntityNotFoundException("El tipo de profesor no es valido.");
        }

        ProfesorEntity profesorEntity = modelMapper.map(profesor, ProfesorEntity.class);
        ProfesorEntity nuevoProfesor = profesorService.createProfesor(profesorEntity, tipo);
        return modelMapper.map(nuevoProfesor, ProfesorDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfesorDTO update(@PathVariable Long id, @RequestBody ProfesorDTO profesor) throws EntityNotFoundException {
        ProfesorEntity profesorEntity = modelMapper.map(profesor, ProfesorEntity.class);
        ProfesorEntity nuevoProfesor = profesorService.updateProfesor(id, profesorEntity);
        return modelMapper.map(nuevoProfesor, ProfesorDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfesor(@PathVariable Long id) throws EntityNotFoundException {
        profesorService.deleteProfesor(id);
    }

}
