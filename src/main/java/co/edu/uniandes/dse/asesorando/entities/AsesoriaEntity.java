/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package co.edu.uniandes.dse.asesorando.entities;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;


@Data   
@Entity
public class AsesoriaEntity extends BaseEntity {

    private String duracion;
    private String tematica;
    private String tipo;
    private String area;
    private Boolean completada;


    @PodamExclude
    @ManyToOne(targetEntity = UsuarioEntity.class)
    private UsuarioEntity usuario;


    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "calendario_id")
    private CalendarioEntity calendario;

    @PodamExclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private ProfesorEntity profesor;

    @PodamExclude
    @OneToOne
    @JoinColumn(name = "reserva_id")
    private ReservaEntity reserva;
    
}
