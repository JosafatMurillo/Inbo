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
import com.jfoenix.controls.JFXDialog;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mx.inbo.domain.KeyGenerator;
import mx.inbo.entities.Quiz;
import mx.inbo.gui.tools.Loader;
import mx.inbo.gui.tools.Mensaje;
import mx.inbo.servidorrmi.ServerConector;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class CStartQuiz implements Initializable {

    private static Quiz quiz;

    public static void setQuiz(Quiz quizz) {
        quiz = quizz;
    }

    public static Quiz getQuiz() {
        return quiz;
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    private StackPane leftPane;

    @FXML
    private StackPane rightPane;

    @FXML
    private TextField emailField;

    @FXML
    private JFXButton enterButton;

    @FXML
    private ListView friendsList;

    private ResourceBundle bundle;
    private Transport transport = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list;
        list = FXCollections.observableArrayList();
        friendsList.setItems(list);

        bundle = rb;

        mainPane.widthProperty().addListener((objects, oldValue, newValue) -> {
            double width = (double) newValue / 2;
            leftPane.setPrefWidth(width);
            leftPane.setMaxWidth(width);
            rightPane.setPrefWidth(width);
            rightPane.setMaxWidth(width);
        });

        friendsList.setCellFactory(celdas -> new ListCell<String>() {

            @Override
            protected void updateItem(String email, boolean vacio) {
                super.updateItem(email, vacio);

                if (vacio || email == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    CInvitedFriendItem listController = new CInvitedFriendItem();
                    listController.setInformation(email);
                    setGraphic(listController.getMainPane());
                }
            }
        });

        playIntroAnimation();
    }

    /**
     * Reproduce la animación inicial.
     */
    private void playIntroAnimation() {
        new BounceInLeft(mainPane).play();
    }

    @FXML
    private void nextPage() {
        createQuizRoom();
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/GameLobby.fxml", "Lobby", actualStage);
    }

    @FXML
    private void stepBack() {
        Stage actualStage = (Stage) mainPane.getScene().getWindow();
        Loader.loadPageInCurrentStage("/mx/inbo/gui/Dashboard.fxml", "Dashboard", actualStage);
    }

    @FXML
    private void addFriend() {

        String email = emailField.getText();

        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";

        boolean showEmptyMessage = false;

        if (email != null) {
            if (!email.isEmpty()) {

                Pattern pattern = Pattern.compile(emailPattern);
                Matcher matcher = pattern.matcher(email);

                if (matcher.matches()) {
                    ObservableList<String> list = friendsList.getItems();
                    list.add(email);
                    friendsList.setItems(list);
                    emailField.setText("");
                } else {
                    Mensaje alerta = new Mensaje();
                    alerta.setHeader(bundle.getString("key.emailIssue"));
                    alerta.setBody(bundle.getString("key.wrongEmailFormat"));
                    JFXDialog dialog = new JFXDialog(leftPane, alerta, JFXDialog.DialogTransition.CENTER);

                    dialog.show();
                }
            } else {
                showEmptyMessage = true;
            }
        } else {
            showEmptyMessage = true;
        }

        if (showEmptyMessage) {
            Mensaje alerta = new Mensaje();
            alerta.setHeader(bundle.getString("key.emptyFieldTitle"));
            alerta.setBody(bundle.getString("key.emptyField"));
            JFXDialog dialog = new JFXDialog(leftPane, alerta, JFXDialog.DialogTransition.CENTER);

            dialog.show();
        }
    }

    private void createQuizRoom() {

        String ip = ServerConector.getIP();

        try {
            Socket socket = IO.socket("http://" + ip + ":5000");

            int gameKey = KeyGenerator.obtenerId();

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... os) {
                    socket.emit("crearSala", gameKey);
                }

            });
            
            socket.connect();

            sentEmails(gameKey);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CGameLobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sentEmails(int gameKey) {
        ObservableList<String> friends = friendsList.getItems();

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(props);

        Properties emailProperties = new Properties();
        try {
            emailProperties.load(this.getClass().getResourceAsStream("/mx/inbo/server/EmailServer.properties"));
        } catch (IOException ex) {
            Logger.getLogger(CStartQuiz.class.getName()).log(Level.SEVERE, null, ex);
        }

        String correoRemitente = emailProperties.getProperty("email");
        String passwordRemitente = emailProperties.getProperty("password");

        String asunto = "Invitación para jugar un quiz: Inbo";
        String mensaje = "¡Hola!<br>"
                + "Has sido invitado a participar en el quiz <b>" + quiz.getTitulo() + "</b>"
                + ", para comenzar a jugar solo debes ingresar a la aplicación de Inbo e ingresar"
                + " el siguiente código <b>" + gameKey + "</b>";

        if (friends != null) {
            friends.forEach(friend -> {

                try {
                    MimeMessage message = new MimeMessage(session);

                    message.setFrom(new InternetAddress(correoRemitente));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(friend));
                    message.setSubject(asunto);
                    message.setText(mensaje, "ISO-8859-1", "html");

                    transport = session.getTransport("smtp");
                    transport.connect(correoRemitente, passwordRemitente);
                    transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                    transport.close();
                } catch (MessagingException ex) {
                    Logger.getLogger(CStartQuiz.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
}
