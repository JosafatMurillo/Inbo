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
import animatefx.animation.SlideInLeft;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import mx.inbo.domain.KeyGenerator;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.gui.tools.FileHelper;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;

/**
 * Clase controladora FXML del Question Maker.
 *
 * @author adolf
 */
public class CQuestionMaker implements Initializable {

    private static final String ANSWERTYPE = "Answer";

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane thumbnailPane;

    @FXML
    private ImageView thumbnail;

    @FXML
    private StackPane centerPane;

    @FXML
    private StackPane infoStack;

    @FXML
    private TextField titleField;

    @FXML
    private TextField timeLimitField;

    @FXML
    private TextField answer1Field;

    @FXML
    private TextField answer2Field;

    @FXML
    private TextField answer3Field;

    @FXML
    private TextField answer4Field;

    @FXML
    private Label pathAnswer1;

    @FXML
    private Label pathAnswer2;

    @FXML
    private Label pathAnswer3;

    @FXML
    private Label pathAnswer4;

    @FXML
    private JFXCheckBox answer1IsCorrect;

    @FXML
    private JFXCheckBox answer2IsCorrect;

    @FXML
    private JFXCheckBox answer3IsCorrect;

    @FXML
    private JFXCheckBox answer4IsCorrect;

    private Quiz quiz;
    private File imageFile;
    private FileChooser chooser;
    private ResourceBundle bundle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mainPane.widthProperty().addListener((objects, oldValue, newValue) -> {
            double width = (double) newValue / 2;
            thumbnail.setFitWidth(width - 10);
            thumbnailPane.setPrefWidth(width);
            centerPane.setPrefSize(width, mainPane.getHeight());

            double infoWidth = width - 20;
            infoStack.setPrefWidth(infoWidth);
            titleField.setPrefWidth(infoWidth / 2);
            timeLimitField.setPrefWidth(infoWidth / 2);
            answer1Field.setPrefWidth(infoWidth / 2);
            answer2Field.setPrefWidth(infoWidth / 2);
            answer3Field.setPrefWidth(infoWidth / 2);
            answer4Field.setPrefWidth(infoWidth / 2);
        });

