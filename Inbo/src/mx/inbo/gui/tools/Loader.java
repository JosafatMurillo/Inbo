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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import mx.inbo.LanguageController;
import mx.inbo.gui.controllers.CSettings;

/**
 * Clase que carga las pantallas en los stages.
 *
 * @author adolf
 */
public class Loader {

    private static final String LANG_ADDRESS = "mx.inbo.lang.lang";

    private static double xOffset = 0;
    private static double yOffset = 0;

    private Loader() {
    }

    /**
     * Carga una página que no puede aumentar o diminuir su tanaño.
     *
     * @param fxmlURL URL del archivo fxml
     * @param pageTitle Titulo de la página
     */
    public static void loadNonResizablePage(String fxmlURL, String pageTitle) {
        Locale locale = Locale.forLanguageTag(LanguageController.getLanguageTag());
        Stage newStage = new Stage();
        try {
            Parent root = FXMLLoader.load(Loader.class.getResource(fxmlURL), ResourceBundle.getBundle(LANG_ADDRESS, locale));

            Scene scene = new Scene(root);

            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.setTitle(pageTitle);
            newStage.show();
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga una pantalla en un nuevo stage y cierra la página que le es
     * transferida.
     *
     * @param fxmlURL URL del archivo fxml
     * @param pageTitle Titulo de la página
     * @param actualStage Stage actual a cerrar
     */
    public static void loadUndecoratedPageClosingCurrent(String fxmlURL, String pageTitle, Stage actualStage) {
        Locale locale = Locale.forLanguageTag(LanguageController.getLanguageTag());
        Stage newStage = new Stage();

        try {
            Parent root = FXMLLoader.load(Loader.class.getResource(fxmlURL), ResourceBundle.getBundle(LANG_ADDRESS, locale));

            root.setOnMousePressed((MouseEvent event) -> {
                xOffset = newStage.getX() - event.getScreenX();
                yOffset = newStage.getY() - event.getScreenY();
            });

            root.setOnMouseDragged((MouseEvent event) -> {
                newStage.setX(event.getScreenX() + xOffset);
                newStage.setY(event.getScreenY() + yOffset);
            });

            Scene scene = new Scene(root);

            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.setScene(scene);
            newStage.setTitle(pageTitle);
            newStage.show();
            actualStage.close();
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga una nueva pantalla en el stage actual.
     *
     * @param fxmlURL URL del archivo fxml
     * @param pageTitle Título de la página
     * @param actualStage Stage actual
     */
    public static void loadPageInCurrentStage(String fxmlURL, String pageTitle, Stage actualStage) {
        Locale locale = Locale.forLanguageTag(LanguageController.getLanguageTag());
        try {
            Parent root = FXMLLoader.load(Loader.class.getResource(fxmlURL), ResourceBundle.getBundle(LANG_ADDRESS, locale));

            Scene scene = new Scene(root, actualStage.getWidth(), actualStage.getHeight());

            actualStage.setScene(scene);
            actualStage.setOnCloseRequest((WindowEvent t) -> {
                Platform.exit();
                System.exit(0);
            });
            actualStage.setTitle(pageTitle);
        } catch (IOException ex) {
            Logger.getLogger(CSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
