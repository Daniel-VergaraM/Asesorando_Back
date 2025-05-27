package co.edu.uniandes.dse.asesorando.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Long id;
    private LocalDateTime fechaReserva;
    private Boolean cancelada = false;
    private String estado = "noCompletada";

    private Long estudianteId;
    private Long asesoriaId;

}