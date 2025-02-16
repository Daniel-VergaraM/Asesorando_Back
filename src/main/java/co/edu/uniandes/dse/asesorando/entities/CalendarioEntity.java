package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.Date;
import java.util.List;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class CalendarioEntity extends BaseEntity 
{ 

private Date fechaInicio;
private Date fechaFin;

@PodamExclude
@OneToMany(mappedBy = "calendario",fetch = FetchType.LAZY)
@JoinColumn (name = "reserva")
private List<ReservaEntity> reservas;


@PodamExclude
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn (name = "profesor")
private ProfesorEntity profesor;

@PodamExclude
@OneToMany(mappedBy = "calendario",fetch = FetchType.LAZY)
@JoinColumn (name = "asesoria")
private List<AsesoriaEntity> asesorias;

}