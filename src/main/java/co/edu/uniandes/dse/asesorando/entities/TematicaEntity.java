package co.edu.uniandes.dse.asesorando.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class TematicaEntity extends BaseEntity{
    private String area;
    @ManyToMany(mappedBy="tematicas")
    private Set<ProfesorEntity> profesores;
    private String tema;
}
