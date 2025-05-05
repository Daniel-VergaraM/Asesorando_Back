package co.edu.uniandes.dse.asesorando.entities;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EstudianteEntity extends UsuarioEntity {

    @NotNull
    private String tipo = "ESTUDIANTE";

    @PodamExclude
    @OneToMany(mappedBy = "estudiante", targetEntity = ReservaEntity.class)
    private List<ReservaEntity> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "comentario", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ComentarioEntity.class)
    private List<ComentarioEntity> comentarios = new ArrayList<>();

}
