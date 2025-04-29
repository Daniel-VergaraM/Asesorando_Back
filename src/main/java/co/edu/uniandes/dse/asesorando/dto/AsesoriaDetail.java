package co.edu.uniandes.dse.asesorando.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsesoriaDetail extends AsesoriaDTO {
    private CalendarioDTO calendario;
    private List<ProfesorDTO> profesor = new ArrayList<>();
    private List<ReservaDTO> reserva = new ArrayList<>();
}
