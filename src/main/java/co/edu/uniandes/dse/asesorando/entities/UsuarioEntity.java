package co.edu.uniandes.dse.asesorando.entities;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class UsuarioEntity extends BaseEntity {
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    // TODO: Cambiar de String a AseoriaEntity
    private ArrayList<String> asesorias;
}
