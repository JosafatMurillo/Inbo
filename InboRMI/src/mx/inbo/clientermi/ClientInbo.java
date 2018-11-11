/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.clientermi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;

/**
 *
 * @author BODEGA
 */
public class ClientInbo {

    private Operaciones stub;

    public void conectarServidor() {
        try {
            Registry registro = LocateRegistry.getRegistry("192.168.1.2");
            stub = (Operaciones) registro.lookup("conectarse");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClientInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarUsuario(User usuario) throws RemoteException{
        stub.agregarUsario(usuario);
    }
    
    public void cambiarContrasenia(User usuario, String contraseniaNueva) throws RemoteException{
        stub.cambiarContrasenia(usuario, contraseniaNueva);
    }
    
    public void agregarQuiz(User idUser, Quiz quiz) throws RemoteException{
        stub.agregarQuiz(idUser, quiz);
    }
    
    public void actualizarQuiz(Quiz quizNuevo) throws RemoteException {
        stub.actualizarQuiz(quizNuevo);
    }
    
    public void eliminarQuiz(Quiz quizEliminar) throws RemoteException {
        stub.eliminarQuiz(quizEliminar);
    }
    
    public void agregarPregunta(Quiz idQuiz, Question pregunta) throws RemoteException {
        stub.actualizarPregunta(pregunta);
    }
    
    public void actualizarPregunta(Question preguntaNueva) throws RemoteException {
        stub.actualizarPregunta(preguntaNueva);
    }
    
    public void eliminarPregunta(Question preguntaEliminar) throws RemoteException {
        stub.eliminarPregunta(preguntaEliminar);
    }
    
    public void agregarRespuesta(Question idQuestion, Answer respuesta) throws RemoteException {
        stub.agregarRespuesta(idQuestion, respuesta);
    }
    
    public void actualizarRespuesta(Answer respuestaNueva) throws RemoteException {
        stub.actualizarRespuesta(respuestaNueva);
    }
    
    public void eliminarRespuesta(Answer respuestaEliminar) throws RemoteException {
        stub.eliminarRespuesta(respuestaEliminar);
    }
    
    public List<Quiz> obtenerQuizzes(int idUser) throws RemoteException {
        return stub.obtenerQuizzes(idUser);
    }
    
    public List<Question> obtenerPreguntas(int idQuiz) throws RemoteException {
        return stub.obtenerPreguntas(idQuiz);
    }
    
    public List<Answer> obtenerRespuestas(int idQuestion) throws RemoteException {
        return stub.obtenerRespuestas(idQuestion);
    }
}
