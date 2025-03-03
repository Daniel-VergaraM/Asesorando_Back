package co.edu.uniandes.dse.asesorando.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.asesorando.services.CalendarioService;

public class CalendarioController {
    @RestController
@RequestMapping("/calendars")
public class BookController {

        @Autowired
        private CalendarioService calendarioService;

        @Autowired
        private ModelMapper modelMapper;
}
}
