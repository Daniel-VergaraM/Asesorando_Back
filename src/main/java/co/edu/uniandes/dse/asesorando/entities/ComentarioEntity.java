package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class ComentarioEntity extends BaseEntity {

    private String comentario;

    private int calificacion;

    //private EstudianteEntity estudiante;

   
}