package mx.inbo.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.inbo.entities.Quiz;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-12-12T15:48:51")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Integer> idUser;
    public static volatile SingularAttribute<User, String> imagen;
    public static volatile SingularAttribute<User, String> contrasenia;
    public static volatile CollectionAttribute<User, Quiz> quizCollection;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;

}