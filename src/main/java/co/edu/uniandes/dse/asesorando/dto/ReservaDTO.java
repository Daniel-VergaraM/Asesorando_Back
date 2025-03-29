package co.edu.uniandes.dse.asesorando.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Date fechaReserva;
    private Boolean cancelada = false;
    private String estado = "noCompletada";

}