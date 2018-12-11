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

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CDashboard implements Initializable {
    
    private static User user;
    private static List<Quiz> quizzes;
    
    public static void setUser(User actualUser){
        user = actualUser;
    }
    
    public static User getUser(){
        return user;
    }
    
    public static void setQuizzes(List<Quiz> quizes){
        quizzes = quizes;
    }
    
    public static List<Quiz> getQuizzes(){
        return quizzes;
    }
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private VBox lateralMenu;
    
    @FXML
    private ListView listaQuizzes;
    
    @FXML
    private JFXButton createQuizButton;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label emailLabel;
    
    private Stage actualStage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         usernameLabel.setText(user.getUsername());
         emailLabel.setText(user.getEmail());
         
         if(quizzes != null){
            ObservableList<Quiz> observableQuiz = FXCollections.observableArrayList(quizzes);
            listaQuizzes.setItems(observableQuiz);
            
            listaQuizzes.setCellFactory(celdas -> new ListCell<Quiz>() {
            
                @Override
                protected void updateItem(Quiz quiz, boolean vacio) {
                    super.updateItem(quiz, vacio);

                    if (vacio || quiz == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        CQuizListItem listController = new CQuizListItem();
                        listController.setInformation(quiz);
                        setGraphic(listController.getBox());
                    }
                }
            });
        }
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
