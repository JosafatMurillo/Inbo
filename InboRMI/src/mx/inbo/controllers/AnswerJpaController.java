/**
 * Clase del controlador de la entidad Answer
 * 
 * De La Cruz Díaz Adolfo Ángel; Murillo Hernández Josafat
 * 
 * Versión 1.0
 * 
 * 19/12/2018
 * 
 * Inbo
 */
package mx.inbo.controllers;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mx.inbo.controllers.exceptions.NonexistentEntityException;
import mx.inbo.datasource.DataBaseInbo;
import mx.inbo.domain.FileHelper;
import mx.inbo.domain.FileSaver;
import mx.inbo.domain.Thumbnail;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Question;

/**
 *
 * @author Josafat
 */
public class AnswerJpaController implements Serializable {

    public AnswerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Answer answer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Question idQuestion = answer.getIdQuestion();
            if (idQuestion != null) {
                idQuestion = em.getReference(idQuestion.getClass(), idQuestion.getIdQuestion());
                answer.setIdQuestion(idQuestion);
            }
            em.persist(answer);
            if (idQuestion != null) {
                idQuestion.getAnswerCollection().add(answer);
                idQuestion = em.merge(idQuestion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Answer answer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Answer persistentAnswer = em.find(Answer.class, answer.getIdAnswer());
            Question idQuestionOld = persistentAnswer.getIdQuestion();
            Question idQuestionNew = answer.getIdQuestion();
            if (idQuestionNew != null) {
                idQuestionNew = em.getReference(idQuestionNew.getClass(), idQuestionNew.getIdQuestion());
                answer.setIdQuestion(idQuestionNew);
            }
            answer = em.merge(answer);
            if (idQuestionOld != null && !idQuestionOld.equals(idQuestionNew)) {
                idQuestionOld.getAnswerCollection().remove(answer);
                idQuestionOld = em.merge(idQuestionOld);
            }
            if (idQuestionNew != null && !idQuestionNew.equals(idQuestionOld)) {
                idQuestionNew.getAnswerCollection().add(answer);
                idQuestionNew = em.merge(idQuestionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = answer.getIdAnswer();
                if (findAnswer(id) == null) {
                    throw new NonexistentEntityException("The answer with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Answer answer;
            try {
                answer = em.getReference(Answer.class, id);
                answer.getIdAnswer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The answer with id " + id + " no longer exists.", enfe);
            }
            Question idQuestion = answer.getIdQuestion();
            if (idQuestion != null) {
                idQuestion.getAnswerCollection().remove(answer);
                idQuestion = em.merge(idQuestion);
            }
            em.remove(answer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Answer> findAnswerEntities() {
        return findAnswerEntities(true, -1, -1);
    }

    public List<Answer> findAnswerEntities(int maxResults, int firstResult) {
        return findAnswerEntities(false, maxResults, firstResult);
    }

    private List<Answer> findAnswerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Answer.class));
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

    public Answer findAnswer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Answer.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnswerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Answer> rt = cq.from(Answer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * 
     * @param idQuestion
     * @param respuesta 
     */
    public void agregarRespuesta(Question idQuestion, Answer respuesta) {

        Thumbnail thumb = respuesta.getImage();

        String filePath = "";

        if (thumb != null) {
            filePath = FileSaver.createFileName(thumb.getType(), respuesta.getIdAnswer(), respuesta.getIdQuestion().getIdQuiz().getIdUser().getUsername(), thumb.getExtention());
            FileSaver.saveFile(thumb, filePath);
        }

        respuesta.setImagen(filePath);
        respuesta.setIdQuestion(idQuestion);
        create(respuesta);

    }

    /**
     * 
     * @param respuestaNueva 
     */
    public void actualizarRespuesta(Answer respuestaNueva) {
        try {
            edit(respuestaNueva);
        } catch (Exception ex) {
            Logger.getLogger(AnswerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param respuestaEliminar 
     */
    public void eliminarRespuesta(Answer respuestaEliminar) {
        try {
            destroy(respuestaEliminar.getIdAnswer());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AnswerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param idQuestion
     * @throws SQLException 
     */
    public void eliminarTodasRespuestas(Question idQuestion) throws SQLException {
        List<Answer> preguntas;
        EntityManager em = getEntityManager();
        DataBaseInbo conexion = new DataBaseInbo();

        if (conexion.MySQLConnect() == null) {
            throw new SQLException("Conexión fallida, intentelo más tarde");
        }

        String queryName = "Answer.findByIdQuestion";
        Query query = em.createNamedQuery(queryName);
        query.setParameter("idQuestion", idQuestion);
        try {
            preguntas = (List<Answer>) query.getResultList();
        } catch (NoResultException ex) {
            throw new NoResultException("Usuario no encontrado");
        }

        for (int i = 0; i < preguntas.size(); i++) {
            try {
                destroy(preguntas.get(i).getIdAnswer());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(AnswerJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param idQuestion
     * @return
     * @throws SQLException 
     */
    public List<Answer> obtenerRespuestas(Question idQuestion) throws SQLException {
        List<Answer> respuestas;
        EntityManager em = getEntityManager();
        DataBaseInbo conexion = new DataBaseInbo();

        if (conexion.MySQLConnect() == null) {
            throw new SQLException("Conexión fallida, intentelo más tarde");
        }

        String queryName = "Answer.findByIdQuestion";
        Query query = em.createNamedQuery(queryName);
        query.setParameter("idQuestion", idQuestion);
        try {
            respuestas = (List<Answer>) query.getResultList();
        } catch (NoResultException ex) {
            throw new NoResultException("Usuario no encontrado");
        }

        if (respuestas != null) {
            respuestas.forEach((respuesta) -> {
                Thumbnail thumb = getThumb(respuesta.getImagen());
                respuesta.setImage(thumb);
            });
        }
        return respuestas;
    }

    /**
     * 
     * @param fileName
     * @return 
     */
    public Thumbnail getThumb(String fileName) {
        Thumbnail thumb = new Thumbnail();
        thumb.setType("Answer");

        File imageFile = new File(System.getProperty("user.home") + "/InboRepo/" + fileName);

        int extIndex = fileName.lastIndexOf(".");
        String imageExtention = fileName.substring(extIndex + 1).toLowerCase();
        thumb.setExtention(imageExtention);

        byte[] image = FileHelper.parseFileToBytes(imageFile, imageExtention);
        thumb.setImage(image);

        return thumb;
    }

}
