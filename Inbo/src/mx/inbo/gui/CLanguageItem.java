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
package mx.inbo.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import mx.inbo.gui.controllers.CQuestionListItem;

/**
 *
 * @author root
 */
public class CLanguageItem {

    @FXML
    private StackPane itemPane;

    @FXML
    private Circle languageIcon;

    @FXML
    private Label languageLabel;

    public CLanguageItem() {
        FXMLLoader loader = new FXMLLoader(CQuestionListItem.class.getClass().getResource("/mx/inbo/gui/LanguageItem.fxml"));
        loader.setController(this);

        try {
            itemPane = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setInformation(String info[]) {
        if (info.length == 3) {
            String languageName = info[0];
            String countryFlag = info[1];
            
            languageLabel.setText(languageName);
            
            Image image = new Image(countryFlag);
            
            ImagePattern imagePattern = new ImagePattern(image);
            
            languageIcon.setFill(imagePattern);
        }
    }

    public StackPane getMainPane() {
        return this.itemPane;
    }
}
