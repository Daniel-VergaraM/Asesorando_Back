package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;
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

    public EstudianteEntity(){
        super();
        this.tipo = "ESTUDIANTE";
        this.reservas = List.of();
    }

    public EstudianteEntity(EstudianteEntity estudiante){
        super();
        this.reservas = estudiante.reservas;
    }
}
