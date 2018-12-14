/*
 * Copyright 2018 adolf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.inbo.gui.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import mx.inbo.entities.Question;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CQuestionListItem {
    
    @FXML
    private HBox itemPane;
    
    @FXML
    private ImageView questionThumb;
    
    @FXML
    private Label titleLabel;
    
    public CQuestionListItem(){
        FXMLLoader loader = new FXMLLoader(CQuestionListItem.class.getClass().getResource("/mx/inbo/gui/QuestionListItem.fxml"));
        loader.setController(this);
        
        try{
            itemPane = loader.load();
        }catch(IOException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setInformation(Question question){
        String imagePath = question.getImagen();
        Image image = new Image(imagePath);
        questionThumb.setImage(image);
        titleLabel.setText(question.getPregunta());
    }
    
    public HBox getBox(){
        return itemPane;
    }
    
}