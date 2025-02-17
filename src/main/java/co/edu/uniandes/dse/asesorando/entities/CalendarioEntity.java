package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import java.util.Date;
import java.util.List;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class CalendarioEntity extends BaseEntity 
{ 
    @OneToOne(mappedBy = "calendario")
    private ReservaEntity reserva;
    
    private Date fechaInicio;
    private Date fechaFin;

@PodamExclude
@OneToMany(mappedBy = "calendario",fetch = FetchType.LAZY)

private List<ReservaEntity> reservas;


@PodamExclude
@ManyToOne(fetch = FetchType.LAZY)

private ProfesorEntity profesor;

@PodamExclude
@OneToMany(mappedBy = "calendario",fetch = FetchType.LAZY)

private List<AsesoriaEntity> asesorias;

}