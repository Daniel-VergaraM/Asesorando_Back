package co.edu.uniandes.dse.asesorando.entities;


import java.util.Date;

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
public class ReservaEntity extends BaseEntity {

    private Date fechaReserva;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "estudiante")
    private EstudianteEntity estudiante;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "calendario")
    private CalendarioEntity calendario;

    @PodamExclude
    @OneToOne
    @JoinColumn(name = "asesoria")
    private AsesoriaEntity asesoria;

    @PodamExclude
    @OneToOne
    @JoinColumn(name = "comentario")
    private ComentarioEntity comentario;

    private Boolean cancelada = false;

    private String estado = "noCompletada";

}
