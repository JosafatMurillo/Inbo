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
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CGameScreen implements Initializable {

    private static String quizTitle;
    private static Integer numQuestions = 0;
    private static byte[] image;
    private final int MAX_TIME = 10;
    private Integer timeSeconds = MAX_TIME;

    public static void setQuizTitle(Object[] params) {
        if (params != null) {
            if (params.length == 3) {
                quizTitle = (String) params[0];
                numQuestions = (int) params[1];
                image = (byte[]) params[2];
            }
        }
    }

    @FXML
    private StackPane mainPane;

    @FXML
    private StackPane labelPane;

    @FXML
    private ImageView background;

    @FXML
    private Label quizTitleLabel;

    @FXML
    private Label counterLabel;
    
    @FXML
    private Label numQuestionsLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mainPane.widthProperty().addListener((objects, oldValue, newValue) -> {
            background.setFitWidth((double) newValue);
            labelPane.setPrefWidth((double) newValue);
        });

        mainPane.heightProperty().addListener((objects, oldValue, newValue) -> {
            background.setFitHeight((double) newValue);
        });

        new BounceInLeft(mainPane).play();
        new SlideInLeft(labelPane).play();

        if (quizTitle == null) {
            quizTitle = "Quiz Title";
        }

        quizTitleLabel.setText(quizTitle);

        counterLabel.setText(timeSeconds.toString());
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler() {
                    // KeyFrame event handler
                    @Override
                    public void handle(Event event) {
                        timeSeconds--;
                        // update timerLabel
                        counterLabel.setText(
                                timeSeconds.toString());
                        if (timeSeconds <= 0) {
                            timeline.stop();
                            Stage stage = (Stage) mainPane.getScene().getWindow();
                            Loader.loadPageInCurrentStage("/mx/inbo/gui/GameScreen2.fxml", "Game", stage);
                        }
                    }
                }));
        timeline.playFromStart();
        
        numQuestionsLabel.setText(numQuestions.toString());

        Image thumb = new Image(new ByteArrayInputStream(image));
        background.setImage(thumb);
    }

}
