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
package co.edu.uniandes.dse.asesorando.controllers;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.server.ResponseStatusException;

import co.edu.uniandes.dse.asesorando.dto.UsuarioDTO;
import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.services.UsuarioService;

/**
 * Clase que realiza las operaciones CRUD para los usuarios
 *
 * @author Daniel-VergaraM
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    private static final List<String> tiposValidos = List.of("PROFESOR", "PROFESORVIRTUAL", "PROFESORPRESENCIAL", "ESTUDIANTE");

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UsuarioDTO> findAll() {
        List<UsuarioEntity> usuarios = usuarioService.obtenerUsuarios();
        return modelMapper.map(usuarios, new TypeToken<List<UsuarioDTO>>() {
        }.getType());
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of UsuarioDTO objects representing all users.
     */
    @GetMapping(value = "/tipo/{tipoUsuario}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UsuarioDTO> findByType(@PathVariable String tipoUsuario) throws EntityNotFoundException {
        if (!tiposValidos.contains(tipoUsuario)) {
            throw new EntityNotFoundException("Tipo de usuario no válido: " + tipoUsuario);
        }
        List<UsuarioEntity> usuarios = usuarioService.obtenerUsuariosPorTipo(tipoUsuario);
        return modelMapper.map(usuarios, new TypeToken<List<UsuarioDTO>>() {
        }.getType());

    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user data transfer object (DTO) corresponding to the
     * specified ID.
     * @throws EntityNotFoundException If no user with the specified ID is
     * found.
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UsuarioDTO findById(@PathVariable Long id) throws EntityNotFoundException {
        UsuarioEntity usuario = usuarioService.getUsuario(id);
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param correo the email address of the user to be retrieved.
     * @return the user details as a UsuarioDTO.
     * @throws EntityNotFoundException if no user is found with the given email
     * address.
     */
    @GetMapping(value = "/")
    @ResponseStatus(code = HttpStatus.OK)
    public UsuarioDTO findByCorreo(@RequestBody Map<String, Object> json) throws EntityNotFoundException {
        String key = "correo";
        if (json.containsKey(key) && json.get(key) != null) {
            String correo = (String) json.get(key);
            UsuarioEntity usuario = usuarioService.getUsuarioByCorreo(correo);
            return modelMapper.map(usuario, UsuarioDTO.class);
        } else {
            throw new EntityNotFoundException("El usuario con correo " + json.get(key) + " no existe.");
        }
    }

    /**
     * Creates a new user in the system.
     *
     * @param usuario The user data transfer object containing the details of
     * the user to be created.
     * @return The created user data transfer object.
     * @throws EntityNotFoundException If the user entity cannot be found.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UsuarioDTO create(@RequestBody UsuarioDTO usuario) throws EntityNotFoundException {
        UsuarioEntity usuarioEntity = modelMapper.map(usuario, UsuarioEntity.class);
        UsuarioEntity nuevoUsuario = usuarioService.createUsuario(usuarioEntity);
        return modelMapper.map(nuevoUsuario, UsuarioDTO.class);
    }

    /**
     * Updates an existing user with the given ID.
     *
     * @param id The ID of the user to be updated.
     * @param usuario The user data to update.
     * @return The updated user data.
     * @throws EntityNotFoundException If the user with the given ID is not
     * found.
     */
    @PutMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
public UsuarioDTO update(
    @PathVariable Long id,
    @RequestBody UsuarioDTO usuarioDto
) throws EntityNotFoundException {
     UsuarioEntity existente = usuarioService.getUsuario(id);

    modelMapper.map(usuarioDto, existente);

    UsuarioEntity actualizado = usuarioService.updateUsuario(id, existente);

    // 4) Devolver DTO para el cliente
    return modelMapper.map(actualizado, UsuarioDTO.class);
}

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted.
     * @throws EntityNotFoundException if no user is found with the given ID.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUsuario(@PathVariable Long id) throws EntityNotFoundException {
        usuarioService.deleteUsuario(id);
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioDTO login(@RequestBody UsuarioDTO usuarioReq) {
    try {
        UsuarioEntity user = usuarioService.getUsuarioByCorreo(usuarioReq.getCorreo());
        if (!user.getContrasena().equals(usuarioReq.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña inválida");
        }
        return modelMapper.map(user, UsuarioDTO.class);
    } catch (EntityNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
    }
    }  
}
