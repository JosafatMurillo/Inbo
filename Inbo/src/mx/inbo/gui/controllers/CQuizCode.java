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
import animatefx.animation.FadeInDown;
import animatefx.animation.Jello;
import animatefx.animation.Pulse;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import mx.inbo.domain.KeyGenerator;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;
import mx.inbo.servidorrmi.ServerConector;

/**
 * Clase FXML controladora de la página Quiz Code.
 *
 * @author adolf
 */
public class CQuizCode implements Initializable {

    private static User user;

    /**
     * Asigna el usuario que juega el quiz
     *
     * @param usr Usuario jugador
     */
    public static void setUser(User usr) {
        user = usr;
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane centerPane;

    @FXML
    private ImageView background;

    @FXML
    private Circle userImage;

    @FXML
    private TextField codeField;

    @FXML
    private JFXSpinner progressIndicator;

    @FXML
    private Label waitingLabel;

    @FXML
    private JFXButton enterButton;

    private boolean playUserAnimation = true;
    private ResourceBundle bundle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        background.fitHeightProperty().bind(mainPane.heightProperty());
        background.fitWidthProperty().bind(mainPane.widthProperty());

        playIntroAnimation();

        Thumbnail thumb = user.getImage();
        Image image = new Image(new ByteArrayInputStream(thumb.getImage()));
        ImagePattern imagePattern = new ImagePattern(image);
        userImage.setFill(imagePattern);

        userImage.setOnMouseClicked((MouseEvent e) -> {
            new Jello(userImage).play();
        });

        new FadeInDown(userImage).play();

        new Pulso().start();

        bundle = rb;
    }

    /**
     * Reproduce la animación de inicio.
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
    }

    /**
     * Regresa a la página anterior.
     */
    @FXML
    public void stepBack() {
        playUserAnimation = false;
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }

    @FXML
    private void startGame() {
        String code = codeField.getText();

        boolean showEmptyMessage = false;

        if (code != null) {
            if (!code.isEmpty()) {
                playUserAnimation = false;
                codeField.disableProperty().set(true);
                enterButton.disableProperty().set(true);
                progressIndicator.setVisible(true);
                waitingLabel.setVisible(true);
                enterRoom(code);
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
            JFXDialog dialog = new JFXDialog(centerPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }
    }

    /**
     * Hilo que controla la animación de pulso de la imágen del usuario.
     */
    class Pulso extends Thread {

        int count = 1;

        @Override
        public void run() {
            while (playUserAnimation) {
                try {
                    new Pulse(userImage).play();
                    Thread.sleep(800);

                    count += 1;

                    if (count > 3) {
                        count = 0;
                        Thread.sleep(2100);
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(CQuizCode.class.getName()).log(Level.SEVERE, null, ex);
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    private void enterRoom(String code) {

        GameConector conector = new GameConector();
        conector.setInfo(code, user.getUsername());
        conector.start();

    }

    class GameConector extends Thread {

        private final String ip = ServerConector.getIP();
        private Socket socket;
        private String[] args;

        @Override
        public void run() {

            try {

                if (socket == null) {
                    socket = IO.socket("http://" + ip + ":5000");
                }

                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... os) {
                        socket.emit("addUser", (Object[]) args);
                    }

                }).on("actualizarSala", new Emitter.Listener() {
                    @Override
                    public void call(Object... os) {
                        String message = (String) os[0];

                        System.out.println(message);
                    }
                }).on("quiz", new Emitter.Listener() {
                    @Override
                    public void call(Object... os) {

                        CGameScreen.setQuizTitle(os);

                        Platform.runLater(() -> {
                            Stage actualStage = (Stage) mainPane.getScene().getWindow();
                            Loader.loadPageInCurrentStage("/mx/inbo/gui/GameScreen.fxml", "Game", actualStage);
                        });

                    }
                });

                socket.connect();

            } catch (URISyntaxException ex) {
                Logger.getLogger(CGameLobby.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public void setInfo(String code, String username) {

            args = new String[]{
                username,
                code
            };

        }

    }

}
