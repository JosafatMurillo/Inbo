package mx.inbo.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.inbo.entities.Question;
import mx.inbo.entities.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-12T15:48:51")
@StaticMetamodel(Quiz.class)
public class Quiz_ { 

    public static volatile SingularAttribute<Quiz, String> descripcion;
    public static volatile SingularAttribute<Quiz, User> idUser;
    public static volatile CollectionAttribute<Quiz, Question> questionCollection;
    public static volatile SingularAttribute<Quiz, Integer> idQuiz;
    public static volatile SingularAttribute<Quiz, String> titulo;
    public static volatile SingularAttribute<Quiz, String> imagen;

}