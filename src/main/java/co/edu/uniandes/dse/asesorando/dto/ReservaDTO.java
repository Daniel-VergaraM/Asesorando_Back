package co.edu.uniandes.dse.asesorando.dto;

import java.sql.Date;
import java.time.LocalDate;

import co.edu.uniandes.dse.asesorando.entities.AsesoriaEntity;
import co.edu.uniandes.dse.asesorando.entities.EstudianteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private LocalDate fechaReserva;
    private Boolean cancelada = false;
    private String estado = "noCompletada";
    public AsesoriaEntity asesoria;
    public EstudianteEntity estudiante;

}
