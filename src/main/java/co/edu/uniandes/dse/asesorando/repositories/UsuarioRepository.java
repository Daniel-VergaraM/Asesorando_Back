package co.edu.uniandes.dse.asesorando.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;

/**
 * Interfaz que define las operaciones sobre la tabla de usuarios
 *
 * @author Daniel-VergaraM
 */
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Obtiene un usuario por su correo
     *
     * @param correo
     * @return
     *
     */
    <T extends UsuarioEntity> Optional<T> findByCorreo(String correo);

    /**
     * Obtiene todos los usuarios de un tipo en particular
     *
     * @param tipo
     * @return
     *
     */
    <T extends UsuarioEntity> List<T> findByTipo(String tipo);

    /**
     * Obtiene un usuario por su nombre
     *
     * @param nombre
     * @return
     *
     */
    <T extends UsuarioEntity> Optional<T> findByNombre(String nombre);
}
