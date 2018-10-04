package javasearchengine.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class PageFile {
    
    public String data;

    public PageFile(File f) {
        data = readToString(f);
    }

    public String readToString(File file) {
        String encoding = "UTF-8";
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.read(fileContent);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
