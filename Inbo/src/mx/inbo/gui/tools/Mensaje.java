/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.gui.tools;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;

/**
 *
 * @author adolf
 */
public class Mensaje extends JFXDialogLayout {
    
    private Text header;
    private Text body;
    private JFXButton botonAceptar;
    
    public Mensaje(){
        header = new Text();
        body = new Text();
        body.setWrappingWidth(300);
        botonAceptar = new JFXButton("Aceptar");
    }
    
    public void setDialog(JFXDialog dialog){
        botonAceptar.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                }
                
        });
        setActions(botonAceptar);
    }
    
    public void setHeader(String content){
        header.setText(content);
        setHeading(header);
    }
    
    public void setButtonLabel(String label){
        botonAceptar = new JFXButton(label);
    }
    
    public void setBody(String content){
        body.setText(content);
        setBody(body);
    }
    
    public JFXDialogLayout getLayout(){
        return this;
    }
}
