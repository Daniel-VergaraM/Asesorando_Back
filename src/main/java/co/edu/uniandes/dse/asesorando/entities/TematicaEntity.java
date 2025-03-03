package co.edu.uniandes.dse.asesorando.entities;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una temática en la base de datos
 *
 * @author Daniel-VergaraM
 */
@Data
@AllArgsConstructor
@Entity
public class TematicaEntity extends BaseEntity {

    @NotNull
    private String area;

    @NotNull
    private String tema;

    @PodamExclude
    @ManyToMany(mappedBy = "tematicas")
    private List<ProfesorEntity> profesores;

    public TematicaEntity() {
        this.area = "";
        this.tema = "";
        this.profesores = new ArrayList<>();
    }

    public TematicaEntity(TematicaEntity entity) {
        this.area = entity.area;
        this.tema = entity.tema;
        this.profesores = entity.profesores;
    }
}
