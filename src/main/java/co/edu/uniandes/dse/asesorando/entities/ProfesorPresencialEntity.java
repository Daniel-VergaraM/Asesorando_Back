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

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;


/*
 * 
 * Clase que representa un profesor de tipo presencial en la base de datos
 * 
 * @author Daniel-VergaraM
 */
@Data
@Entity
@DiscriminatorValue("PROFESORPRESENCIAL")
public class ProfesorPresencialEntity extends ProfesorEntity {

    private Integer codigoPostal;
    private Double latitud;
    private Double longitud;

    public ProfesorPresencialEntity() {
        super();
        this.tipo = "PROFESORPRESENCIAL";
        this.codigoPostal = 0;
        this.latitud = 0.0;
        this.longitud = 0.0;
    }

    public ProfesorPresencialEntity(ProfesorPresencialEntity profesor) {
        super(profesor);
        this.tipo = "PROFESORPRESENCIAL";
        this.codigoPostal = profesor.codigoPostal;
        this.latitud = profesor.latitud;
        this.longitud = profesor.longitud;
    }
}
