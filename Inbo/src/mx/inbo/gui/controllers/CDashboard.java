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

import animatefx.animation.BounceOutLeft;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CDashboard implements Initializable {
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private VBox lateralMenu;
    
    @FXML
    private JFXButton createQuizButton;
    
    private Stage actualStage;
    
    private Loader loader;
    
    private BounceOutLeft transicion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        transicion = new BounceOutLeft(mainPane); 
    }
    
    @FXML
    private void loadQuizMaker(){
        actualStage = (Stage) createQuizButton.getScene().getWindow();
        loadPage("/mx/inbo/gui/QuizMaker.fxml", "New Quiz", actualStage);
    }

    @FXML
    private void loadSettings(){
        actualStage = (Stage) createQuizButton.getScene().getWindow();
        loadPage("/mx/inbo/gui/Settings.fxml", "Settings", actualStage);
    }
    
    @FXML
    private void loadPlayWithCode(){
        actualStage = (Stage) createQuizButton.getScene().getWindow();
        loadPage("/mx/inbo/gui/QuizCode.fxml", "Play with code", actualStage);
    }
    
    private void loadPage(String url, String title, Stage actualStage){
        Loader.loadPageInCurrentStage(url, title, actualStage);
    }
}
