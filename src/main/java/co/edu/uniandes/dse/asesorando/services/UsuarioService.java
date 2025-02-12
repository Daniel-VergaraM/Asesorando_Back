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
package co.edu.uniandes.dse.asesorando.services;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;
import co.edu.uniandes.dse.asesorando.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con el repositorio de Usuario
 *
 * @author Daniel-VergaraM
 */
@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;



    @Transactional
    ProfesorEntity registrarProfesor(@Valid @NotNull ProfesorEntity profesor) {
        log.info("Registrando un profesor nuevo");

        Optional<UsuarioEntity> profesorExistente = usuarioRepository.findById(profesor.getId());

        if (profesorExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor ya esta registrado.");
        }

        log.info("Profesor creado");
        return usuarioRepository.save(profesor);
    }

}
