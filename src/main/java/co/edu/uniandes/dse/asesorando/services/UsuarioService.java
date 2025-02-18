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

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorPresencialEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorVirtualEntity;
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

    /**
     * Crea un usuario por medio de un objeto usuario
     */
    @Transactional
    public UsuarioEntity createUsuario(@Valid @NotNull UsuarioEntity usuario) throws IllegalArgumentException {
        log.info("Creando un usuario nuevo");
        Optional<UsuarioEntity> usuarioExistente = usuarioRepository.findByCorreo(usuario.getCorreo());

        if (usuarioExistente.isPresent()) {
            log.error("El usuario con correo {} ya existe", usuario.getCorreo());
            throw new IllegalArgumentException("El usuario con correo " + usuario.getCorreo() + " ya existe");
        }

        log.info("Usuario creado");
        return usuarioRepository.save(usuario);
    }

    /**
     * Obtiene todos los usuarios
     *
     * @return
     */
    @Transactional
    public List<UsuarioEntity> obtenerUsuarios() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su correo
     *
     * @param correo
     * @return
     */
    @Transactional
    public UsuarioEntity getUsuario(@NotNull Long id) throws IllegalArgumentException {
        log.info("Obteniendo un profesor");

        Optional<UsuarioEntity> usuarioExistente = usuarioRepository.findById(id);

        if (usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        log.info("Profesor obtenido");
        return usuarioExistente.get();
    }

    /**
     * Actualiza un usuario por medio de un objeto usuario
     *
     * @param id
     * @param usuario
     * @return
     */
    @Transactional
    public UsuarioEntity updateUsuario(@NotNull Long id, @Valid @NotNull UsuarioEntity usuario) {
        log.info("Actualizando un usuario");

        UsuarioEntity usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        usuarioExistente.setContrasena(usuario.getContrasena());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setTipo(usuario.getTipo());

        if (usuario instanceof ProfesorEntity && usuarioExistente instanceof ProfesorEntity) {
            ProfesorEntity profesor = (ProfesorEntity) usuario;
            ProfesorEntity profesorExistente = (ProfesorEntity) usuarioExistente;
            profesorExistente.setTematicas(profesor.getTematicas());
            profesorExistente.setFormacion(profesor.getFormacion());
            profesorExistente.setExperiencia(profesor.getExperiencia());
            profesorExistente.setPrecioHora(profesor.getPrecioHora());
            profesorExistente.setFotoUrl(profesor.getFotoUrl());

            if (profesor instanceof ProfesorVirtualEntity && profesorExistente instanceof ProfesorVirtualEntity) {
                ((ProfesorVirtualEntity) profesorExistente)
                        .setEnlaceReunion(((ProfesorVirtualEntity) profesor).getEnlaceReunion());
            }

            if (profesor instanceof ProfesorPresencialEntity && profesorExistente instanceof ProfesorPresencialEntity) {
                ((ProfesorPresencialEntity) profesorExistente)
                        .setCodigoPostal(((ProfesorPresencialEntity) profesor).getCodigoPostal());
                ((ProfesorPresencialEntity) profesorExistente)
                        .setLatitud(((ProfesorPresencialEntity) profesor).getLatitud());
                ((ProfesorPresencialEntity) profesorExistente)
                        .setLongitud(((ProfesorPresencialEntity) profesor).getLongitud());
            }
        } else if (usuario instanceof EstudianteEntity && usuarioExistente instanceof EstudianteEntity) {
            ((EstudianteEntity) usuario).setAsesorias(((EstudianteEntity) usuarioExistente).getAsesorias());
            ((EstudianteEntity) usuario).setReservas(((EstudianteEntity) usuarioExistente).getReservas());
        }

        log.info("Usuario actualizado");
        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Elimina un usuario por su id
     *
     * @param id
     * @return
     */
    @Transactional
    public void deleteUsuario(@NotNull Long id) {
        log.info("Eliminando un usuario");

        UsuarioEntity usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));

        log.info("Usuario eliminado");
        usuarioRepository.deleteById(usuarioExistente.getId());
    }

}
