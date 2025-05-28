package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.asesorando.dto.TematicaDTO;
import co.edu.uniandes.dse.asesorando.dto.TematicaDetailDTO;
import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.services.ProfesorTematicaService;

@RestController
@RequestMapping("/profesores")
public class ProfesorTematicaController {

    @Autowired
    private ProfesorTematicaService profesorTematicaService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{profesorId}/tematicas/{tematicaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TematicaDetailDTO addTematica(@PathVariable Long profesorId, @PathVariable Long tematicaId)
            throws EntityNotFoundException {
        TematicaEntity tematicaEntity = profesorTematicaService.agregarTematicaAProfesor(profesorId, tematicaId);
        return modelMapper.map(tematicaEntity, TematicaDetailDTO.class);
    }

    @GetMapping(value = "/{profesorId}/tematicas/{tematicaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TematicaDetailDTO getTematica(@PathVariable Long profesorId, @PathVariable Long tematicaId)
            throws EntityNotFoundException {
        TematicaEntity tematicaEntity = profesorTematicaService.getTematicaDeProfesor(profesorId, tematicaId);
        return modelMapper.map(tematicaEntity, TematicaDetailDTO.class);
    }

    

    @PutMapping(value = "/{profesorId}/tematicas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TematicaDetailDTO> updateTematicas(@PathVariable Long profesorId, @RequestBody List<TematicaDTO> tematicas)
            throws EntityNotFoundException {
        List<TematicaEntity> entities = modelMapper.map(tematicas, new TypeToken<List<TematicaEntity>>() {}.getType());
        List<TematicaEntity> tematicasList = profesorTematicaService.actualizarProfesorTematicas(profesorId, entities);
        return modelMapper.map(tematicasList, new TypeToken<List<TematicaDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{profesorId}/tematicas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TematicaDetailDTO> getTematicas(@PathVariable Long profesorId) throws EntityNotFoundException {
        List<TematicaEntity> tematicas = profesorTematicaService.getTematicasDeProfesor(profesorId);
        return modelMapper.map(tematicas, new TypeToken<List<TematicaDetailDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{profesorId}/tematicas/{tematicaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeTematica(@PathVariable Long profesorId, @PathVariable Long tematicaId)
            throws EntityNotFoundException {
        profesorTematicaService.eliminarTematicaDeProfesor(profesorId, tematicaId);
    }

    
}
