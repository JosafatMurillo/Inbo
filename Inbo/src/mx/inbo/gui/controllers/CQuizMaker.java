/*
 * Nombre de la clase:  CQuizMaker.java
 * Versión:             1.0
 * Fecha:               27/10/2018
 *
 * Copyright 2018 Adolf Ángel.
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
import animatefx.animation.FadeIn;
import animatefx.animation.SlideInLeft;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
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
public class CQuizMaker implements Initializable {
    
    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane thumbnailPane;

    @FXML
    private StackPane centerPane;

    @FXML
    private StackPane infoStack;

    @FXML
    private ImageView thumbnail;

    @FXML
    private JFXButton backButton;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

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
            titleField.setPrefWidth(infoWidth);
            descriptionField.setPrefWidth(infoWidth);
        });

        playIntroAnimation();
        
    }
    
    private void playIntroAnimation(){
        
        new BounceInLeft(mainPane).play();
        new SlideInLeft(thumbnailPane).play();
        
    }

    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }
    
    @FXML
    private void nextPage() {
        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizQuestions.fxml", "Questions", actualStage);
    }

}
