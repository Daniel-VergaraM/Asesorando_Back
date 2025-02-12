package co.edu.uniandes.dse.asesorando.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("ESTUDIANTE")
public class EstudianteEntity extends UsuarioEntity {
    
}
