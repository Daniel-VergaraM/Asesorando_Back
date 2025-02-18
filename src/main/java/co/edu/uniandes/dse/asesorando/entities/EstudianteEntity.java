package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("ESTUDIANTE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EstudianteEntity extends UsuarioEntity {
    
    @OneToMany(mappedBy = "estudiante", targetEntity = ReservaEntity.class)
    private List<ReservaEntity> reservas;
  
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AsesoriaEntity.class)
    private List<AsesoriaEntity> asesorias;

    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ComentarioEntity.class)
    private List<ComentarioEntity> comentarios;

    public EstudianteEntity(){
        super();
        this.tipo = "ESTUDIANTE";
        this.reservas = List.of();
        this.asesorias = List.of();
    }

    public EstudianteEntity(EstudianteEntity estudiante){
        super();
        this.tipo = "ESTUDIANTE";
        this.reservas = estudiante.reservas;
        this.asesorias = estudiante.asesorias;
    }

}
