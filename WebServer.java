package httpwebserver.server;

import httpwebserver.Main;
import httpwebserver.util.Tools;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javasearchengine.objects.Datas;
import javasearchengine.objects.Page;
import javasearchengine.objects.PageManager;

public class WebServer {

    public List<String> BanIps = new ArrayList<>();
    public String WorkPath = System.getProperty("user.dir");
    public ServerSocket ss;
    public String path_main = "/search.html";
    public HashMap<String, Integer> map = new HashMap<>();
    public Datas datasForSearch = new Datas();

    public WebServer(int i) {
        try {
            ss = new ServerSocket(i);
        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        PageManager.savePages();
        System.exit(0);
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                Scanner s = new Scanner(System.in);
                while (s.hasNextLine()) {
                    String[] line = s.nextLine().split(" ");
                    if (line.length == 1 && line[0].equalsIgnoreCase("stop")) {
                        Main.wb.stop();
                    } else if (line.length == 2 && line[0].equalsIgnoreCase("ban")) {
                        Main.wb.BanIps.add(line[1]);
                    } else if (line.length == 2 && line[0].equalsIgnoreCase("unban")) {
                        Main.wb.BanIps.remove(line[1]);
                    } else if (line.length == 1 && line[0].equalsIgnoreCase("tps")) {
                        System.out.println("内存使用率:" + Tools.getMemory());
                    } else if (line.length == 2 && line[0].equalsIgnoreCase("addPage")) {
                        Page p = new Page(line[1]);
                        PageManager.LIST.add(p);
                        p.save(new File(WorkPath, "save"));
                    }
                }
            }
        }.start();
        Socket s;
        while (true) {
            try {
                s = ss.accept();
                new SocketThread(s).start();
            } catch (IOException ex) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
