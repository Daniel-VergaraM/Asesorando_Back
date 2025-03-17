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

import java.util.List;

import javax.validation.constraints.NotNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.co.jemos.podam.common.PodamExclude;

/**
 *
 * Clase que representa un profesor de cualquier tipo en la base de datos
 *
 * @author Daniel-VergaraM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ProfesorEntity extends UsuarioEntity {

    @NotNull
    protected String tipo = "PROFESOR";


    @NotNull
    private String formacion;

    @PodamExclude
    @ManyToMany
    private List<TematicaEntity> tematicas;

    @PodamExclude
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AsesoriaEntity.class)
    private List<AsesoriaEntity> asesorias;

    @NotNull
    private String experiencia;

    @NotNull
    private String precioHora;

    @NotNull
    private String fotoUrl;

    @NotNull
    private String videoUrl;

    @PodamExclude
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = CalendarioEntity.class)
    private List<CalendarioEntity> calendario;

}
