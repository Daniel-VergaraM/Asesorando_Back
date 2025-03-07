package co.edu.uniandes.dse.asesorando.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CalendarioDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

}
