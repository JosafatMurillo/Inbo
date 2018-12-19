/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.inbo.domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
    
    public static String createFileName(String type, Integer id, String username, String extention){
        return username + "_" + type + id + "." + extention;
    }
    
    public static String saveFile(Thumbnail thumb, String filePath) {
        String directoryPath = System.getProperty("user.home") + "/InboRepo";
        System.out.println(directoryPath);
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            InputStream input = new ByteArrayInputStream(thumb.getImage());
            BufferedImage imageBuff = ImageIO.read(input);
            File oldFile = new File(System.getProperty("user.home") + "/InboRepo/" + filePath);
            oldFile.delete();
            File file = new File(System.getProperty("user.home") + "/InboRepo/" + filePath);
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
