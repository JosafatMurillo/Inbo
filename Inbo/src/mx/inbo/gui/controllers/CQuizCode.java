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
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CQuizCode implements Initializable {

    private static User user;

    public static void setUser(User usr) {
        user = usr;
    }
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private ImageView background;
    
    @FXML
    private Circle userImage;
    
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
    }
    
    private void playIntroAnimation(){
        new BounceInLeft(mainPane).play();
    }

    @FXML
    public void stepBack(){
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }
    
    class Pulso extends Thread {
        
        int count = 1;
        
        @Override
        public void run(){
            while(true){
                try {
                    new Pulse(userImage).play();
                    Thread.sleep(800);
                    
                    count += 1;
                    
                    if(count > 3){
                        count = 0;
                        Thread.sleep(2100);
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(CQuizCode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
}
