package com.APRT.utmLogin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class webServer implements HttpHandler {
    //http通讯，等待开发中。

   public static HttpServer server = null;
    private static int port = (int) ReadYaml.readYamlValue("config/config.yml","Config.web.port");
   public static void webStart(){

       Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           if(server!=null){
               System.out.println("Stopping web server......");
               server.stop(0);

           }

       }));

       try {
           System.out.println("Starting web server......");
           LLogger.LogRec("Starting web server......at port: " + port);
           server = HttpServer.create(new InetSocketAddress(port), 0);
           server.setExecutor(null);
           server.createContext("/", new HttpHandler() {
               @Override
               public void handle(HttpExchange exchange) throws IOException {
                   if ("POST".equals(exchange.getRequestMethod())) {
                       // 处理POST请求的逻辑
                       InputStream requestBody = exchange.getRequestBody();
                       ObjectMapper mapper = new ObjectMapper();
                       JsonNode jsonNode = mapper.readTree(requestBody);

                       boolean hasUser = jsonNode.has("user");
                       boolean hasPasswd = jsonNode.has("passwd");
                       boolean hasStatus = jsonNode.has("status");
                       System.out.println("Get post!");
                       System.out.println("Has 'user': " + hasUser);
                       System.out.println("Has 'passwd': " + hasPasswd);
                       System.out.println("Has 'status': " + hasStatus);

                       // 构建JSON响应数据
                       String responseJson = "{\"message\": \"Received your request\"}";

                       exchange.sendResponseHeaders(200, responseJson.getBytes().length);
                       OutputStream outputStream = exchange.getResponseBody();
                       outputStream.write(responseJson.getBytes());
                       outputStream.close();
                   } else if ("GET".equals(exchange.getRequestMethod())) {
                       Path indexHtmlPath = Paths.get("html/index.html");
                       if (Files.exists(indexHtmlPath)) {
                           serveFile(exchange, indexHtmlPath);
                       } else if (Files.exists(indexHtmlPath)) {
                           serveFile(exchange, indexHtmlPath);
                       } else {
                           exchange.sendResponseHeaders(404, 0);
                       }
                   } else {
                       // 如果不是POST或GET请求，返回403错误
                       exchange.sendResponseHeaders(403, 0);
                   }
               }
           });
           server.start();
           System.out.println("Started web server!");
           LLogger.LogRec("Done!");
       } catch (IOException e) {
           Logger.getLogger("this").warning("Error while starting web server!!");
           LLogger.LogRec("Error while starting web server!!");
           LLogger.LogRec(Arrays.toString(e.getStackTrace()));
           LLogger.LogRec("Message: "+e.getMessage());
           System.out.println();
           System.out.println("Get more information at kernel.log!");
           System.out.println("Cause by: "+e.getCause());
           System.out.println();
           System.out.println("--------------------------");
           System.out.println();
           System.out.println("trace: "+e.getStackTrace());
           e.printStackTrace();
           System.out.println("--------------------------");
           System.out.println();
           System.out.println("Message: "+e.getMessage());
           System.out.println();
           System.out.println("--------------------------");
           System.out.println("Now stopping server......");
           System.exit(-1);
           throw new RuntimeException(e);
       }

   }

    private static void serveFile(HttpExchange exchange, Path filePath) throws IOException {
        byte[] content = Files.readAllBytes(filePath);
        exchange.getResponseHeaders().set("Content-Type", getContentType(filePath));
        exchange.sendResponseHeaders(200, content.length);
        OutputStream os = exchange.getResponseBody();
        os.write(content);
        os.close();
    }
    private static String getContentType(Path path) {
        String fileName = path.getFileName().toString();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".php")) {
            return "application/x-httpd-php";
        }
        // Add more content types as needed
        return "application/octet-stream"; // Default content type
    }

}

