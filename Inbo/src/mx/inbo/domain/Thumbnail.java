/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.domain;

import java.io.Serializable;

/**
 *
 * @author adolf
 */
public class Thumbnail implements Serializable{
    
    private byte[] image;
    private String type;
    private String extention;
    
    public void setImage(byte[] image){
        this.image = image;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public void setExtention(String extention){
        this.extention = extention;
    }
    
    public byte[] getImage(){
        return image;
    }
    
    public String getType(){
        return type;
    }
    
    public String getExtention(){
        return extention;
    }
    
}
