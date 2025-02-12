package co.edu.uniandes.dse.asesorando.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.asesorando.entities.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    <T extends UsuarioEntity> Optional<T> findByCorreo(String correo);

    <T extends UsuarioEntity> List<T> findByTipo(String tipo);

    <T extends UsuarioEntity> Optional<T> findByNombre(String nombre);
}
