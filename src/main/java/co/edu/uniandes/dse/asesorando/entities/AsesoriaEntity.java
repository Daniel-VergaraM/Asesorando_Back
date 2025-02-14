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

    @OneToOne
    private CalendarioEntity calendario;
    @OneToOne
    private ComentarioEntity comentario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private ProfesorEntity profesor;


}


