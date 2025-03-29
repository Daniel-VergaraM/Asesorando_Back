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

import co.edu.uniandes.dse.asesorando.dto.EstudianteDTO;
import co.edu.uniandes.dse.asesorando.dto.EstudianteDetailDTO;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.EstudianteService;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EstudianteDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException{
        EstudianteEntity estudianteEntity = estudianteService.getEstudiante(id);
        return modelMapper.map(estudianteEntity, EstudianteDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EstudianteDTO create(@RequestBody EstudianteDTO estudianteDTO) throws IllegalOperationException, EntityNotFoundException{
        EstudianteEntity estudianteEntity = estudianteService.createEstudianteByObject(modelMapper.map(estudianteDTO, EstudianteEntity.class));
        return modelMapper.map(estudianteEntity, EstudianteDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EstudianteDTO update(@PathVariable Long id, @RequestBody EstudianteDTO estudianteDTO) throws EntityNotFoundException, IllegalOperationException{
        EstudianteEntity estudianteEntity = estudianteService.updateEstudianteById(id, modelMapper.map(estudianteDTO, EstudianteEntity.class));
        return modelMapper.map(estudianteEntity, EstudianteDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException{
        estudianteService.deleteEstudiante(id);
    }
}
