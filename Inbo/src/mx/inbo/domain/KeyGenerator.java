package mx.inbo.domain;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que generadora de claves
 *
 * @author Adolfo Ángel
 */
public class KeyGenerator {

    private static int key = -1;

    /**
     * Genera una clave en base al algoritmo SHA1PRNG
     *
     * @return Clave generada
     */
    public static int obtenerId() {

        SecureRandom cipher;

        try {
            cipher = SecureRandom.getInstance("SHA1PRNG");
            key = cipher.nextInt(500000);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return key;
    }

    public KeyGenerator() {
    }

}
