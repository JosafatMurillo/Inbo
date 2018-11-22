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

import animatefx.animation.BounceInLeft;
import animatefx.animation.SlideInLeft;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CQuestionMaker implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane thumbnailPane;

    @FXML
    private ImageView thumbnail;

    @FXML
    private StackPane centerPane;

    @FXML
    private StackPane infoStack;

    @FXML
    private TextField titleField;
    
    @FXML
    private TextField timeLimitField;
    
    @FXML
    private TextField answer1Field;
    
    @FXML
    private TextField answer2Field;
    
    @FXML
    private TextField answer3Field;
    
    @FXML
    private TextField answer4Field;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mainPane.widthProperty().addListener((objects, oldValue, newValue) -> {
            double width = (double) newValue / 2;
            thumbnail.setFitWidth(width - 10);
            thumbnailPane.setPrefWidth(width);
            centerPane.setPrefSize(width, mainPane.getHeight());

            double infoWidth = width - 20;
            infoStack.setPrefWidth(infoWidth);
            titleField.setPrefWidth(infoWidth/2);
            timeLimitField.setPrefWidth(infoWidth/2);
            answer1Field.setPrefWidth(infoWidth/2);
            answer2Field.setPrefWidth(infoWidth/2);
            answer3Field.setPrefWidth(infoWidth/2);
            answer4Field.setPrefWidth(infoWidth/2);
        });
        
        playIntroAnimation();
    }
    
    private void playIntroAnimation(){
        new BounceInLeft(mainPane).play();
        new SlideInLeft(thumbnailPane).play();
    }
    
    @FXML
    private void saveQuestion(){
        
        String title = titleField.getText();
        String answer1 = answer1Field.getText();
        String answer2 = answer2Field.getText();
        
        try{
            Integer limit = Integer.parseInt(timeLimitField.getText());
        }catch(InputMismatchException ex){
            Logger.getLogger(CQuestionMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(title.isEmpty() && answer1.isEmpty() && answer2.isEmpty()){
            
            
            
        }
    }

    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizQuestions.fxml", "Questions", actualStage);
    }
}
