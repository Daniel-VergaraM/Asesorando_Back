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

<<<<<<< HEAD
    @OneToOne
    @JoinColumn(name = "calendario")
    private CalendarioEntity calendario;
    
=======
    @ManyToOne
    @JoinColumn(name = "calendario_id")
    private CalendarioEntity calendario;

    @NotNull
>>>>>>> 7398f4e804032446d833015a386fbe16a30f3a8a
    @ManyToOne
    @JoinColumn(name = "profesor")
    private ProfesorEntity profesor;

    @OneToOne
    @JoinColumn(name = "calendario")
    private ReservaEntity reserva;
    
}
