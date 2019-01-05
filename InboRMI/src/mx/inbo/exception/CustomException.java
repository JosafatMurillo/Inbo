/**
 * Clase de excepciones personales
 * 
 * De La Cruz Díaz Adolfo Ángel; Murillo Hernández Josafat
 * 
 * Versión 1.0
 * 
 * 19/12/2018
 * 
 * Inbo
 */
package mx.inbo.exception;

/**
 *
 * @author Josafat
 */
public class CustomException extends Exception{
    
    /**
     * Funcion que representa una excpeion personalizada
     * @param mensaje Cadena de caracteres que será el mensaje
     */
    public CustomException(String mensaje){
        super(mensaje);
    }
    
}
