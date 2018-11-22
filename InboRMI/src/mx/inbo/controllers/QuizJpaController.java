/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mx.inbo.entities.User;
import mx.inbo.entities.Question;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import mx.inbo.controllers.exceptions.IllegalOrphanException;
import mx.inbo.controllers.exceptions.NonexistentEntityException;
import mx.inbo.datasource.DataBaseInbo;
import mx.inbo.domain.FileSaver;
import mx.inbo.domain.KeyGenerator;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Quiz;

/**
 *
 * @author BODEGA
 */
public class QuizJpaController implements Serializable {

    public QuizJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Quiz quiz) {
        if (quiz.getQuestionCollection() == null) {
            quiz.setQuestionCollection(new ArrayList<Question>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User idUser = quiz.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getIdUser());
                quiz.setIdUser(idUser);
            }
            Collection<Question> attachedQuestionCollection = new ArrayList<Question>();
            for (Question questionCollectionQuestionToAttach : quiz.getQuestionCollection()) {
                questionCollectionQuestionToAttach = em.getReference(questionCollectionQuestionToAttach.getClass(), questionCollectionQuestionToAttach.getIdQuestion());
                attachedQuestionCollection.add(questionCollectionQuestionToAttach);
            }
            quiz.setQuestionCollection(attachedQuestionCollection);
            em.persist(quiz);
            if (idUser != null) {
                idUser.getQuizCollection().add(quiz);
                idUser = em.merge(idUser);
            }
            for (Question questionCollectionQuestion : quiz.getQuestionCollection()) {
                Quiz oldIdQuizOfQuestionCollectionQuestion = questionCollectionQuestion.getIdQuiz();
                questionCollectionQuestion.setIdQuiz(quiz);
                questionCollectionQuestion = em.merge(questionCollectionQuestion);
                if (oldIdQuizOfQuestionCollectionQuestion != null) {
                    oldIdQuizOfQuestionCollectionQuestion.getQuestionCollection().remove(questionCollectionQuestion);
                    oldIdQuizOfQuestionCollectionQuestion = em.merge(oldIdQuizOfQuestionCollectionQuestion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Quiz quiz) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Quiz persistentQuiz = em.find(Quiz.class, quiz.getIdQuiz());
            User idUserOld = persistentQuiz.getIdUser();
            User idUserNew = quiz.getIdUser();
            Collection<Question> questionCollectionOld = persistentQuiz.getQuestionCollection();
            Collection<Question> questionCollectionNew = quiz.getQuestionCollection();
            List<String> illegalOrphanMessages = null;
            for (Question questionCollectionOldQuestion : questionCollectionOld) {
                if (!questionCollectionNew.contains(questionCollectionOldQuestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Question " + questionCollectionOldQuestion + " since its idQuiz field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getIdUser());
                quiz.setIdUser(idUserNew);
            }
            Collection<Question> attachedQuestionCollectionNew = new ArrayList<Question>();
            for (Question questionCollectionNewQuestionToAttach : questionCollectionNew) {
                questionCollectionNewQuestionToAttach = em.getReference(questionCollectionNewQuestionToAttach.getClass(), questionCollectionNewQuestionToAttach.getIdQuestion());
                attachedQuestionCollectionNew.add(questionCollectionNewQuestionToAttach);
            }
            questionCollectionNew = attachedQuestionCollectionNew;
            quiz.setQuestionCollection(questionCollectionNew);
            quiz = em.merge(quiz);
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getQuizCollection().remove(quiz);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getQuizCollection().add(quiz);
                idUserNew = em.merge(idUserNew);
            }
            for (Question questionCollectionNewQuestion : questionCollectionNew) {
                if (!questionCollectionOld.contains(questionCollectionNewQuestion)) {
                    Quiz oldIdQuizOfQuestionCollectionNewQuestion = questionCollectionNewQuestion.getIdQuiz();
                    questionCollectionNewQuestion.setIdQuiz(quiz);
                    questionCollectionNewQuestion = em.merge(questionCollectionNewQuestion);
                    if (oldIdQuizOfQuestionCollectionNewQuestion != null && !oldIdQuizOfQuestionCollectionNewQuestion.equals(quiz)) {
                        oldIdQuizOfQuestionCollectionNewQuestion.getQuestionCollection().remove(questionCollectionNewQuestion);
                        oldIdQuizOfQuestionCollectionNewQuestion = em.merge(oldIdQuizOfQuestionCollectionNewQuestion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = quiz.getIdQuiz();
                if (findQuiz(id) == null) {
                    throw new NonexistentEntityException("The quiz with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Quiz quiz;
            try {
                quiz = em.getReference(Quiz.class, id);
                quiz.getIdQuiz();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The quiz with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Question> questionCollectionOrphanCheck = quiz.getQuestionCollection();
            for (Question questionCollectionOrphanCheckQuestion : questionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Quiz (" + quiz + ") cannot be destroyed since the Question " + questionCollectionOrphanCheckQuestion + " in its questionCollection field has a non-nullable idQuiz field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User idUser = quiz.getIdUser();
            if (idUser != null) {
                idUser.getQuizCollection().remove(quiz);
                idUser = em.merge(idUser);
            }
            em.remove(quiz);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Quiz> findQuizEntities() {
        return findQuizEntities(true, -1, -1);
    }

    public List<Quiz> findQuizEntities(int maxResults, int firstResult) {
        return findQuizEntities(false, maxResults, firstResult);
    }

    private List<Quiz> findQuizEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Quiz.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Quiz findQuiz(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Quiz.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuizCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Quiz> rt = cq.from(Quiz.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void agregarQuiz(User idUser, Quiz quiz) {

        int id = KeyGenerator.obtenerId();
        quiz.setIdQuiz(id);
        
        Thumbnail thumb = quiz.getImage();
        
        String filePath = FileSaver.createFilePath(thumb.getType(), quiz.getIdQuiz(), idUser.getUsername(), thumb.getExtention());
        
        FileSaver.saveFile(thumb, filePath);
        
        quiz.setImagen(filePath);
        quiz.setIdUser(idUser);
        create(quiz);
    }

    public void actualizarQuiz(Quiz quizNuevo) throws NonexistentEntityException {
        try {
            edit(quizNuevo);
        } catch (NonexistentEntityException ex) {
            throw new NonexistentEntityException("En este momento no es posible actualizar el quiz");
        } catch (Exception ex) {
            Logger.getLogger(QuizJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarQuiz(Quiz quizEliminar) throws SQLException {
        DataBaseInbo conexion = new DataBaseInbo();
        QuestionJpaController ajc = new QuestionJpaController(emf);

        if (conexion.MySQLConnect() == null) {
            throw new SQLException("Conexión fallida, intentelo más tarde");
        }

        ajc.eliminarTodasPreguntas(quizEliminar.getIdQuiz());
    }

    public List<Quiz> obtenerQuizzes(int idUser) throws SQLException {
        List<Quiz> quizzes;
        EntityManager em = getEntityManager();
        DataBaseInbo conexion = new DataBaseInbo();

        if (conexion.MySQLConnect() == null) {
            throw new SQLException("Conexión fallida, intentelo más tarde");
        }

        String queryName = "Quiz.findByIdUser";
        Query query = em.createNamedQuery(queryName);
        query.setParameter("idUser", idUser);
        try {
            quizzes = (List<Quiz>) query.getResultList();
        } catch (NoResultException ex) {
            throw new NoResultException("Usuario no encontrado");
        }
        return quizzes;
    }

}
