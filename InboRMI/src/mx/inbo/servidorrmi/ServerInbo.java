/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.servidorrmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import mx.inbo.controllers.AnswerJpaController;
import mx.inbo.controllers.QuestionJpaController;
import mx.inbo.controllers.QuizJpaController;
import mx.inbo.controllers.UserJpaController;
import mx.inbo.controllers.exceptions.NonexistentEntityException;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;
import mx.inbo.exception.CustomException;

/**
 *
 * @author BODEGA
 */
public class ServerInbo implements Operaciones{
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("InboRMIPU");
    UserJpaController ujc = new UserJpaController(emf);
    QuizJpaController qjc = new QuizJpaController(emf);
    QuestionJpaController qujc = new QuestionJpaController(emf);
    AnswerJpaController ajc = new AnswerJpaController(emf);

    public static void main(String args[]){
        ServerInbo server = new ServerInbo();
        
        try {
            Operaciones stub = (Operaciones) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("servidorInbo", stub);
            System.out.println("Server is listening...");
        } catch (RemoteException | AlreadyBoundException ex) {
            ex.getMessage();
        }
    }
    
    @Override
    public void agregarUsario(User usuario) throws RemoteException {
        try {
            ujc.agregarUsuario(usuario);
        } catch (MessagingException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void editarUsuario(User usuario) throws RemoteException {
        try {
            ujc.editarUsuario(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void agregarQuiz(User idUser, Quiz quiz) throws RemoteException {
        qjc.agregarQuiz(idUser, quiz);
    }

    @Override
    public void actualizarQuiz(Quiz quizNuevo) throws RemoteException {
        try {
            qjc.actualizarQuiz(quizNuevo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminarQuiz(Quiz quizEliminar) throws RemoteException {
        try {
            qjc.eliminarQuiz(quizEliminar);
        } catch (SQLException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void agregarPregunta(Quiz idQuiz, Question pregunta) throws RemoteException {
        qujc.agregarPregunta(idQuiz, pregunta);
    }

    @Override
    public void actualizarPregunta(Question preguntaNueva) throws RemoteException {
        qujc.actualizarPregunta(preguntaNueva);
    }

    @Override
    public void eliminarPregunta(Question preguntaEliminar) throws RemoteException {
        qujc.eliminarPregunta(preguntaEliminar);
    }

    @Override
    public void agregarRespuesta(Question idQuestion, Answer respuesta) throws RemoteException {
        ajc.agregarRespuesta(idQuestion, respuesta);
    }

    @Override
    public void actualizarRespuesta(Answer respuestaNueva) throws RemoteException {
        ajc.actualizarRespuesta(respuestaNueva);
    }

    @Override
    public void eliminarRespuesta(Answer respuestaEliminar) throws RemoteException {
        ajc.eliminarRespuesta(respuestaEliminar);
    }

    @Override
    public List<Quiz> obtenerQuizzes(User idUser) throws RemoteException {
        List<Quiz> quizzes = null;
        try {
            quizzes = qjc.obtenerQuizzes(idUser);
        } catch (SQLException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quizzes;
    }

    @Override
    public List<Question> obtenerPreguntas(Quiz idQuiz) throws RemoteException {
        List<Question> preguntas = null;
        try {
            qujc.obtenerPreguntas(idQuiz);
        } catch (SQLException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preguntas;
    }

    @Override
    public List<Answer> obtenerRespuestas(Question idQuestion) throws RemoteException {
        List<Answer> respuestas = null;
        try {
            respuestas = ajc.obtenerRespuestas(idQuestion);
        } catch (SQLException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuestas;
    }

    @Override
    public boolean validarLogin(String username, String contrasenia) throws RemoteException {
        boolean exists = true;
        try {
            ujc.validarLogin(username, contrasenia);
        } catch (SQLException | CustomException | NoResultException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
            exists = false;
        }
        
        return exists;
    }

    @Override
    public User obtenerUsario(String username) throws RemoteException {
        User user = ujc.obtenerUsuario(username);
        return user;
    }
    
}
