package co.edu.uniandes.dse.asesorando.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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

    //@JoinColumn(name = "estudiante_id", nullable = false)
    @ManyToOne(targetEntity = EstudianteEntity.class, optional = false, cascade = CascadeType.ALL)
    private EstudianteEntity estudiante;

    //@JoinColumn(name = "asesoria_id", nullable = false)
    @OneToOne(targetEntity = AsesoriaEntity.class, mappedBy = "reserva", optional = false, cascade = CascadeType.ALL)
    private AsesoriaEntity asesoria;

    private boolean cancelada = false;

    private String estado = "noCompletada";

}
