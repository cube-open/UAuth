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
import java.util.Arrays;
import java.util.logging.Logger;

public class webServer implements HttpHandler {
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
           server.createContext("/");
           server.setExecutor(null);
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
            // 处理GET请求，执行301重定向
            performRedirect(exchange);
        } else {
            // 如果不是POST或GET请求，返回403错误
            exchange.sendResponseHeaders(403, 0);
        }
    }

    private void performRedirect(HttpExchange exchange) throws IOException {
        String redirectUrl = "about:blank#block";
        exchange.getResponseHeaders().set("Location", redirectUrl);
        exchange.sendResponseHeaders(301, -1); // 发送301重定向响应码
        exchange.close();
    }
    }

