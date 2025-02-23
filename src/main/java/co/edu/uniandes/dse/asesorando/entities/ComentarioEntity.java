package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ComentarioEntity extends BaseEntity {

    private String comentario;

    private Integer calificacion;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "estudiante")
    private EstudianteEntity estudiante;

    @PodamExclude
    @OneToOne(mappedBy = "comentario")
    private ReservaEntity reserva;
   
}
