package httpwebserver;

import httpwebserver.server.WebServer;
import javasearchengine.objects.PageManager;

public class Main {

    public static WebServer wb;

    public static void main(String[] args) {
        wb = new WebServer(80);
        PageManager.readPages();
        wb.start();
    }
}
