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
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CQuizQuestions implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane titlePane;

    @FXML
    private JFXButton backButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        playIntroAnimation();
    
    }
    
    private void playIntroAnimation(){
        new BounceInLeft(mainPane).play();
    }

    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizMaker.fxml", "New Quiz", actualStage);
    }
    
    @FXML
    private void loadQuestionMaker(){
        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuestionMaker.fxml", "New Question", actualStage);
    }

}
