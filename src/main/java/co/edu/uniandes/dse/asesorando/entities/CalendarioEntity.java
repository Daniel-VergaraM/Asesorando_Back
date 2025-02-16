package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import java.util.Date;

@Data
@Entity

public class CalendarioEntity extends BaseEntity 
{ 
    @OneToOne(mappedBy = "calendario")
    private ReservaEntity reserva;
    
    private Date fechaInicio;
    private Date fechaFin;

}


