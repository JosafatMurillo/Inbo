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
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;
import mx.inbo.servidorrmi.Operaciones;
import mx.inbo.servidorrmi.ServerConector;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CDashboard implements Initializable {

    private static User user;
    private static JFXDialog dialog;
    private static ResourceBundle bundle;
    private static Stage actualStage;
    private static Operaciones stub;
    private static List<Quiz> quizzes;

    public static void setUser(User actualUser) {
        user = actualUser;
    }

    public static User getUser() {
        return user;
    }
    
    public static void setStage(Stage actualS) {
        actualStage = actualS;
    }

    public static void setQuizzes(List<Quiz> quizes) {
        quizzes = quizes;
    }

    public static List<Quiz> getQuizzes() {
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

    @FXML
    private Label numberQuizzesLabel;
    
    @FXML
    private StackPane centerPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        
        if (quizzes == null) {
            stub = ServerConector.getStub();
            try {
                quizzes = stub.obtenerQuizzes(user);
            } catch (RemoteException ex) {
                Logger.getLogger(CDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (quizzes != null) {
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

            numberQuizzesLabel.setText(String.valueOf(quizzes.size()));
        } else {
            numberQuizzesLabel.setText("0");
        }
        
        dialog = new JFXDialog();
        dialog.setDialogContainer(centerPane);
    }

    @FXML
    private void loadQuizMaker() {
        loadPage("/mx/inbo/gui/QuizMaker.fxml", "New Quiz");
    }

    @FXML
    private void loadSettings() {
        loadPage("/mx/inbo/gui/Settings.fxml", "Settings");
    }

    @FXML
    private void loadPlayWithCode() {
        loadPage("/mx/inbo/gui/QuizCode.fxml", "Play with code");
    }

    private static void loadPage(String url, String title) {
        Loader.loadPageInCurrentStage(url, title, actualStage);
    }
    
    public static void deleteQuiz(Quiz quiz){
        JFXDialogLayout layout = new JFXDialogLayout();
        Text header = new Text(bundle.getString("key.dwarningHeader"));
        Text body = new Text(bundle.getString("key.dwarningBody"));
        String yesLabel = bundle.getString("key.dwarningYes");
        String noLabel = bundle.getString("key.dwarningNo");
        layout.setHeading(header);
        layout.setBody(body);
        JFXButton yesButton = new JFXButton(yesLabel);
        yesButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    try {
                        stub.eliminarQuiz(quiz);
                        CDashboard.setUser(user);
                        CDashboard.setQuizzes(null);
                        loadPage("/mx/inbo/gui/Dashboard.fxml", "Dashboard");
                        dialog.close();
                    } catch (RemoteException ex) {
                        Logger.getLogger(CDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
        });
        JFXButton noButton = new JFXButton(noLabel);
        noButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                }
                
        });
        
        List<JFXButton> actions = new ArrayList<>();
        actions.add(noButton);
        actions.add(yesButton);
        
        layout.setActions(actions);
        
        dialog.setContent(layout);
        dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        
        dialog.show();
    }
    
}
