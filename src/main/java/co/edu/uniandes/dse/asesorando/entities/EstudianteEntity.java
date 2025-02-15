package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ESTUDIANTE")
public class EstudianteEntity extends UsuarioEntity {

    @OneToMany(mappedBy = "estudiante")
    private List<ReservaEntity> reservas;

    @OneToMany(mappedBy = "estudiante")
    private List<ComentarioEntity> comentarios;
}
