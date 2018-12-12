package mx.inbo.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.inbo.entities.Question;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-12T15:48:51")
@StaticMetamodel(Answer.class)
public class Answer_ { 

    public static volatile SingularAttribute<Answer, Integer> idAnswer;
    public static volatile SingularAttribute<Answer, Question> idQuestion;
    public static volatile SingularAttribute<Answer, String> imagen;
    public static volatile SingularAttribute<Answer, String> respuesta;
    public static volatile SingularAttribute<Answer, Boolean> esCorrecta;

}