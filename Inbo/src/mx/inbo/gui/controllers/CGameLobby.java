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
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.domain.KeyGenerator;
import mx.inbo.gui.tools.Loader;
import mx.inbo.servidorrmi.ServerConector;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CGameLobby implements Initializable {

    private static int gameKey = 0;
    
    public static void setGameKey(int key){
        gameKey = key;
    }
    
    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane thumbnailPane;

    @FXML
    private StackPane rightPane;

    @FXML
    private ImageView thumbnail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mainPane.widthProperty().addListener((objects, oldValue, newValue) -> {
            double width = (double) newValue / 2;
            thumbnail.setFitWidth(width - 10);
            thumbnailPane.setPrefWidth(width);
            rightPane.setPrefWidth(width);
        });

        playIntroAnimation();

    }

    /**
     * Reproduce la animación inicial.
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
    }

    @FXML
    private void nextPage() {
        
        String ip = ServerConector.getIP();

        try {
            Socket socket = IO.socket("http://" + ip + ":5000");

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... os) {
                    socket.emit("iniciarQuiz", gameKey);
                }

            });
            
            socket.connect();
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(CGameLobby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/GameInProgress.fxml", "Game", actualStage);
    }

    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }
}
