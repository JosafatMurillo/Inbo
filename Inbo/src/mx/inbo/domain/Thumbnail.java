/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.domain;

import java.io.Serializable;

/**
 * Clase que representa las portadas o thumbnails de los quiz, preguntas y
 * respuestas. Esta clase almecena un arreglo de bytes de un archivo de imagen,
 * así como su extensión o formato y el tipo de portada que representa.
 *
 * @author adolf
 */
public class Thumbnail implements Serializable {

    private byte[] image;
    private String type;
    private String extention;

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    public byte[] getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getExtention() {
        return extention;
    }

}
