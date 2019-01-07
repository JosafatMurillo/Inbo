/*
 * Copyright 2019 root.
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

import animatefx.animation.BounceInUp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author root
 */
public class CGameInProgress implements Initializable {

    @FXML
    private StackPane mainPane;
    
    @FXML
    private StackPane labelPane;
    
    @FXML
    private ImageView background;
    
    @FXML
    private Label gameInProgress;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        mainPane.widthProperty().addListener((objects, oldValue, newValue) -> {
            background.setFitWidth((double) newValue);
        });
        
        mainPane.heightProperty().addListener((objects, oldValue, newValue) ->{
            background.setFitHeight((double) newValue);
        });
        
        Font customFont = Font.loadFont(CGameInProgress.class.getResource("/mx/inbo/fonts/Friendly Font.otf").toExternalForm(), 58);
        
        gameInProgress.setFont(customFont);
        
        new BounceInUp(labelPane).play();
        
    }   
    
}
