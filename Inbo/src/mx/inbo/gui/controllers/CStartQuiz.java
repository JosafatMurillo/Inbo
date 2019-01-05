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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.entities.Quiz;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CStartQuiz implements Initializable {

    private static Quiz quiz;

    public static void setQuiz(Quiz quizz) {
        quiz = quizz;
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane leftPane;

    @FXML
    private TextField emailField;

    @FXML
    private JFXButton enterButton;

    @FXML
    private ListView friendsList;

    private ResourceBundle bundle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list;
        list = FXCollections.observableArrayList();
        friendsList.setItems(list);

        bundle = rb;
    }

    @FXML
    private void nextPage() {
        System.out.println("Pressed");
    }

    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }

    @FXML
    private void addFriend() {

        String email = emailField.getText();

        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";

        boolean showEmptyMessage = false;

        if (email != null) {
            if (!email.isEmpty()) {

                Pattern pattern = Pattern.compile(emailPattern);
                Matcher matcher = pattern.matcher(email);

                if (matcher.matches()) {
                    ObservableList<String> list = friendsList.getItems();
                    list.add(email);
                    friendsList.setItems(list);
                    emailField.setText("");
                } else {
                    Mensaje alerta = new Mensaje();
                    alerta.setHeader(bundle.getString("key.emailIssue"));
                    alerta.setBody(bundle.getString("key.wrongEmailFormat"));
                    JFXDialog dialog = new JFXDialog(leftPane, alerta, JFXDialog.DialogTransition.CENTER);

                    dialog.show();
                }
            } else {
                showEmptyMessage = true;
            }
        } else {
            showEmptyMessage = true;
        }

        if (showEmptyMessage) {
            Mensaje alerta = new Mensaje();
            alerta.setHeader(bundle.getString("key.emptyFieldTitle"));
            alerta.setBody(bundle.getString("key.emptyField"));
            JFXDialog dialog = new JFXDialog(leftPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }
    }
}
