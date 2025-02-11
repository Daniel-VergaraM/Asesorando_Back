package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class TematicaEntity extends BaseEntity {

    @NotNull
    private String area;

    @NotNull
    private String tema;

    @ManyToMany(mappedBy = "tematicas")
    private List<ProfesorEntity> profesores;
}
