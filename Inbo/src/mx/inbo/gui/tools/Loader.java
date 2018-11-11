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
package mx.inbo.gui.tools;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mx.inbo.gui.controllers.CSettings;

/**
 *
 * @author adolf
 */
public class Loader{
    
    private static final String LANG_ADDRESS = "mx.inbo.lang.lang";
    
    private Loader(){}
    
    public static void loadPage(String fxmlURL, String pageTitle){
        Locale locale = Locale.getDefault();
        Stage settings = new Stage();
        try{
            Parent root = FXMLLoader.load(Loader.class.getResource(fxmlURL), ResourceBundle.getBundle(LANG_ADDRESS, locale));
            
            Scene scene = new Scene(root);
            
            settings.setScene(scene);
            settings.setTitle(pageTitle);
            settings.show();
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void loadPageClosingCurrent(String fxmlURL, String pageTitle, Stage actualStage){
        Locale locale = Locale.getDefault();
        Stage newStage = new Stage();
        try{
            Parent root = FXMLLoader.load(Loader.class.getResource(fxmlURL), ResourceBundle.getBundle(LANG_ADDRESS, locale));
            
            Scene scene = new Scene(root);
            
            newStage.setMinHeight(650);
            newStage.setMinWidth(1050);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.setTitle(pageTitle);
            newStage.show();
            actualStage.close();
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void loadPageInCurrentStage(String fxmlURL, String pageTitle, Stage actualStage){
        Locale locale = Locale.getDefault();
        try{
            Parent root = FXMLLoader.load(Loader.class.getResource(fxmlURL), ResourceBundle.getBundle(LANG_ADDRESS, locale));
            
            Scene scene = new Scene(root, actualStage.getWidth(), actualStage.getHeight());
            
            actualStage.setScene(scene);
            actualStage.setTitle(pageTitle);
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
