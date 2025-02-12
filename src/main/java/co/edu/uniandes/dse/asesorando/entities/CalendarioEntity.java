package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.util.Date;
import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesoreEntity;

@Data
@Entity

public class CalendarioEntity extends BaseEntity 
{ 

private Date fechaInicio;
private Date fechaFin;
@podamExclude
@OneToOne
private AsesoriaEntity asesoria;
@podamExclude   
@OneToOne
private ProfesoreEntity profesor;
@podamExclude


}


