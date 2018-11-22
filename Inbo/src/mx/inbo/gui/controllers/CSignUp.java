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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Mensaje;
import mx.inbo.server.ServerConector;
import mx.inbo.servidorrmi.Operaciones;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CSignUp implements Initializable {

    @FXML
    private StackPane contentPane;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private JFXSpinner progressIndicator;

    private ResourceBundle bundle;
    private User user;
    private Operaciones stub;
    private boolean showError = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        bundle = rb;
        
        user = new User();
        ServerConector.useErrorMessage(contentPane, bundle);
        
    }

    @FXML
    private void signUp() {

        String username = usernameField.getText();
        String email = emailField.getText();

        user.setUsername(username);
        user.setEmail(email);
        user.setImagen("/mx/inbo/image/dog.jpg");

        Service<Void> serv = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        
                        stub = ServerConector.getStub();
                        
                        try {
                            stub.agregarUsario(user);
                        } catch (RemoteException | NullPointerException ex) {
                            if(ex.getClass() != NullPointerException.class){
                                showError = true;
                            }
                        }

                        Platform.runLater(() -> {
                            if (showError) {
                                Mensaje alerta = new Mensaje();
                                alerta.setHeader(bundle.getString("key.alertSignUpTitle"));
                                alerta.setBody(bundle.getString("key.signUpErrorMessage"));
                                JFXDialog dialog = new JFXDialog(contentPane, alerta, JFXDialog.DialogTransition.CENTER);

                                dialog.show();
                            }else{
                                ServerConector.useErrorMessage(contentPane, bundle);
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

}
