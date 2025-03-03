package co.edu.uniandes.dse.asesorando.dto;

import java.util.*;
import lombok.Data;
@Data

public class EstudianteDetailDTO extends EstudianteDTO{

    private List<ComentarioDTO> comentarios = new ArrayList<>();
    private List<ReservaDTO> reservas = new ArrayList<>();
}
