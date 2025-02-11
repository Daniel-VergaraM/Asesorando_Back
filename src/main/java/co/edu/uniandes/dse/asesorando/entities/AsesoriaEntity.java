package co.edu.uniandes.dse.asesorando.entities;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity



public class AsesoriaEntity extends BaseEntity {
    private String duracion;
    private String tematica;
    private String tipo;
    private String area;
    
    
    



}
