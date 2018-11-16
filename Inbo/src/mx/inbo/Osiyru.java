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
package mx.inbo;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Clase principal que lanza la aplicación, ya sea la página principal o la
 * página de inicio de sesión.
 *
 * @author adolf
 */
public class Osiyru extends Application {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    /**
     * Inicializa la página inicial que se muestra al usuario.
     *
     * @param primaryStage Stage incial
     * @throws IOException Lanza la excepción cuando no ecuentra la página o
     * propiedades necesarias para inicializar la aplicación.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Locale locale = Locale.getDefault();
        Parent root = FXMLLoader.load(Osiyru.class.getResource("/mx/inbo/gui/Login.fxml"), ResourceBundle.getBundle("mx.inbo.lang.lang", locale));

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });

        Scene scene = new Scene(root);

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Método inicial
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
