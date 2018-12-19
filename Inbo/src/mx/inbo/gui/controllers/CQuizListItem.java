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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Quiz;
import mx.inbo.gui.tools.Loader;


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
    
    private Quiz quiz;

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
        this.quiz = quiz;
        Thumbnail thumb = quiz.getImage();
        Image image = new Image(new ByteArrayInputStream(thumb.getImage()));
        quizThumb.setImage(image);
        titleLabel.setText(quiz.getTitulo());
    }
    
    public HBox getBox(){
        return itemPane;
    }
    
    @FXML
    private void delete(){
        CDashboard.deleteQuiz(quiz);
    }
    
    @FXML
    private void edit(){
        Stage stage = CDashboard.getStage();
        CQuizMaker.setQuiz(quiz);
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizMaker.fxml", "New Quiz", stage);
    }
    
    @FXML
    private void play(){
        Stage stage = CDashboard.getStage();
        CStartQuiz.setQuiz(quiz);
        Loader.loadPageInCurrentStage("/mx/inbo/gui/StartQuiz.fxml", "Start Quiz", stage);
    }
}
