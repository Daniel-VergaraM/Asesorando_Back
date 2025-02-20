package co.edu.uniandes.dse.asesorando.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CalendarioEntity extends BaseEntity 
{ 
    
    private Date fechaInicio;
    private Date fechaFin;

@PodamExclude
@OneToMany(mappedBy = "calendario", fetch = FetchType.LAZY)
private List<ReservaEntity> reservas;


@PodamExclude
@ManyToOne(fetch = FetchType.LAZY)

private ProfesorEntity profesor;

@PodamExclude
@OneToMany(mappedBy = "calendario", fetch = FetchType.LAZY)
private List<AsesoriaEntity> asesorias;

}