package co.edu.uniandes.dse.asesorando.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity
public class ReservaEntity extends BaseEntity {

    private LocalDate fechaReserva;

    private boolean cancelada = false;

    //@JoinColumn(name = "estudiante_id", nullable = false)
    //private Estudiante estudiante;

    //@JoinColumn(name = "asesoria_id", nullable = false)
   // private Asesoria asesoria;
    
    
}

