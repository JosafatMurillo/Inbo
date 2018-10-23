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
package mx.inbo.gui;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CDashboard implements Initializable {

    @FXML
    private JFXButton createQuizButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void loadSettings(){
        Stage actualStage = (Stage) createQuizButton.getScene().getWindow();
        Locale locale = Locale.getDefault();
        Stage settings = new Stage();
        try{
            Parent root = FXMLLoader.load(this.getClass().getResource("/mx/inbo/gui/Settings.fxml"), ResourceBundle.getBundle("mx.inbo.lang.lang", locale));
            
            Scene scene = new Scene(root, actualStage.getWidth(), actualStage.getHeight());
            
            settings.setScene(scene);
            settings.setMaximized(true);
            settings.setTitle("Settings");
            settings.show();
            actualStage.close();
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
