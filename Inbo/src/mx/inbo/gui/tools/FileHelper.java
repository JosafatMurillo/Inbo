/*
 * Copyright 2018 adolf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.inbo.gui.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adolf
 */
public class FileHelper {
    
    public static byte[] parseFileToBytes(File file, String fileExtention){
        
        byte[] fileBytes = {};
        
        ByteArrayOutputStream byteArrayOut = null;
        
        try {
            BufferedImage imageBuff = ImageIO.read(file);
            byteArrayOut = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, fileExtention, byteArrayOut);
            byteArrayOut.flush();
            fileBytes = byteArrayOut.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                byteArrayOut.close();
            } catch (IOException ex) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return fileBytes;
    }
    
}
