package co.edu.uniandes.dse.asesorando.entities;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data   
@Entity
public class AsesoriaEntity extends BaseEntity {

    private String duracion;
    private String tematica;
    private String tipo;
    private String area;
    private Boolean completada;

    @ManyToOne(targetEntity = UsuarioEntity.class)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "calendario_id")
    private CalendarioEntity calendario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private ProfesorEntity profesor;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private ReservaEntity reserva;
    
}
