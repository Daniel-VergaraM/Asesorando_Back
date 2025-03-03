package co.edu.uniandes.dse.asesorando.dto;

import java.util.ArrayList;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioDetailDTO {
    private List<ProfesorDTO> profesores = new ArrayList<>();
    private List<AsesoriaDTO> asesorias  = new ArrayList<>();
    private List<ReservaDTO> reservas   = new ArrayList<>();

}
