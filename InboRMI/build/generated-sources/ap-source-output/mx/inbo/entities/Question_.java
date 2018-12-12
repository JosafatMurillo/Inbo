package mx.inbo.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.inbo.entities.Answer;
import mx.inbo.entities.Quiz;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-12T15:48:51")
@StaticMetamodel(Question.class)
public class Question_ { 

    public static volatile SingularAttribute<Question, Quiz> idQuiz;
    public static volatile SingularAttribute<Question, Integer> tiempo;
    public static volatile SingularAttribute<Question, Integer> idQuestion;
    public static volatile CollectionAttribute<Question, Answer> answerCollection;
    public static volatile SingularAttribute<Question, String> imagen;
    public static volatile SingularAttribute<Question, String> pregunta;

}