        chooser = new FileChooser();

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        quiz = CQuizQuestions.getQuiz();

        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(),
                20, p -> Pattern.matches("\\d*", p.getText()) ? p : null);

        timeLimitField.setTextFormatter(formatter);

        bundle = rb;

        playIntroAnimation();
    }

    /**
     * Reproduce la animación de entrada
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
        new SlideInLeft(thumbnailPane).play();
    }

    /**
     * Guarda la pregunta en la lista del quiz
     */
    @FXML
    private void saveQuestion() {

        String title = titleField.getText();
        String answer1Text = answer1Field.getText();
        String answer2Text = answer2Field.getText();
        String answer3Text = answer3Field.getText();
        String answer4Text = answer4Field.getText();

        Integer limit = 20;

        try {
            limit = Integer.parseInt(timeLimitField.getText());
        } catch (InputMismatchException | NumberFormatException ex) {
            limit = 20;
            timeLimitField.setText(limit.toString());
        }

        boolean canContinue = !areThereEmptyFields(title) && isThereACorrectAnswer();

        if (canContinue) {

            Question question = new Question();

            question.setPregunta(title);
            question.setTiempo(limit);

            Thumbnail thumb = new Thumbnail();
            thumb.setType("Question");

            if (imageFile == null) {
                imageFile = new File(System.getProperty("user.dir") + "/src/mx/inbo/images/default_thumbnail_question.jpg");
            }

            String imagePath = imageFile.toURI().toString();
            int extIndex = imageFile.getName().lastIndexOf('.');
            String imageExtention = imageFile.getName().substring(extIndex + 1).toLowerCase();
            thumb.setExtention(imageExtention);

            byte[] image = FileHelper.parseFileToBytes(imageFile, imageExtention);
            thumb.setImage(image);

            int id = KeyGenerator.obtenerId();

            question.setIdQuestion(id);
            question.setImage(thumb);
            question.setImagen(imagePath);

            Answer answer1 = new Answer();

            answer1.setRespuesta(answer1Text);

            Answer answer2 = new Answer();
            answer2.setRespuesta(answer2Text);

            int idAnswer1 = KeyGenerator.obtenerId();
            int idAnswer2 = KeyGenerator.obtenerId();

            answer1 = setAnswerImage(answer1, pathAnswer1.getText());
            answer2 = setAnswerImage(answer2, pathAnswer2.getText());

            answer1.setIdAnswer(idAnswer1);
            answer2.setIdAnswer(idAnswer2);
            
            answer1.setEsCorrecta(answer1IsCorrect.isSelected());
            answer2.setEsCorrecta(answer2IsCorrect.isSelected());

            Answer answer3 = new Answer();
            answer3.setRespuesta(answer3Text);

            Answer answer4 = new Answer();
            answer4.setRespuesta(answer4Text);

            int idAnswer3 = KeyGenerator.obtenerId();
            int idAnswer4 = KeyGenerator.obtenerId();

            answer3 = setAnswerImage(answer3, pathAnswer4.getText());
            answer4 = setAnswerImage(answer4, pathAnswer4.getText());

            answer3.setIdAnswer(idAnswer3);
            answer4.setIdAnswer(idAnswer4);
            
            answer3.setEsCorrecta(answer3IsCorrect.isSelected());
            answer4.setEsCorrecta(answer4IsCorrect.isSelected());

            Collection<Answer> answers = new ArrayList<>();

            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            answers.add(answer4);

            question.setAnswerCollection(answers);

            Collection<Question> questions = quiz.getQuestionCollection();

            if (questions == null || questions.isEmpty()) {
                questions = new ArrayList<>();
            }

            questions.add(question);

            quiz.setQuestionCollection(questions);

            Stage actualStage = (Stage) mainPane.getScene().getWindow();
            quiz.setQuestionCollection(questions);
            CQuizQuestions.setQuiz(quiz);
            Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizQuestions.fxml", "Questions", actualStage);

        }
    }

    /**
     * Retorna si hay campos vacíos que son necesarios para continuar
     *
     * @param title Campo a evaluar
     * @return Si hay campos vacíos
     */
    private boolean areThereEmptyFields(String title) {
        boolean areThere = false;
        String answer1Text = answer1Field.getText();
        String answer2Text = answer2Field.getText();
        String answer3Text = answer3Field.getText();
        String answer4Text = answer4Field.getText();

        if (title.isEmpty() && answer1Text.isEmpty() && answer2Text.isEmpty()
                && answer3Text.isEmpty() && answer4Text.isEmpty()) {
            areThere = true;

            Mensaje alerta = new Mensaje();
            alerta.setHeader(bundle.getString("key.emptyFieldTitle"));
            alerta.setBody(bundle.getString("key.emptyField"));
            JFXDialog dialog = new JFXDialog(centerPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }

        return areThere;
    }

    /**
     * Retorna {@code true} si el usuario seleccionó una respuesta correcta, de
     * lo contrario regresa {@code false}
     *
     * @return Si se seleccionó una respuesta correcta
     */
    private boolean isThereACorrectAnswer() {

        boolean isThere = true;

        if (answer1IsCorrect.isSelected() == false && answer2IsCorrect.isSelected() == false
                && answer3IsCorrect.isSelected() == false && answer4IsCorrect.isSelected() == false) {
            isThere = false;

            Mensaje alerta = new Mensaje();
            alerta.setHeader(bundle.getString("key.theresNoCorrectAnswer"));
            alerta.setBody(bundle.getString("key.selectACorrectAnswer"));
            JFXDialog dialog = new JFXDialog(centerPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }

        return isThere;
    }

    /**
     * Regresa a la página anterior
     */
    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizQuestions.fxml", "Questions", actualStage);
    }

    /**
     * Cambia la imágen de portada
     */
    @FXML
    private void changeImage() {

        Stage actualStage = (Stage) mainPane.getScene().getWindow();

        imageFile = chooser.showOpenDialog(actualStage);

        Image image = new Image(imageFile.toURI().toString());

        thumbnail.setImage(image);
    }

    /**
     * Obtiene e inicializa las imágenes que le fueron asignadas a las
     * preguntas.
     *
     * @param answer Pregunta a asignar portada.
     * @param path Dirección de la imágen.
     * @return Respuesta con portada.
     */
    private Answer setAnswerImage(Answer answer, String path) {

        File image = null;
        Thumbnail thumb = null;

        if (!path.isEmpty()) {
            int extIndex = path.lastIndexOf('.');
            String extension = path.substring(extIndex + 1).toLowerCase();
            image = new File(path);
            thumb = new Thumbnail();
            byte[] bytes = FileHelper.parseFileToBytes(image, extension);
            thumb.setType(ANSWERTYPE);
            thumb.setImage(bytes);
            thumb.setExtention(extension);
            answer.setImage(thumb);
        }

        return answer;
    }

}
