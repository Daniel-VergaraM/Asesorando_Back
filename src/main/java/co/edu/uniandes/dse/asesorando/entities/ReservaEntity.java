package co.edu.uniandes.dse.asesorando.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private EstudianteEntity estudiante;

    //@JoinColumn(name = "asesoria_id", nullable = false)
    @OneToMany(mappedBy = "reserva")
    private AsesoriaEntity asesoria;

    private boolean cancelada = false;

    private String estado = "noCompletada";
    
}

