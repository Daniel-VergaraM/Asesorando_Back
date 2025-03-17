package co.edu.uniandes.dse.asesorando.dto;

import java.time.LocalDate;

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

}