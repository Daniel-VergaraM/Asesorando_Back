package co.edu.uniandes.dse.asesorando.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.repositories.ComentarioRepository;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Transactional;

@Service
@MappedSuperclass
public class ComentarioService{

    @Autowired
    private ComentarioRepository comentarioRepository;
@Transactional    
public ComentarioEntity crearComentario(ComentarioEntity comentario) throws IllegalArgumentException{
 
    if( comentario.getComentario() == null || comentario.getComentario().isEmpty() 
    || comentario.getCalificacion() == null) {
        throw new IllegalArgumentException("El comentario no puede ser nulo o vacio");
    }

    if(comentario.getCalificacion() < 0 || comentario.getCalificacion() > 5){
        throw new IllegalArgumentException("La calificacion debe estar entre 0 y 5");
}

    return comentarioRepository.save(comentario);
}

@Transactional
public ComentarioEntity leer_comentario(ComentarioEntity comentario){

    if (comentario.getId() == null){
        throw new IllegalArgumentException("El id del comentario no puede ser nulo");
    }
    
    if(( !comentarioRepository.existsById(comentario.getId()))){
        throw new IllegalArgumentException("El comentario no existe");
    }
    
    return comentarioRepository.findById(comentario.getId()).orElse(null);
}

@Transactional
public ComentarioEntity actualizar_comentario(ComentarioEntity comentario){
    if( comentario.getComentario() == null || comentario.getComentario().isEmpty() 
    || comentario.getCalificacion() == null) {
        throw new IllegalArgumentException("El comentario no puede ser nulo o vacio");
    }

    if(comentario.getCalificacion() < 0 || comentario.getCalificacion() > 5){
        throw new IllegalArgumentException("La calificacion debe estar entre 0 y 5");

    }

    
    return comentarioRepository.save(comentario);
}


@Transactional
public void  eliminar_comentario(ComentarioEntity comentario){
    if(comentario.getId() == null){
        throw new IllegalArgumentException("El id del comentario no puede ser nulo");
    }
    if(( !comentarioRepository.existsById(comentario.getId()))){
        throw new IllegalArgumentException("El comentario no existe");
    }   
    comentarioRepository.deleteById(comentario.getId());
}


}