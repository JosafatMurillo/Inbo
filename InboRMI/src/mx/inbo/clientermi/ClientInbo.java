/**
 * Clase del lado del cliente RMI
 * 
 * De La Cruz Díaz Adolfo Ángel; Murillo Hernández Josafat
 * 
 * Versión 1.0
 * 
 * 19/12/2018
 * 
 * Inbo
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
 * Esta clase es la que hace la conexión con el servidor RMI para todo el apartado
 * de adición de datos sobre el quiz, los usuarios y la posibilidad de juego.
 * 
 * @author Josafat
 */
public class ClientInbo {

    private Operaciones stub;

    /**
     * Funcion que sirve como iniciadora del servicio, verifica la conexión con el servidor.
     */
    public void conectarServidor() {
        try {
            Registry registro = LocateRegistry.getRegistry("192.168.1.2");
            stub = (Operaciones) registro.lookup("conectarse");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClientInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Función que agrega un usuario nuevo al sistema.
     * @param usuario Objeto del tipo User que se va a agregar a la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void agregarUsuario(User usuario) throws RemoteException{
        stub.agregarUsario(usuario);
    }
    
    /**
     * Funcion que verifica al usuario y la contraseña que se va acambiar de dicho usuario.
     * @param usuario Objeto del tipo User que funciona como lienzo para la modificación
     * @param contraseniaNueva Cade de caracteres que simboliza la contraseña que se quiere actualizar.
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void cambiarContrasenia(User usuario, String contraseniaNueva) throws RemoteException{
        stub.cambiarContrasenia(usuario, contraseniaNueva);
    }
    
    /**
     * Función que añade un unevo quiz a la base de datos guiandose por un usuario.
     * @param idUser Objeto del tipo User que es fundamental para la adición de un nuevo registro
     * @param quiz Objeto del tipo Quiz que es el que se va a gregar a la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void agregarQuiz(User idUser, Quiz quiz) throws RemoteException{
        stub.agregarQuiz(idUser, quiz);
    }
    
    /**
     * Función que actualiza un quiz ya existente
     * @param quizNuevo Objeto del tipo Quiz que contiene la información de un nuevo quiz que modifica a uno ya existente
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void actualizarQuiz(Quiz quizNuevo) throws RemoteException {
        stub.actualizarQuiz(quizNuevo);
    }
    
    /**
     * Función que eliminar un quiz de la base de datos
     * @param quizEliminar Objeto del tipo Quiz que será eliminado permanentemente de la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void eliminarQuiz(Quiz quizEliminar) throws RemoteException {
        stub.eliminarQuiz(quizEliminar);
    }
    
    /**
     * Función que añade a la base de datos una pregunta relacionada con un quiz en especifico
     * @param idQuiz Objeto del tipo Quiz que sirve como indice para añadirle una pregunta
     * @param pregunta Objeto del tipo Question que es la pregunta a añadir dentro del quiz
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void agregarPregunta(Quiz idQuiz, Question pregunta) throws RemoteException {
        stub.actualizarPregunta(pregunta);
    }
    
    /**
     * Función que actualiza una pregunta en especifico de cualquier quiz
     * @param preguntaNueva Objeto del tipo Question que contiene la informacion de una nueva pregunta que modifica a una ya existente
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void actualizarPregunta(Question preguntaNueva) throws RemoteException {
        stub.actualizarPregunta(preguntaNueva);
    }
    
    /**
     * Funcion que eliminar una pregunta de un quiz en especifico
     * @param preguntaEliminar Objeto del tipo Question que será eliminado permanentemente de la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void eliminarPregunta(Question preguntaEliminar) throws RemoteException {
        stub.eliminarPregunta(preguntaEliminar);
    }
    
    /**
     * Funcion que agrega una respuesta a una pregunta en especifico.
     * @param idQuestion Objeto del tipo Question que sirve como indice para saber a que pregunta se le añadirá la respuesta
     * @param respuesta Objeto del tipo Answer que contiene la informacion necesaria para agregar una pregunta a la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void agregarRespuesta(Question idQuestion, Answer respuesta) throws RemoteException {
        stub.agregarRespuesta(idQuestion, respuesta);
    }
    
    /**
     * Función que actualiza una respuesta en especifico
     * @param respuestaNueva Objeto del tipo Answer que modificará al objeto que esté en su lugar
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void actualizarRespuesta(Answer respuestaNueva) throws RemoteException {
        stub.actualizarRespuesta(respuestaNueva);
    }
    
    /**
     * Función que elimina una respuesta de cualquier pregunta
     * @param respuestaEliminar Objeto del tipo Answer que servirá como indicador que que respuesta se va a eliminar de la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void eliminarRespuesta(Answer respuestaEliminar) throws RemoteException {
        stub.eliminarRespuesta(respuestaEliminar);
    }
    
    /**
     * Función que muestra todos los quizzes de un usuario
     * @param idUser Indice que sirve para saber que quizzes son de este usuario
     * @return Regresa uuna lista con todos los quizzes
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public List<Quiz> obtenerQuizzes(int idUser) throws RemoteException {
        return stub.obtenerQuizzes(idUser);
    }
    
    /**
     * Funcion que muestra todas las preguntas de un quiz
     * @param idQuiz Indice que sirve para conocer que quiz se esta buscando
     * @return Regresa una lista con todas las preguntas del quiz
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public List<Question> obtenerPreguntas(int idQuiz) throws RemoteException {
        return stub.obtenerPreguntas(idQuiz);
    }
    
    /**
     * Funcion que muestra todas las respuestas de una pregunta en especifico
     * @param idQuestion Indice que indica bajo que pregutna estan las respuestas
     * @return Regresa una lista con las respuestas de la pregunta
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public List<Answer> obtenerRespuestas(int idQuestion) throws RemoteException {
        return stub.obtenerRespuestas(idQuestion);
    }
    
    /**
     * Función que verifica la integridad de la información de inicio de sesión
     * @param username Cadena de caracteres que indican el nombre del usuario
     * @param contrasenia Cadena de caracteres que indican la contraseña ingresada.
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    public void validarLogin(String username, String contrasenia) throws RemoteException{
        stub.validarLogin(username, contrasenia);
    }
}
