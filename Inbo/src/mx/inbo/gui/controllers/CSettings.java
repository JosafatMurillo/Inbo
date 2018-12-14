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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mx.inbo.entities.User;
import mx.inbo.gui.tools.Loader;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CSettings implements Initializable {
    
    private static User user;
    
    public static void setUser(User usr){
        user = usr;
    }
    
    @FXML
    private BorderPane mainPane;
    
    @FXML
    private JFXButton changeImageButton;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField currentPasswordField;
    
    @FXML
    private PasswordField newPasswordField;
    
    @FXML
    private PasswordField passwordVerificationField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playIntroAnimation();
        
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
    }
    
    private void playIntroAnimation(){
        new BounceInLeft(mainPane).play();
    }
    
    @FXML
    public void stepBack(){
        Stage actualStage = (Stage) changeImageButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }
    
    @FXML
    private void save(){
        String username = usernameField.getText();
        String email = emailField.getText();
        
        if((username != null || !username.isEmpty()) && (email != null || !email.isEmpty())){
            
            user.setUsername(username);
            user.setEmail(email);
            
            String newPassword = newPasswordField.getText();
            
            if(newPassword != null || !newPassword.isEmpty()){
                
                
                
            }
            
        }
    }
    
}
