package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;

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

import co.edu.uniandes.dse.asesorando.dto.CalendarioDTO;
import co.edu.uniandes.dse.asesorando.dto.CalendarioDetailDTO;
import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.CalendarioService;
import org.modelmapper.TypeToken;

public class CalendarioController {
@RestController
@RequestMapping("/calendars")
public class BookController {

    @Autowired
    private CalendarioService calendarioService;

    @Autowired
    private ModelMapper modelMapper;

@GetMapping
@ResponseStatus(code = HttpStatus.OK)
public List<CalendarioDetailDTO> findAll() {
        List<CalendarioEntity> calendars = calendarioService.getCalendarios();
        return modelMapper.map(calendars, new TypeToken<List<CalendarioDetailDTO>>() {
        }.getType());
    }

@GetMapping(value = "/{id}")
@ResponseStatus(code = HttpStatus.OK)
public CalendarioDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        CalendarioEntity calendarioEntity = calendarioService.getCalendario(id);
        return modelMapper.map(calendarioEntity, CalendarioDetailDTO.class);
      
}
@PostMapping
@ResponseStatus(code = HttpStatus.CREATED)
public CalendarioDTO create(@RequestBody CalendarioDTO CalendarioDTO) throws IllegalOperationException, EntityNotFoundException {
    CalendarioEntity calendarioEntity = calendarioService.createCalendario(modelMapper.map(CalendarioDTO, CalendarioEntity.class));
        return modelMapper.map(calendarioEntity, CalendarioDTO.class);
}
@PutMapping(value = "/{id}")
@ResponseStatus(code = HttpStatus.OK)
public CalendarioDTO update(@PathVariable Long id, @RequestBody CalendarioDTO CalendarioDTO)throws EntityNotFoundException, IllegalOperationException {
    CalendarioEntity calendarioEntity = calendarioService.updateCalendario( modelMapper.map(CalendarioDTO, CalendarioEntity.class));
        return modelMapper.map(calendarioEntity, CalendarioDTO.class);
}
@DeleteMapping(value = "/{id}")
@ResponseStatus(code = HttpStatus.NO_CONTENT)
public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        calendarioService.deleteCalendario(id);
}

}
}