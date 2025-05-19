package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;

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

import co.edu.uniandes.dse.asesorando.dto.CalendarioDTO;
import co.edu.uniandes.dse.asesorando.dto.CalendarioDetailDTO;
import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.asesorando.services.CalendarioService;

@RestController
@RequestMapping("/calendars")
public class CalendarioController {

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
    public CalendarioDTO create(@RequestBody CalendarioDTO calendarioDTO) throws IllegalOperationException {
        CalendarioEntity calendarioEntity = calendarioService
                .createCalendario(modelMapper.map(calendarioDTO, CalendarioEntity.class));
        return modelMapper.map(calendarioEntity, CalendarioDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CalendarioDTO update(@PathVariable Long id, @RequestBody CalendarioDTO calendarioDTO)
            throws IllegalOperationException {
        CalendarioEntity calendarioEntity = calendarioService
                .updateCalendario(modelMapper.map(calendarioDTO, CalendarioEntity.class));
        return modelMapper.map(calendarioEntity, CalendarioDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        calendarioService.deleteCalendario(id);
    }

    @PostMapping(value = "/fechaInicio")
    @ResponseStatus(code = HttpStatus.OK)
    public CalendarioDetailDTO findByFechaInicio(@RequestBody CalendarioDTO calendarioDTO)
            throws IllegalOperationException {
        CalendarioEntity calendarioEntity = calendarioService
                .getCalendarioByFechaInicio(modelMapper.map(calendarioDTO, CalendarioEntity.class).getFechaInicio());
        return modelMapper.map(calendarioEntity, CalendarioDetailDTO.class);
    }

    @PostMapping(value = "/fechaFin")
    @ResponseStatus(code = HttpStatus.OK)
    public CalendarioDetailDTO findByFechaFin(@RequestBody CalendarioDTO calendarioDTO)
            throws IllegalOperationException {
        CalendarioEntity calendarioEntity = calendarioService
                .getCalendarioByFechaFin(modelMapper.map(calendarioDTO, CalendarioEntity.class).getFechaFin());
        return modelMapper.map(calendarioEntity, CalendarioDetailDTO.class);
    }

    @GetMapping(value = "/fechaInicio/menor/{fechaInicio}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CalendarioDetailDTO> findByFechaInicioLessThan(@RequestBody CalendarioDTO calendarioDTO)
            throws EntityNotFoundException {
        List<CalendarioEntity> calendars = calendarioService.getCalendarioByFechaInicioLessThan(
                modelMapper.map(calendarioDTO, CalendarioEntity.class).getFechaInicio());
        return modelMapper.map(calendars, new TypeToken<List<CalendarioDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/fechaInicio/entre/{fechaInicio}/{fechaFin}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CalendarioDetailDTO> findByFechaInicioBetween(@RequestBody CalendarioDTO startDateDTO,
            @RequestBody CalendarioDTO calendarioDTO) throws EntityNotFoundException {
        List<CalendarioEntity> calendars = calendarioService.getCalendarioByFechaInicioBetween(
                modelMapper.map(calendarioDTO, CalendarioEntity.class).getFechaInicio(),
                modelMapper.map(calendarioDTO, CalendarioEntity.class).getFechaFin());
        return modelMapper.map(calendars, new TypeToken<List<CalendarioDetailDTO>>() {
        }.getType());
    }
}
