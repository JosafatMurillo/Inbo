/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.controllers;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mx.inbo.entities.Quiz;
import mx.inbo.entities.Answer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import mx.inbo.controllers.exceptions.IllegalOrphanException;
import mx.inbo.controllers.exceptions.NonexistentEntityException;
import mx.inbo.datasource.DataBaseInbo;
import mx.inbo.domain.FileHelper;
import mx.inbo.domain.FileSaver;
import mx.inbo.domain.KeyGenerator;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Question;

/**
 *
 * @author BODEGA
 */
public class QuestionJpaController implements Serializable {

    public QuestionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Question question) {
        if (question.getAnswerCollection() == null) {
            question.setAnswerCollection(new ArrayList<Answer>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Quiz idQuiz = question.getIdQuiz();
            if (idQuiz != null) {
                idQuiz = em.getReference(idQuiz.getClass(), idQuiz.getIdQuiz());
                question.setIdQuiz(idQuiz);
            }
            Collection<Answer> attachedAnswerCollection = new ArrayList<Answer>();
            for (Answer answerCollectionAnswerToAttach : question.getAnswerCollection()) {
                answerCollectionAnswerToAttach = em.getReference(answerCollectionAnswerToAttach.getClass(), answerCollectionAnswerToAttach.getIdAnswer());
                attachedAnswerCollection.add(answerCollectionAnswerToAttach);
            }
            question.setAnswerCollection(attachedAnswerCollection);
            em.persist(question);
            if (idQuiz != null) {
                idQuiz.getQuestionCollection().add(question);
                idQuiz = em.merge(idQuiz);
            }
            for (Answer answerCollectionAnswer : question.getAnswerCollection()) {
                Question oldIdQuestionOfAnswerCollectionAnswer = answerCollectionAnswer.getIdQuestion();
                answerCollectionAnswer.setIdQuestion(question);
                answerCollectionAnswer = em.merge(answerCollectionAnswer);
                if (oldIdQuestionOfAnswerCollectionAnswer != null) {
                    oldIdQuestionOfAnswerCollectionAnswer.getAnswerCollection().remove(answerCollectionAnswer);
                    oldIdQuestionOfAnswerCollectionAnswer = em.merge(oldIdQuestionOfAnswerCollectionAnswer);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Question question) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Question persistentQuestion = em.find(Question.class, question.getIdQuestion());
            Quiz idQuizOld = persistentQuestion.getIdQuiz();
            Quiz idQuizNew = question.getIdQuiz();
            Collection<Answer> answerCollectionOld = persistentQuestion.getAnswerCollection();
            Collection<Answer> answerCollectionNew = question.getAnswerCollection();
            List<String> illegalOrphanMessages = null;
            for (Answer answerCollectionOldAnswer : answerCollectionOld) {
                if (!answerCollectionNew.contains(answerCollectionOldAnswer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Answer " + answerCollectionOldAnswer + " since its idQuestion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idQuizNew != null) {
                idQuizNew = em.getReference(idQuizNew.getClass(), idQuizNew.getIdQuiz());
                question.setIdQuiz(idQuizNew);
            }
            Collection<Answer> attachedAnswerCollectionNew = new ArrayList<Answer>();
            for (Answer answerCollectionNewAnswerToAttach : answerCollectionNew) {
                answerCollectionNewAnswerToAttach = em.getReference(answerCollectionNewAnswerToAttach.getClass(), answerCollectionNewAnswerToAttach.getIdAnswer());
                attachedAnswerCollectionNew.add(answerCollectionNewAnswerToAttach);
            }
            answerCollectionNew = attachedAnswerCollectionNew;
            question.setAnswerCollection(answerCollectionNew);
            question = em.merge(question);
            if (idQuizOld != null && !idQuizOld.equals(idQuizNew)) {
                idQuizOld.getQuestionCollection().remove(question);
                idQuizOld = em.merge(idQuizOld);
            }
            if (idQuizNew != null && !idQuizNew.equals(idQuizOld)) {
                idQuizNew.getQuestionCollection().add(question);
                idQuizNew = em.merge(idQuizNew);
            }
            for (Answer answerCollectionNewAnswer : answerCollectionNew) {
                if (!answerCollectionOld.contains(answerCollectionNewAnswer)) {
                    Question oldIdQuestionOfAnswerCollectionNewAnswer = answerCollectionNewAnswer.getIdQuestion();
                    answerCollectionNewAnswer.setIdQuestion(question);
                    answerCollectionNewAnswer = em.merge(answerCollectionNewAnswer);
                    if (oldIdQuestionOfAnswerCollectionNewAnswer != null && !oldIdQuestionOfAnswerCollectionNewAnswer.equals(question)) {
                        oldIdQuestionOfAnswerCollectionNewAnswer.getAnswerCollection().remove(answerCollectionNewAnswer);
                        oldIdQuestionOfAnswerCollectionNewAnswer = em.merge(oldIdQuestionOfAnswerCollectionNewAnswer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = question.getIdQuestion();
                if (findQuestion(id) == null) {
                    throw new NonexistentEntityException("The question with id " + id + " no longer exists.");
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
            Question question;
            try {
                question = em.getReference(Question.class, id);
                question.getIdQuestion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The question with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Answer> answerCollectionOrphanCheck = question.getAnswerCollection();
            for (Answer answerCollectionOrphanCheckAnswer : answerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Question (" + question + ") cannot be destroyed since the Answer " + answerCollectionOrphanCheckAnswer + " in its answerCollection field has a non-nullable idQuestion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Quiz idQuiz = question.getIdQuiz();
            if (idQuiz != null) {
                idQuiz.getQuestionCollection().remove(question);
                idQuiz = em.merge(idQuiz);
            }
            em.remove(question);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Question> findQuestionEntities() {
        return findQuestionEntities(true, -1, -1);
    }

    public List<Question> findQuestionEntities(int maxResults, int firstResult) {
        return findQuestionEntities(false, maxResults, firstResult);
    }

    private List<Question> findQuestionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Question.class));
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

    public Question findQuestion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Question.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuestionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Question> rt = cq.from(Question.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void agregarPregunta(Quiz idQuiz, Question pregunta){
        
        Thumbnail thumb = pregunta.getImage();
        
        String filePath = FileSaver.createFileName(thumb.getType(), pregunta.getIdQuestion(), idQuiz.getIdUser().getUsername(), thumb.getExtention());
        
        FileSaver.saveFile(thumb, filePath);
        
        pregunta.setImagen(filePath);
        pregunta.setIdQuiz(idQuiz);
        create(pregunta);
        
    }
    
    public void actualizarPregunta(Question preguntaNueva){
        try {
            edit(preguntaNueva);
        } catch (Exception ex) {
            Logger.getLogger(AnswerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarPregunta(Question preguntaEliminar){
        AnswerJpaController ajc = new AnswerJpaController(emf);
        try {
            ajc.eliminarTodasRespuestas(preguntaEliminar);
            destroy(preguntaEliminar.getIdQuestion());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AnswerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException | SQLException ex) {
            Logger.getLogger(QuestionJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarTodasPreguntas(Quiz idQuiz) throws SQLException{
        List<Question> preguntas;
        EntityManager em = getEntityManager();
        DataBaseInbo conexion = new DataBaseInbo();
        AnswerJpaController ajc = new AnswerJpaController(emf);

        if (conexion.MySQLConnect() == null) {
            throw new SQLException("Conexión fallida, intentelo más tarde");
        }

        String queryName = "Question.findByIdQuiz";
        Query query = em.createNamedQuery(queryName);
        query.setParameter("idQuiz", idQuiz);
        try {
            preguntas = (List<Question>) query.getResultList();
        } catch (NoResultException ex) {
            throw new NoResultException("Usuario no encontrado");
        }
        
        for (int i = 0; i < preguntas.size(); i++) {
            try {
                ajc.eliminarTodasRespuestas(preguntas.get(i));
                destroy(preguntas.get(i).getIdQuestion());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(AnswerJpaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(QuestionJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<Question> obtenerPreguntas(Quiz idQuiz) throws SQLException{
        List<Question> preguntas;
        EntityManager em = getEntityManager();
        DataBaseInbo conexion = new DataBaseInbo();

        if (conexion.MySQLConnect() == null) {
            throw new SQLException("Conexión fallida, intentelo más tarde");
        }

        String queryName = "Question.findByIdQuiz";
        Query query = em.createNamedQuery(queryName);
        query.setParameter("idQuestion", idQuiz);
        try {
            preguntas = (List<Question>) query.getResultList();
        } catch (NoResultException ex) {
            throw new NoResultException("Usuario no encontrado");
        }
        
        if(preguntas != null){
            preguntas.forEach((pregunta) -> {
                Thumbnail thumb = getThumb(pregunta.getImagen());
                pregunta.setImage(thumb);
            });
        }
        
        return preguntas;
    }
    
    public Thumbnail getThumb(String fileName) {
        Thumbnail thumb = new Thumbnail();
        thumb.setType("Question");

        File imageFile = new File(System.getProperty("user.home") + "/InboRepo/" + fileName);

        int extIndex = fileName.lastIndexOf(".");
        String imageExtention = fileName.substring(extIndex + 1).toLowerCase();
        thumb.setExtention(imageExtention);

        byte[] image = FileHelper.parseFileToBytes(imageFile, imageExtention);
        thumb.setImage(image);

        return thumb;
    }
    
}
