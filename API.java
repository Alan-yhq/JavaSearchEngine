package httpwebserver.util;

import httpwebserver.server.WebServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javasearchengine.objects.Page;
import javasearchengine.objects.PageFile;
import javasearchengine.objects.PageManager;

public class API {

    public static void respond(Socket socket, WebServer webServer) throws IOException {
        try {
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            if (request.getRequestType() != null) {
                if (request.getRequestType().equalsIgnoreCase("GET")) {
                    respondGet(request, webServer, socket);
                }
            }
        } catch (IOException ex) {
        }
        socket.close();
    }

    public static void respondGet(Request request, WebServer webServer, Socket socket) throws UnsupportedEncodingException {
        String requestContent = request.getRequestContent();
        if (requestContent.contains(":") && requestContent.contains(";") && requestContent.contains("?")) {
            OutputStream outputStream = null;
            String search = URLDecoder.decode(requestContent.substring(requestContent.indexOf("?") + 1, requestContent.indexOf(";")), "utf-8");
            int pageValue = Integer.parseInt(requestContent.substring(requestContent.lastIndexOf(":") + 1));
            System.out.println("IP:\"" + socket.getInetAddress().toString() + "\" Search:\"" + search + "\" Page:\"" + pageValue + "\"");
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException ex) {
            }
            List<String> head = new ArrayList<>();
            head.add("Content-Type: %TYPE\r\n".replace("%TYPE", getType("null.html")));
            String datas = "";
            List<List<Page>> pages = PageManager.searchPages(search.split(" "));
            if (!pages.isEmpty() && search.length() > 0 && pageValue < pages.size() && pageValue >= 0) {
                List<Page> pages2 = pages.get(pageValue);
                for (int i = 0; i < pages2.size(); i++) {
                    Page page = pages2.get(i);
                    if (page != null) {
                        if (page.pageDescribe.length() > 0 && page.pagePath.length() > 0 && page.pageTitle.length() > 0
                                && !page.pageDescribe.equalsIgnoreCase("NONE") && !page.pagePath.equalsIgnoreCase("NONE") && !page.pageTitle.equalsIgnoreCase("NONE")) {
                            datas += page.toString() + "\r\n";
                        }
                    } else {
                        pages2.remove(page);
                        PageManager.LIST.remove(page);
                    }
                }
            }
            String buttonDatas = "";
            for (int i = (pageValue >= 5 ? pageValue - 4 : 1); i <= (pageValue < 5 && pages.size() >= 10 ? 10 : pageValue + 5) && i <= pages.size() && search.length() > 0 && pages.size() > 1 && pageValue < pages.size() && pageValue >= 0; i++) {
                buttonDatas += "		<button style=\"\n"
                        + "			width:30px;\n"
                        + "			height:30px;\n"
                        + "			font-weight: bold;\n"
                        + "			font-size:20px\"\n"
                        + "		onClick=\"window.location.href='search=?" + search + ";Pages:" + (i - 1) + "'\">" + i + "</button></center>";
            }
            webServer.datasForSearch.registerData("%DATAS%", datas);
            webServer.datasForSearch.registerData("%BUTTONDATAS%", buttonDatas);
            Response response = new Response("HTTP/1.1", "200", "OK", head, webServer.datasForSearch.useDatas(new PageFile(new File(webServer.WorkPath, "search.html")).data));
            webServer.datasForSearch.clearData();
            write(outputStream, response);
        } else {
            try {
                OutputStream outputStream = socket.getOutputStream();
                List<String> head = new ArrayList<>();
                head.add("Content-Type: %TYPE\r\n".replace("%TYPE", getType(webServer.path_main)));
                webServer.datasForSearch.registerData("%DATAS%", "");
                webServer.datasForSearch.registerData("%BUTTONDATAS%", "");
                Response response = new Response("HTTP/1.1", "200", "OK", head, webServer.datasForSearch.useDatas(new PageFile(new File(webServer.WorkPath, "search.html")).data));
                webServer.datasForSearch.clearData();
                write(outputStream, response);
            } catch (IOException ex) {
            }
        }
    }

    public static void write(OutputStream outputStream, Response response) {
        try {
            outputStream.write(response.toString().getBytes("UTF-8"));
            outputStream.write(response.getDatas().getBytes());
            outputStream.write("\r\n".getBytes());
        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }
    }

    public static String getType(String requestContent) {
        String s = requestContent.substring(requestContent.lastIndexOf(".") + 1, requestContent.length());
        if (s.equalsIgnoreCase("html") || s.equalsIgnoreCase("js") || s.equalsIgnoreCase("css")) {
            return "text/html";
        } else if (s.equalsIgnoreCase("jpg") || s.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        return "";
    }

}
