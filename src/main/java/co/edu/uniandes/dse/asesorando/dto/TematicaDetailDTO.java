package co.edu.uniandes.dse.asesorando.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TematicaDetailDTO extends TematicaDTO {

    private List<ProfesorDTO> profesores = new ArrayList<>();

    


}
