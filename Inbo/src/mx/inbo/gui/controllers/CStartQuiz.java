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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mx.inbo.entities.Quiz;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CStartQuiz implements Initializable {
    
    private static Quiz quiz;
    
    public static void setQuiz(Quiz quizz){
        quiz = quizz;
    }
    
    @FXML
    private BorderPane mainPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void nextPage(){
        
    }
    
    @FXML
    private void stepBack(){
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }
    
}