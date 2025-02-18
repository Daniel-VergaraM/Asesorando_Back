package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una temática en la base de datos
 *
 * @author Daniel-VergaraM
 */
@Data
@Entity
public class TematicaEntity extends BaseEntity {

    @NotNull
    private String area;

    @NotNull
    private String tema;

    @PodamExclude
    @ManyToMany(mappedBy = "tematicas")
    private List<ProfesorEntity> profesores;
}
