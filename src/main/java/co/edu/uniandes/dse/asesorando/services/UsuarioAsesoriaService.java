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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.AsesoriaRepository;
import co.edu.uniandes.dse.asesorando.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la relación entre Usuario y Asesoría
 *
 * @author Daniel-VergaraM
 */
@Slf4j
@Service
public class UsuarioAsesoriaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AsesoriaRepository asesoriaRepository;

    /**
     * Agrega una asesoría completada a un usuario
     *
     * @param usuarioId El id del usuario al que se le va a agregar la asesoría
     * @param asesoriaId El id de la asesoría que se va a agregar
     * @return El usuario con la asesoría agregada
     */
    @Transactional
    public UsuarioEntity addAsesoria(Long usuarioId, Long asesoriaId) throws EntityNotFoundException, IllegalArgumentException {
        log.info("Inicia proceso de agregar una asesoría al usuario con id = {0}", usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con id = " + usuarioId));
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new EntityNotFoundException("No se encontró la asesoría con id = " + asesoriaId));
        if (usuario.getAsesoriasCompletadas().contains(asesoria)) {
            throw new IllegalArgumentException("La asesoría con id = " + asesoriaId + " ya está en la lista de asesorías completadas del usuario con id = " + usuarioId);
        }
        usuario.getAsesoriasCompletadas().add(asesoria);
        usuarioRepository.save(usuario);
        log.info("Termina proceso de agregar una asesoría al usuario con id = {0}", usuarioId);
        return usuario;

    }

    /**
     * Elimina una asesoría completada de un usuario
     *
     * @param usuarioId El id del usuario al que se le va a eliminar la asesoría
     * @param asesoriaId El id de la asesoría que se va a eliminar
     * @return El usuario con la asesoría eliminada
     */
    @Transactional
    public UsuarioEntity removeAsesoria(Long usuarioId, Long asesoriaId) throws EntityNotFoundException, IllegalArgumentException {
        log.info("Inicia proceso de eliminar una asesoría al usuario con id = {0}", usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con id = " + usuarioId));
        AsesoriaEntity asesoria = asesoriaRepository.findById(asesoriaId).orElseThrow(() -> new EntityNotFoundException("No se encontró la asesoría con id = " + asesoriaId));
        if (!usuario.getAsesoriasCompletadas().contains(asesoria)) {
            throw new IllegalArgumentException("La asesoría con id = " + asesoriaId + " no está en la lista de asesorías completadas del usuario con id = " + usuarioId);
        }
        usuario.getAsesoriasCompletadas().remove(asesoria);
        usuarioRepository.save(usuario);
        log.info("Termina proceso de eliminar una asesoría al usuario con id = {0}", usuarioId);
        return usuario;
    }

    /**
     * Obtiene una lista con todas las asesorías completadas de un usuario
     * @param usuarioId El id del usuario
     * @return La lista con las asesorías completadas
     * @throws EntityNotFoundException Si no se encuentra el usuario
     */
    @Transactional
    public Iterable<AsesoriaEntity> getAsesoriasCompletadas(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de obtener las asesorías completadas del usuario con id = {0}", usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con id = " + usuarioId));
        log.info("Termina proceso de obtener las asesorías completadas del usuario con id = {0}", usuarioId);
        return usuario.getAsesoriasCompletadas();
    }

}
