package javasearchengine.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class Page {

    public String pageTitle = "None";
    public String pageDescribe = "None";
    public String pagePath = "None";

    public Page(String pageTitle, String pageDescribe, String pagePath) {
        this.pageTitle = pageTitle;
        this.pageDescribe = pageDescribe;
        this.pagePath = pagePath;
    }

    public Page clone() {
        return new Page(pageTitle, pageDescribe, pagePath);
    }
    
    public void save(File files){
        File file = new File(files, (int) (Math.random() * 100000000) + ".page");
        while (file.exists()){
            file = new File(files, (int) (Math.random() * 100000000) + ".page");
        }
        try {
            try (FileOutputStream in = new FileOutputStream(file)) {
                if (pageDescribe.length() > 0 && pagePath.length() > 0 && pageTitle.length() > 0
                        && !pageDescribe.equalsIgnoreCase("NONE") && !pagePath.equalsIgnoreCase("NONE") && !pageTitle.equalsIgnoreCase("NONE")) {
                    in.write((pageDescribe + "\r\n").getBytes());
                    in.write((pagePath + "\r\n").getBytes());
                    in.write((pageTitle + "\r\n").getBytes());
                } else {
                    file.delete();
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public Page(String ip) {
        try {
            this.pagePath = ip;
            this.pageTitle = Jsoup.connect(ip).get().getElementsByTag("title").text();
            this.pageDescribe = "";
            Document document = Jsoup.connect(ip).get();
            for (Element element : document.children()) {
                this.pageDescribe += element.text();
            }
        } catch (MalformedURLException | IllegalArgumentException ex) {
        } catch (IOException ex) {
        }
    }

    @Override
    public String toString() {
        String send = "<div><a  href=\"%PATH%\" target=\"_blank\"><font size=\"4\" color=\"blue\"><u>%TITLE%</u></font></a><br><font size=\"3\" color=\"gray\">%DESCRIBE%</font><br><a target=\"_blank\" href=\"%PATH%\"><font size=\"3\" color=\"green\">%PATH%</font></a></div><br>\r\n";
        return send.replace("%TITLE%", pageTitle)
                .replace("%DESCRIBE%", pageDescribe.substring(0, (pageDescribe.length() < 180 ? pageDescribe.length() : 180)))
                .replace("%PATH%", pagePath);
    }

}
