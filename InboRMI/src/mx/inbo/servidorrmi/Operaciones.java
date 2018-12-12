/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.servidorrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;

/**
 *
 * @author BODEGA
 */
public interface Operaciones extends Remote{
    
    public void agregarUsario(User usuario) throws RemoteException;
    
    public User obtenerUsario(String username) throws RemoteException;
    
    public void cambiarContrasenia(User usuario, String contraseniaNueva)
            throws RemoteException;
    
    public void agregarQuiz(User idUser, Quiz quiz) throws RemoteException;
    
    public void actualizarQuiz(Quiz quizNuevo) throws RemoteException;
    
    public void eliminarQuiz(Quiz quizEliminar)throws RemoteException;
    
    public List<Quiz> obtenerQuizzes(User idUser) throws RemoteException;
    
    public void agregarPregunta(Quiz idQuiz, Question pregunta)throws RemoteException;
    
    public void actualizarPregunta(Question preguntaNueva)throws RemoteException;
    
    public void eliminarPregunta(Question preguntaEliminar) throws RemoteException;
    
    public List<Question> obtenerPreguntas(Quiz idQuiz) throws RemoteException;
    
    public void agregarRespuesta(Question idQuestion, Answer respuesta)throws RemoteException;
    
    public void actualizarRespuesta(Answer respuestaNueva)throws RemoteException;
    
    public void eliminarRespuesta(Answer respuestaEliminar) throws RemoteException;
    
    public List<Answer> obtenerRespuestas(Question idQuestion) throws RemoteException;
    
    public boolean validarLogin(String username, String contrasenia) throws RemoteException;
}
