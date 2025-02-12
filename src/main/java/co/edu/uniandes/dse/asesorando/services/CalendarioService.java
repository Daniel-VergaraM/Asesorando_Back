package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import lombok.extern.slf4j.Slf4j;

public class CalendarioService {
    
    @Autowired
    private CalendarioRepository calendarioRepository;


}
