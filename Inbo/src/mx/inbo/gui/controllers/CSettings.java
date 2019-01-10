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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.persistence.NoResultException;
import mx.inbo.LanguageController;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.User;
import mx.inbo.gui.CLanguageItem;
import mx.inbo.gui.tools.FileHelper;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;
import mx.inbo.servidorrmi.Operaciones;
import mx.inbo.servidorrmi.ServerConector;

/**
 * Clase controladora FXML de la página Settings.
 *
 * @author adolf
 */
public class CSettings implements Initializable {

    private static User user;

    public static void setUser(User usr) {
        user = usr;
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane centerPane;

    @FXML
    private JFXButton changeImageButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Circle userImage;

    @FXML
    private ImagePattern userThumb;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField passwordVerificationField;

    @FXML
    private ListView languageList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playIntroAnimation();

        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());

        Thumbnail thumb = user.getImage();
        Image image = new Image(new ByteArrayInputStream(thumb.getImage()));
        ImagePattern imagePattern = new ImagePattern(image);
        userImage.setFill(imagePattern);

        usernameField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (usernameField.getText().length() >= 20) {
                    usernameField.setText(usernameField.getText().substring(0, 20));
                }
            }
        });

        currentPasswordField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (currentPasswordField.getText().length() >= 20) {
                    currentPasswordField.setText(currentPasswordField.getText().substring(0, 20));
                }
            }
        });

        newPasswordField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (newPasswordField.getText().length() >= 20) {
                    newPasswordField.setText(newPasswordField.getText().substring(0, 20));
                }
            }
        });

        passwordVerificationField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (passwordVerificationField.getText().length() >= 20) {
                    passwordVerificationField.setText(passwordVerificationField.getText().substring(0, 20));
                }
            }
        });

        emailField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (emailField.getText().length() >= 50) {
                    emailField.setText(emailField.getText().substring(0, 50));
                }
            }
        });

        ObservableList<String[]> languages = FXCollections.observableArrayList();

        String spanishMX[] = {
            "Español (MX)",
            "/mx/inbo/images/mexico_flag.png",
            "es-MX"
        };

        String englishUS[] = {
            "English (US)",
            "/mx/inbo/images/usa_flag.jpg",
            "en-US"
        };

        languages.add(spanishMX);
        languages.add(englishUS);

        languageList.setItems(languages);

        languageList.setCellFactory(celdas -> new ListCell<String[]>() {

            @Override
            protected void updateItem(String[] info, boolean vacio) {
                super.updateItem(info, vacio);

                if (vacio || info == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    CLanguageItem listController = new CLanguageItem();
                    listController.setInformation(info);
                    setGraphic(listController.getMainPane());
                }
            }
        });

    }

    /**
     * Reproduce la animación inicial.
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
    }

    /**
     * Regresa a la página anterior.
     */
    @FXML
    public void stepBack() {
        Stage actualStage = (Stage) changeImageButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }

    /**
     * Guarda los cambios realizados al perfil.
     */
    @FXML
    private void save() {
        Operaciones stub;
        String username = usernameField.getText();
        String email = emailField.getText();

        if (username != null && email != null) {

            if (!username.isEmpty() && !email.isEmpty()) {
                boolean alreadyExists = true;

                stub = ServerConector.getStub();

                if (!username.equals(user.getUsername())) {
                    try {
                        User existingUser = stub.obtenerUsario(username);
                    } catch (RemoteException | NoResultException ex) {
                        alreadyExists = false;
                    }
                } else {
                    alreadyExists = false;
                }

                if (!alreadyExists) {

                    user.setUsername(username);
                    user.setEmail(email);

                    String newPassword = newPasswordField.getText();

                    if (newPassword != null) {

                        String currentPassword = currentPasswordField.getText();

                        if (!newPassword.isEmpty()) {
                            if (currentPassword.equals(user.getContrasenia())) {

                                String passwordVerify = passwordVerificationField.getText();
                                if (newPassword.equals(passwordVerify)) {
                                    user.setContrasenia(newPassword);
                                } else {
                                    Mensaje message = new Mensaje();
                                    message.setHeader("Contraseña");
                                    message.setBody("La nueva contraseña y la verificación de la conraseña con coinciden");

                                    JFXDialog dialog = new JFXDialog(centerPane, message, JFXDialog.DialogTransition.CENTER);
                                    message.setDialog(dialog);
                                    dialog.show();
                                }
                            } else {
                                Mensaje message = new Mensaje();
                                message.setHeader("Contraseña");
                                message.setBody("La contraseña que ingresó como contraseña actual para el usuario " + username + " no coincide con la contraseña registrada");

                                JFXDialog dialog = new JFXDialog(centerPane, message, JFXDialog.DialogTransition.CENTER);
                                message.setDialog(dialog);
                                dialog.show();
                            }
                        }

                    }

                    try {
                        stub.editarUsuario(user);

                        String[] language = (String[]) languageList.getSelectionModel().getSelectedItem();

                        if (language != null) {
                            LanguageController.setLanguageTag(language[2]);
                            System.out.println(language[2]);
                            System.out.println(LanguageController.getLanguageTag());
                        }

                        stepBack();
                    } catch (RemoteException ex) {
                        Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    Mensaje message = new Mensaje();
                    message.setHeader("Usuario");
                    message.setBody("El nombre de usuario " + username + " ya está ocupado");

                    JFXDialog dialog = new JFXDialog(centerPane, message, JFXDialog.DialogTransition.CENTER);
                    message.setDialog(dialog);
                    dialog.show();
                }
            }

        }
    }

    /**
     * Cambia el icon de usuario.
     */
    @FXML
    private void changeImage() {

        File imageFile;

        Stage actualStage = (Stage) mainPane.getScene().getWindow();

        FileChooser chooser = new FileChooser();

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        imageFile = chooser.showOpenDialog(actualStage);

        Image image = new Image(imageFile.toURI().toString());

        userThumb = new ImagePattern(image);
        userImage.setFill(userThumb);

        Thumbnail thumb = new Thumbnail();
        int extIndex = imageFile.getName().lastIndexOf('.');
        String imageExtention = imageFile.getName().substring(extIndex + 1).toLowerCase();
        thumb.setExtention(imageExtention);

        byte[] imageBytes = FileHelper.parseFileToBytes(imageFile, imageExtention);
        thumb.setImage(imageBytes);
        thumb.setType("User");

        user.setImage(thumb);
    }

}
