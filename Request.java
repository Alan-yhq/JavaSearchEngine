package httpwebserver.util;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    
    private String requestType;  
    private String requestContent;
    private String head;
    private String requestByte;
    public String datas;
    
    public Request(InputStream is){
        byte[] data = new byte[1024];
        try {
            int len = is.read(data);
            if (len != -1){
                datas = new String(data);
                requestType = getRequestType0(data);
                requestContent = getRequestContent0(data);
                head = getHead0(data);
                requestByte = getRequestByte0(data);
            }
        } catch (IOException ex) {
        }
    }

    public void setHead(String Head) {
        this.head = Head;
    }

    public void setRequestByte(String RequestByte) {
        this.requestByte = RequestByte;
    }

    public void setRequestContent(String RequestContent) {
        this.requestContent = RequestContent;
    }

    public void setRequestType(String RequestType) {
        this.requestType = RequestType;
    }

    public String getHead() {
        return head;
    }

    public String getRequestByte() {
        return requestByte;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public String getRequestType() {
        return requestType;
    }
    
    private String getRequestByte0(byte[] data){
        String s = new String(data);
        int a = s.lastIndexOf("\r\n");
        int b = s.length()-1;
        return s.substring(a+1,b);
    }
    
    private String getHead0(byte[] data){
        String s = new String(data);
        int a = s.indexOf("\r\n");
        int b = s.lastIndexOf("\r\n");
        if (a >= 0 && b >= 0){
            return s.substring(a+1,b);
        }
        return "";
    }
    
    private String getRequestType0(byte[] data){
        String s = new String(data);
        int a = s.indexOf(" ");
        return s.substring(0, a);
    }  
    
    private String getRequestContent0(byte[] data){
        String s = new String(data);
        int a = s.indexOf(" ");
        int b = s.indexOf(" ",a+1);
        if (a >= 0 && b >= 0){
            return s.substring(a+1, b).replace("/", "\\");
        }
        return "";
    }    
}
