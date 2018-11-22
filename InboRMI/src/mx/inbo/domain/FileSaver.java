/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mx.inbo.controllers.QuizJpaController;

/**
 *
 * @author adolf
 */
public class FileSaver {
    
    public static String createFilePath(String type, Integer id, String username, String extention){
        return System.getProperty("user.home") + "/InboRepo/" + username + "_" + type + id + "." + extention;
    }
    
    public static String saveFile(Thumbnail thumb, String filePath) {
        File directory = new File(System.getProperty("user.home") + "/InboRepo");
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            InputStream input = new ByteArrayInputStream(thumb.getImage());
            BufferedImage imageBuff = ImageIO.read(input);
            File file = new File(filePath);
            boolean created = file.createNewFile();
            if (created) {
                ImageIO.write(imageBuff, thumb.getExtention(), file);
            }
        } catch (IOException ex) {
            Logger.getLogger(QuizJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return filePath;
    }
    
}
