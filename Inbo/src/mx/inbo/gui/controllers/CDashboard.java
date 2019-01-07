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
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.ByteArrayInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;
import mx.inbo.servidorrmi.Operaciones;
import mx.inbo.servidorrmi.ServerConector;

/**
 * Clase controladora del Dashboard.
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

    /**
     * *
     * Asigna el valor de un objeto User a la variable interna.
     *
     * @param actualUser Objeto a asignar a la variable interna.
     */
    public static void setUser(User actualUser) {
        user = actualUser;
    }

    public static User getUser() {
        return user;
    }

    public static void setStage(Stage actualS) {
        actualStage = actualS;
    }

    public static Stage getStage() {
        return actualStage;
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
    private Circle userImage;

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

        Thumbnail thumb = user.getImage();
        Image image = new Image(new ByteArrayInputStream(thumb.getImage()));
        ImagePattern imagePattern = new ImagePattern(image);
        userImage.setFill(imagePattern);
        
        playIntroAnimation();

    }
    
    /**
     * Reproduce la animación inicial.
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
    }

    /**
     * Carga la página Quiz Maker para añadir un quiz.
     */
    @FXML
    private void loadQuizMaker() {
        CQuizMaker.setQuiz(null);
        loadPage("/mx/inbo/gui/QuizMaker.fxml", "New Quiz");
    }

    /**
     * Carga la página Settings para cambiar la configuración de la aplicación.
     */
    @FXML
    private void loadSettings() {
        CSettings.setUser(user);
        loadPage("/mx/inbo/gui/Settings.fxml", "Settings");
    }

    /**
     * Carga la página Quiz Code para iniciar una partida a base de un código
     * quiz.
     */
    @FXML
    private void loadPlayWithCode() {
        CQuizCode.setUser(user);
        loadPage("/mx/inbo/gui/QuizCode.fxml", "Play with code");
    }

    /**
     * Cierra la sesión del usuario.
     */
    @FXML
    private void logOut() {
        Loader.loadUndecoratedPageClosingCurrent("/mx/inbo/gui/Login.fxml", "Login", actualStage);
    }

    /**
     * Carga una página en el stage actual.
     *
     * @param url URL del archivo fxml.
     * @param title Título de la ventana.
     */
    private static void loadPage(String url, String title) {
        Loader.loadPageInCurrentStage(url, title, actualStage);
    }

    /**
     * Elimina un quiz de la lista y del servidor.
     *
     * @param quiz Quiz a eliminar.
     */
    public static void deleteQuiz(Quiz quiz) {
        JFXDialogLayout layout = new JFXDialogLayout();
        Text header = new Text(bundle.getString("key.dwarningHeader"));
        Text body = new Text(bundle.getString("key.dwarningBody"));
        String yesLabel = bundle.getString("key.dwarningYes");
        String noLabel = bundle.getString("key.dwarningNo");
        layout.setHeading(header);
        layout.setBody(body);
        JFXButton yesButton = new JFXButton(yesLabel);
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
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
        noButton.setOnAction(new EventHandler<ActionEvent>() {
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
