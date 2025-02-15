package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("ESTUDIANTE")
public class EstudianteEntity extends UsuarioEntity {
    
    @OneToMany(mappedBy = "estudiante")
    private List<ReservaEntity> reservas;
}
