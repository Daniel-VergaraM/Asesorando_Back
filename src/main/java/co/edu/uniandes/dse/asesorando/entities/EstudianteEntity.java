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
    
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AsesoriaEntity.class)
    private List<AsesoriaEntity> asesorias;

    public EstudianteEntity(){
        super();
        this.asesorias = List.of();
    }

    public EstudianteEntity(EstudianteEntity estudiante){
        super();
        this.asesorias = estudiante.asesorias;
    }
}
