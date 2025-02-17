package co.edu.uniandes.dse.asesorando.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ReservaEntity extends BaseEntity {

    private LocalDate fechaReserva;

    @ManyToOne
    @JoinColumn(name = "estudiante")
    private EstudianteEntity estudiante;

    @ManyToOne
    @JoinColumn(name = "calendario")
    private CalendarioEntity calendario;

    @OneToOne
    @JoinColumn(name = "asesoria")
    private AsesoriaEntity asesoria;

    @OneToOne
    @JoinColumn(name = "comentario")
    private ComentarioEntity comentario;

    private boolean cancelada = false;

    private String estado = "noCompletada";

}
