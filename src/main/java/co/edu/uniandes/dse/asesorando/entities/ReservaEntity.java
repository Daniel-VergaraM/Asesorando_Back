package co.edu.uniandes.dse.asesorando.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaReserva;

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
