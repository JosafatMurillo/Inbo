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
import mx.inbo.entities.Quiz;


/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CQuizListItem{
    
    @FXML
    private HBox itemPane;
    
    @FXML
    private ImageView quizThumb;
    
    @FXML
    private Label titleLabel;

    public CQuizListItem() {
        FXMLLoader loader = new FXMLLoader(CQuestionListItem.class.getClass().getResource("/mx/inbo/gui/QuizListItem.fxml"));
        loader.setController(this);
        
        try{
            itemPane = loader.load();
        }catch(IOException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setInformation(Quiz quiz){
        String imagePath = quiz.getImagen();
        Image image = new Image(imagePath);
        quizThumb.setImage(image);
        titleLabel.setText(quiz.getTitulo());
    }
    
    public HBox getBox(){
        return itemPane;
    }
    
}
