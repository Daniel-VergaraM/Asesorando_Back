package co.edu.uniandes.dse.asesorando.dto;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.CalendarioEntity;
import co.edu.uniandes.dse.asesorando.entities.ComentarioEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDetailDTO extends ReservaDTO{
    private EstudianteEntity estudiante;
    private CalendarioEntity calendario;
    private AsesoriaEntity asesoria;
    private ComentarioEntity comentario;
}
