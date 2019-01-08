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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.gui.tools.Loader;
import mx.inbo.servidorrmi.ServerConector;
import mx.inbo.servidorrmi.Operaciones;

/**
 * Clase controladora FXML de la página Quiz Questions.
 *
 * @author adolf
 */
public class CQuizQuestions implements Initializable {

    private static Quiz quiz;
    private static boolean isEditing = false;

    public static void setQuiz(Quiz quizz) {
        quiz = quizz;
    }

    public static Quiz getQuiz() {
        return quiz;
    }

    public static void toCreate() {
        isEditing = false;
    }

    public static void toEdit() {
        isEditing = true;
    }

    public static boolean gonnaEdit() {
        return isEditing;
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane titlePane;

    @FXML
    private JFXButton backButton;

    @FXML
    private ListView questionsList;

    private Collection<Question> questions;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        playIntroAnimation();

        questions = quiz.getQuestionCollection();

        if (questions != null) {
            ObservableList<Question> observableQuestion = FXCollections.observableArrayList(questions);
            questionsList.setItems(observableQuestion);

            questionsList.setCellFactory(celdas -> new ListCell<Question>() {

                @Override
                protected void updateItem(Question question, boolean vacio) {
                    super.updateItem(question, vacio);

                    if (vacio || question == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        CQuestionListItem listController = new CQuestionListItem();
                        listController.setInformation(question);
                        setGraphic(listController.getBox());
                    }
                }
            });
        }

    }

    /**
     * Reproduce la animación de entrada.
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
    }

    /**
     * Regresa a la página anterior.
     */
    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuizMaker.fxml", "New Quiz", actualStage);
    }

    /**
     * Carga la página Question Maker para añadir una nueva pregunta.
     */
    @FXML
    private void loadQuestionMaker() {
        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/QuestionMaker.fxml", "New Question", actualStage);
    }

    /**
     * Registra la información del Quiz en el servidor.
     */
    @FXML
    private void saveQuiz() {

        Operaciones stub = ServerConector.getStub();

        if (!isEditing) {
            Collection<Question> emptyQuestions = new ArrayList<>();

            quiz.setQuestionCollection(emptyQuestions);

            try {
                stub.agregarQuiz(CDashboard.getUser(), quiz);
            } catch (RemoteException ex) {
                Logger.getLogger(CQuizQuestions.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (questions != null) {
                questions.forEach(question -> {
                    Collection<Answer> answers = question.getAnswerCollection();
                    Collection<Answer> emptyAnswers = new ArrayList<>();
                    question.setAnswerCollection(emptyAnswers);

                    try {
                        stub.agregarPregunta(quiz, question);
                    } catch (RemoteException ex) {
                        Logger.getLogger(CQuizQuestions.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    answers.forEach(answer -> {
                        try {
                            stub.agregarRespuesta(question, answer);
                        } catch (RemoteException ex) {
                            Logger.getLogger(CQuizQuestions.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                });
            }

            List<Quiz> quizzes = CDashboard.getQuizzes();

            if (quizzes == null) {
                quizzes = new ArrayList<>();
            }

            quizzes.add(quiz);

            CDashboard.setQuizzes(quizzes);
        } else {
            try {
                stub.actualizarQuiz(quiz);
            } catch (RemoteException ex) {
                Logger.getLogger(CQuizQuestions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Stage actualStage = (Stage) backButton.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }

}
