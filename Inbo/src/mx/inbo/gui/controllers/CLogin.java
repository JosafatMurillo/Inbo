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
import com.jfoenix.controls.JFXSpinner;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;
import mx.inbo.servidorrmi.ServerConector;
import mx.inbo.servidorrmi.Operaciones;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CLogin implements Initializable {

    @FXML
    private StackPane contentPane;

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private JFXSpinner progressIndicator;

    private ResourceBundle bundle;
    private User user;
    private Operaciones stub;
    private boolean userExists = false;
    private boolean showError = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bundle = rb;
        
        passwordField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (passwordField.getText().length() >= 20) {
                    passwordField.setText(passwordField.getText().substring(0, 20));
                }
            }
        });
        
        userField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (userField.getText().length() >= 20) {
                    userField.setText(userField.getText().substring(0, 20));
                }
            }
        });
    }

    @FXML
    private void loadSignUpPage() {
        Loader.loadNonResizablePage("/mx/inbo/gui/SignUp.fxml", "Sign Up");
    }

    private void loadDashboard() {
        Stage actualStage = (Stage) loginButton.getScene().getWindow();
        Locale locale = Locale.getDefault();
        Stage newStage = new Stage();
        try{
            Parent root = FXMLLoader.load(Loader.class.getResource("/mx/inbo/gui/Dashboard.fxml"), ResourceBundle.getBundle("mx.inbo.lang.lang", locale));
            
            Scene scene = new Scene(root);
            
            newStage.setMinHeight(650);
            newStage.setMinWidth(1050);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.setTitle("Dashboard");
            newStage.show();
            CDashboard.setStage(newStage);
            actualStage.close();
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void closeOperation() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void login() {
        String username = userField.getText();
        String password = passwordField.getText();
        Service<Void> serv;
        serv = new Service<Void>() {
            
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {

                        stub = ServerConector.getStub();

                        try {
                            userExists = stub.validarLogin(username, password);
                            if (userExists) {
                                user = stub.obtenerUsario(username);
                            }
                        } catch (RemoteException | NullPointerException ex) {
                            Logger.getLogger(CDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            showError = true;
                        }

                        Platform.runLater(() -> {
                            if (userExists) {
                                CDashboard.setUser(user);
                                loadDashboard();
                            } else {
                                showErrorMessage();
                            }

                        });

                        return null;
                    }
                };
            }
        };

        progressIndicator.visibleProperty().bind(serv.runningProperty());

        serv.reset();
        serv.start();
    }

    private void showErrorMessage() {
        if (showError) {
            Mensaje alerta = new Mensaje();
            alerta.setHeader(bundle.getString("key.alertLoginTitle"));
            alerta.setBody(bundle.getString("key.connectionFailed"));
            JFXDialog dialog = new JFXDialog(contentPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        } else {
            Mensaje alerta = new Mensaje();
            alerta.setHeader(bundle.getString("key.alertLoginTitle"));
            alerta.setBody(bundle.getString("key.userNotFound"));
            JFXDialog dialog = new JFXDialog(contentPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }
    }

}
