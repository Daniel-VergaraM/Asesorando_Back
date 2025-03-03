package co.edu.uniandes.dse.asesorando.dto;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.ProfesorEntity;
import co.edu.uniandes.dse.asesorando.entities.ReservaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioDetailDTO {
    private ProfesorEntity profesor;
    private AsesoriaEntity asesoria;
    private ReservaEntity reserva;
}
