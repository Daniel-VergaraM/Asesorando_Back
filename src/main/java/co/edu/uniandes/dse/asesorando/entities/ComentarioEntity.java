package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ComentarioEntity extends BaseEntity {

    private String comentario;

    private Integer calificacion;

    @ManyToOne
    private EstudianteEntity estudiante;

}
