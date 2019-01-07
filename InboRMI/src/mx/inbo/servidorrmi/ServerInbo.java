/**
 * Clase del aldo del servidor RMI
 *
 * De La Cruz Díaz Adolfo Ángel; Murillo Hernández Josafat
 *
 * Versión 1.0
 *
 * 19/12/2018
 *
 * Inbo
 */
package mx.inbo.servidorrmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
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
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.User;
import mx.inbo.exception.CustomException;

/**
 *
 * @author Josafat
 */
public class ServerInbo implements Operaciones {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("InboRMIPU");
    UserJpaController ujc = new UserJpaController(emf);
    QuizJpaController qjc = new QuizJpaController(emf);
    QuestionJpaController qujc = new QuestionJpaController(emf);
    AnswerJpaController ajc = new AnswerJpaController(emf);

    public static void main(String args[]) {
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingresa la ip del servidor: ");
        String ip = scanner.nextLine();
        
        System.setProperty("java.rmi.server.hostname", ip);
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

    /**
     * Función que agrega un usuario nuevo al sistema.
     *
     * @param usuario Objeto del tipo User que se va a agregar a la base de
     * datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void agregarUsario(User usuario) throws RemoteException {
        try {
            ujc.agregarUsuario(usuario);
        } catch (MessagingException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Funcion que verifica al usuario y la contraseña que se va acambiar de
     * dicho usuario.
     *
     * @param usuario Objeto del tipo User que funciona como lienzo para la
     * modificación
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void editarUsuario(User usuario) throws RemoteException {
        try {
            ujc.editarUsuario(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Función que añade un unevo quiz a la base de datos guiandose por un
     * usuario.
     *
     * @param idUser Objeto del tipo User que es fundamental para la adición de
     * un nuevo registro
     * @param quiz Objeto del tipo Quiz que es el que se va a gregar a la base
     * de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void agregarQuiz(User idUser, Quiz quiz) throws RemoteException {
        qjc.agregarQuiz(idUser, quiz);
    }

    /**
     * Función que actualiza un quiz ya existente
     *
     * @param quizNuevo Objeto del tipo Quiz que contiene la información de un
     * nuevo quiz que modifica a uno ya existente
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void actualizarQuiz(Quiz quizNuevo) throws RemoteException {
        try {
            qjc.actualizarQuiz(quizNuevo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Función que eliminar un quiz de la base de datos
     *
     * @param quizEliminar Objeto del tipo Quiz que será eliminado
     * permanentemente de la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void eliminarQuiz(Quiz quizEliminar) throws RemoteException {
        try {
            qjc.eliminarQuiz(quizEliminar);
        } catch (SQLException ex) {
            Logger.getLogger(ServerInbo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Función que añade a la base de datos una pregunta relacionada con un quiz
     * en especifico
     *
     * @param idQuiz Objeto del tipo Quiz que sirve como indice para añadirle
     * una pregunta
     * @param pregunta Objeto del tipo Question que es la pregunta a añadir
     * dentro del quiz
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void agregarPregunta(Quiz idQuiz, Question pregunta) throws RemoteException {
        qujc.agregarPregunta(idQuiz, pregunta);
    }

    /**
     * Función que actualiza una pregunta en especifico de cualquier quiz
     *
     * @param preguntaNueva Objeto del tipo Question que contiene la informacion
     * de una nueva pregunta que modifica a una ya existente
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void actualizarPregunta(Question preguntaNueva) throws RemoteException {
        qujc.actualizarPregunta(preguntaNueva);
    }

    /**
     * Funcion que eliminar una pregunta de un quiz en especifico
     *
     * @param preguntaEliminar Objeto del tipo Question que será eliminado
     * permanentemente de la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void eliminarPregunta(Question preguntaEliminar) throws RemoteException {
        qujc.eliminarPregunta(preguntaEliminar);
    }

    /**
     * Funcion que agrega una respuesta a una pregunta en especifico.
     *
     * @param idQuestion Objeto del tipo Question que sirve como indice para
     * saber a que pregunta se le añadirá la respuesta
     * @param respuesta Objeto del tipo Answer que contiene la informacion
     * necesaria para agregar una pregunta a la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void agregarRespuesta(Question idQuestion, Answer respuesta) throws RemoteException {
        ajc.agregarRespuesta(idQuestion, respuesta);
    }

    /**
     * Función que actualiza una respuesta en especifico
     *
     * @param respuestaNueva Objeto del tipo Answer que modificará al objeto que
     * esté en su lugar
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void actualizarRespuesta(Answer respuestaNueva) throws RemoteException {
        ajc.actualizarRespuesta(respuestaNueva);
    }

    /**
     * Función que elimina una respuesta de cualquier pregunta
     *
     * @param respuestaEliminar Objeto del tipo Answer que servirá como
     * indicador que que respuesta se va a eliminar de la base de datos
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public void eliminarRespuesta(Answer respuestaEliminar) throws RemoteException {
        ajc.eliminarRespuesta(respuestaEliminar);
    }

    /**
     * Función que muestra todos los quizzes de un usuario
     *
     * @param idUser Indice que sirve para saber que quizzes son de este usuario
     * @return Regresa uuna lista con todos los quizzes
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
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

    /**
     * Funcion que muestra todas las preguntas de un quiz
     *
     * @param idQuiz Indice que sirve para conocer que quiz se esta buscando
     * @return Regresa una lista con todas las preguntas del quiz
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
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

    /**
     * Funcion que muestra todas las respuestas de una pregunta en especifico
     *
     * @param idQuestion Indice que indica bajo que pregutna estan las
     * respuestas
     * @return Regresa una lista con las respuestas de la pregunta
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
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

    /**
     * Funcion validadora que verifica el inicio de sesión
     *
     * @param username Cadena de caracteres que son el nombre del usuario
     * @param contrasenia Cadena de caracteres que son la contraseña del usuario
     * @return Regresa un booleano para saber si se realizó con exito la
     * operación
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
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

    /**
     * Función que verifica la integridad de la información de inicio de sesión
     *
     * @param username Cadena de caracteres que indican el nombre del usuario
     * @throws RemoteException Excepción obligatoria por el uso de RMI
     */
    @Override
    public User obtenerUsario(String username) throws RemoteException {
        User user = ujc.obtenerUsuario(username);
        return user;
    }

}
