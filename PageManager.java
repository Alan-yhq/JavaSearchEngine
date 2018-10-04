package javasearchengine.objects;

import httpwebserver.Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageManager {

    public final static List<Page> LIST = new ArrayList<>();
    public static int everyPageLength = 20;
    static int i = 0;
    static HashMap<String, Boolean> map = new HashMap<>();

    public static List<List<Page>> searchPages(String[] strs) {
        List<List<Page>> list1 = new ArrayList<>();
        Thread t = new Thread() {
            @Override
            public void run() {
                synchronized (list1){
                    List<Page> list_path = new ArrayList<>();        
                    List<Page> list_title = new ArrayList<>();        
                    List<Page> list_Describe = new ArrayList<>();        
                    for (int i = 0; i < LIST.size(); i++) {
                        Page p = LIST.get(i);
                        if (p != null) {
                            if (stringContains(p.pagePath,strs)) {
                                Page p2 = p.clone();
                                for (String s : strs){
                                    if (s.length() > 0){
                                        p2.pageDescribe = p2.pageDescribe.replace(s, "<font color=\"red\">" + s + "</font>");
                                        p2.pageTitle = p2.pageTitle.replace(s, "<font color=\"red\">" + s + "</font>");      
                                    }                                     
                                }
                                list_path.add(p2);
                            } else if (stringContains(p.pageTitle,strs)){
                                Page p2 = p.clone();
                                for (String s : strs){
                                    if (s.length() > 0){
                                        p2.pageDescribe = p2.pageDescribe.replace(s, "<font color=\"red\">" + s + "</font>");
                                        p2.pageTitle = p2.pageTitle.replace(s, "<font color=\"red\">" + s + "</font>");      
                                    }                                     
                                }
                                list_title.add(p2);
                            } else if (stringContains(p.pageDescribe,strs)){
                                Page p2 = p.clone();
                                for (String s : strs){
                                    if (s.length() > 0){
                                        p2.pageDescribe = p2.pageDescribe.replace(s, "<font color=\"red\">" + s + "</font>");
                                        p2.pageTitle = p2.pageTitle.replace(s, "<font color=\"red\">" + s + "</font>");      
                                    }                                     
                                }
                                list_Describe.add(p2);
                            }
                        } else {
                            LIST.remove(i);
                            i--;
                        }
                    }                       
                    list_path.addAll(list_title);
                    list_path.addAll(list_Describe);
                    List<Page> list3 = new ArrayList<>();
                    for (int i = 0; i < list_path.size(); i++) {
                        Page p = list_path.get(i);
                        list3.add(p);
                        if ((i % (everyPageLength - 1) == 0) && i > 1) {
                            list1.add(list3);
                            list3 = new ArrayList<>();
                        } else if (list_path.size() < everyPageLength) {
                            list1.add(list_path);
                            break;
                        }
                    }    
                }              
            }
        };
        t.start();
        while (t.isAlive()){
        }
        return list1;
    }

    public static void savePages() {
        File files = new File(Main.wb.WorkPath, "save");
        try {
            files.createNewFile();
        } catch (IOException ex) {
        }
        for (Page p : LIST) {
            File file = new File(files, Math.random() * 100000 + ".page");
            try {
                try (FileOutputStream in = new FileOutputStream(file)) {
                    if (p.pageDescribe.length() > 0 && p.pagePath.length() > 0 && p.pageTitle.length() > 0
                            && !p.pageDescribe.equalsIgnoreCase("NONE") && !p.pagePath.equalsIgnoreCase("NONE") && !p.pageTitle.equalsIgnoreCase("NONE")) {
                        in.write((p.pageDescribe + "\r\n").getBytes());
                        in.write((p.pagePath + "\r\n").getBytes());
                        in.write((p.pageTitle + "\r\n").getBytes());
                    } else {
                        file.delete();
                    }
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    public static void readPages() {
        File files = new File(Main.wb.WorkPath, "save");
        if (!files.exists()) {
            files.mkdir();
        }
        String encoding = "UTF-8";
        for (File file : files.listFiles()) {
            new Thread() {
                public void run() {
                    Long filelength = file.length();
                    if (filelength == 0){
                        file.delete();
                        return;
                    }
                    String s = null, a, b, c;
                    byte[] filecontent = new byte[filelength.intValue()];
                    try {
                        try (FileInputStream in = new FileInputStream(file)) {
                            in.read(filecontent);
                        }
                    } catch (FileNotFoundException e) {
                    } catch (IOException e) {
                    }
                    try {
                        s = new String(filecontent, encoding);
                    } catch (UnsupportedEncodingException ex) {
                    }
                    String[] strs = s.split("\r\n");
                    if (strs.length < 3) {
                        file.delete();
                        return;
                    }
                    a = strs[0];
                    b = strs[1];
                    if (map.get(b) == null){
                        map.put(b, true);
                    } else {
                        file.delete();
                        return;
                    }
                    c = strs[2];
                    Page p = new Page(c, a, b);
                    if (p.pageDescribe.length() > 0 && p.pagePath.length() > 0 && p.pageTitle.length() > 0
                            && !p.pageDescribe.equalsIgnoreCase("NONE") && !p.pagePath.equalsIgnoreCase("NONE") && !p.pageTitle.equalsIgnoreCase("NONE")) {
                        synchronized (LIST) {
                            LIST.add(p);
                        }
                    } else {
                        file.delete();
                    }
                }
            }.start();
        }
        System.out.println("Start");
    }
    
    public static boolean stringContains(String s, String[] strs){
        boolean flag = true;
        for (String s2 : strs){
            if (!s.contains(s2)){
                flag = false;
            }
        }
        return flag;
    }
}
