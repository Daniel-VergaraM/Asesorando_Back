package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.CalendarioRepository;
import co.edu.uniandes.dse.asesorando.repositories.ProfesorRepository;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfesorCalendarioService {

@Autowired
	private ProfesorRepository profesorRepository;

	@Autowired
	private CalendarioRepository calendarioRepository;

    
    
}
