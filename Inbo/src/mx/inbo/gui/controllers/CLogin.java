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
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;
import mx.inbo.server.ServerConector;
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
        
        stub = ServerConector.getStub();
    }

    @FXML
    private void loadSignUpPage() {
        Loader.loadNonResizablePage("/mx/inbo/gui/SignUp.fxml", "Sign Up");
    }

    private void loadDashboard() {
        Stage actualStage = (Stage) loginButton.getScene().getWindow();
        Loader.loadPageClosingCurrent("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
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
        Service<Void> serv = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        try {
                            userExists = stub.validarLogin(username, password);
                            if (userExists) {
                                user = stub.obtenerUsario(username);
                            }
                        } catch (RemoteException | NullPointerException ex) {
                            showError = true;
                        }

                        Platform.runLater(() -> {
                            if (userExists) {
                                CDashboard.setUser(user);
                                loadDashboard();
                            } else {

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

    private void logn() {
        String username = userField.getText();
        String password = passwordField.getText();
        try {
            boolean exists = stub.validarLogin(username, password);
            if (exists) {
                User user = stub.obtenerUsario(username);
                CDashboard.setUser(user);
                loadDashboard();
            } else {
                Mensaje alerta = new Mensaje();
                alerta.setHeader("Login");
                alerta.setBody("The user does not exists. Please, verify your information and try again.");
                JFXDialog dialog = new JFXDialog(contentPane, alerta, JFXDialog.DialogTransition.CENTER);

                dialog.show();
            }
        } catch (RemoteException | NullPointerException ex) {
            Mensaje alerta = new Mensaje();
            alerta.setHeader("Login");
            alerta.setBody("An error occurred while login. Please, try again in a few minutes.");
            JFXDialog dialog = new JFXDialog(contentPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }

    }

}
