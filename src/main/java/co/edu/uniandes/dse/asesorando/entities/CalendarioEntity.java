package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.util.Date;

@Data
@Entity

public class CalendarioEntity extends BaseEntity 
{ 

private Date fechaInicio;
private Date fechaFin;

}


