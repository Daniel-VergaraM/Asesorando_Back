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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.TematicaEntity;
import co.edu.uniandes.dse.asesorando.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.asesorando.repositories.TematicaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con el repositorio de Tematica
 *
 * @author Daniel-VergaraM
 */
@Slf4j
@Service
public class TematicaService {

    @Autowired
    private TematicaRepository tematicaRepository;

    /**
     * Crea una tematica por medio de un area y un tema
     *
     * @param area
     * @param tema
     * @return TematicaEntity
     */
    @Transactional
    public TematicaEntity createTematica(@NotNull String area, @NotNull String tema) throws EntityNotFoundException {
        log.info("Creando una tematica nueva");

        Optional<TematicaEntity> tematicaExistente = tematicaRepository.findByTemaAndArea(tema, area);

        if (tematicaExistente.isPresent()) {
            log.error("Ya existe una tematica con ese tema y area");
            throw new EntityNotFoundException("Ya existe una tematica con ese tema y area");
        }

        TematicaEntity tematica = new TematicaEntity();
        tematica.setArea(area);
        tematica.setTema(tema);
        tematica.setProfesores(new ArrayList<>());

        return tematicaRepository.save(tematica);
    }

    /**
     * Crea una tematica por medio de un TematicaEntity
     *
     * @param tematica
     * @return TematicaEntity
     */
    @Transactional
    public TematicaEntity createTematica(@Valid @NotNull TematicaEntity tematica) throws EntityNotFoundException {
        log.info("Creando una tematica nueva");
        Optional<TematicaEntity> tematicaExistente = tematicaRepository.findByTema(tematica.getTema());

        if (tematicaExistente.isPresent()) {
            log.error("Ya existe un resultado con ese tema");
            throw new EntityNotFoundException("Ya existe una tematica con ese tema");
        }

        log.info("Guardando la tematica");

        return tematicaRepository.save(tematica);
    }

    /**
     * Obtiene todas las tematicas
     *
     * @return
     */
    @Transactional
    public List<TematicaEntity> getTematicas() {
        log.info("Obteniendo todas las tematicas");
        return tematicaRepository.findAll();
    }

    /**
     * Obtiene una tematica por su id
     *
     * @param tematicaId
     * @return
     */
    @Transactional
    public TematicaEntity getTematica(@NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Obteniendo la tematica con id: {}", tematicaId);
        Optional<TematicaEntity> tematica = tematicaRepository.findById(tematicaId);

        if (tematica.isEmpty()) {
            throw new EntityNotFoundException("La tematica no existe");
        }

        return tematica.get();
    }

    /**
     * Actualiza una tematica por medio de su id y un TematicaEntity
     *
     * @param tematicaId
     * @param tematica
     * @return
     */
    @Transactional
    public TematicaEntity updateTematica(@NotNull Long tematicaId, @NotNull TematicaEntity tematica) throws EntityNotFoundException {
        log.info("Actualizando la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId).orElseThrow(() -> new EntityNotFoundException("La tematica no existe"));

        tematicaExistente.setArea(tematica.getArea());
        tematicaExistente.setTema(tematica.getTema());
        tematicaExistente.setProfesores(tematica.getProfesores());

        log.info("La tematica con id {} ha sido actualizada", tematicaId);
        return tematicaRepository.save(tematicaExistente);

    }

    /**
     * Elimina una tematica por su id
     *
     * @param tematicaId
     */
    @Transactional
    public void deleteTematica(@NotNull Long tematicaId) throws EntityNotFoundException {
        log.info("Eliminando la tematica con id: {}", tematicaId);
        TematicaEntity tematicaExistente = tematicaRepository.findById(tematicaId)
                .orElseThrow(() -> new EntityNotFoundException("La tematica no existe"));

        log.info("La tematica con id {} ha sido eliminada", tematicaId);
        tematicaRepository.delete(tematicaExistente);
    }
}
