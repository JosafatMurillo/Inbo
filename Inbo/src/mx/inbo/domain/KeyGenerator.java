
package mx.inbo.domain;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyGenerator {
    
    private static int key = -1;
    
    // This hides the implicit public constructor
    private KeyGenerator(){}
        
    public static int obtenerId(){
        
        SecureRandom cipher;
        
        try{
            cipher = SecureRandom.getInstance("SHA1PRNG");
            key = cipher.nextInt(500000);
            
        }catch(NoSuchAlgorithmException ex){
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }
    
}
