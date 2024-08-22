package com.cubeStudio.UAuth;

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
    private static final Path HTML_ROOT = Paths.get("html/");

    public static HttpServer server = null;
    private static int port = (int) ReadYaml.readYamlValue("config/config.yml", "Config.web.port");

    public static void webStart() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server != null) {
                LLogger.info("Stopping web server......");
                server.stop(0);

            }

        }));

        try {
            LLogger.info("Starting web server......at port: " + port);
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
                        boolean hasAction = jsonNode.has("action");
                        boolean success = false;
                        LLogger.info("Get post!"+"Has 'user': " + hasUser+"Has 'passwd': " + hasPasswd+"Has 'action' :"+hasAction);
                        String token = "";

                        // 构建JSON响应数据
                        String responseJson = "{\"status\":"+success+",\"token\":\""+token+"\",\"message\":\"System is developing......\",\"version\":\"UAuth V0.1 DEV\"}";

                        exchange.sendResponseHeaders(200, responseJson.getBytes().length);
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(responseJson.getBytes());
                        outputStream.close();
                    } else if ("GET".equals(exchange.getRequestMethod())) {
                        String requestedPath = exchange.getRequestURI().getPath();

                        // 输出请求的路径

                        LLogger.info("GET request for path: " + requestedPath);

                        // 获取客户端的IP地址
                        String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();

                        LLogger.info("Request from IP: " + clientIp);

                        // 去除开头的"/"
                        if (requestedPath.startsWith("/")) {
                            requestedPath = requestedPath.substring(1);
                        }

                        Path filePath = Paths.get("html/", requestedPath);

                        LLogger.info("Trying to access file at: " + filePath);

                        // 检查文件或目录是否存在
                        if (Files.exists(filePath)) {
                            if (Files.isDirectory(filePath)) {
                                // 如果是目录，尝试找到index.html
                                Path indexPath = filePath.resolve("index.html");
                                if (Files.exists(indexPath)) {
                                    LLogger.debug("Found index.html, serving...");
                                    serveFile(exchange, indexPath);
                                } else {
                                    LLogger.debug("No index.html found, sending 404.");
                                    send404(exchange);
                                }
                            } else {
                                // 如果是文件，直接返回文件内容
                                LLogger.debug("Serving...");
                                LLogger.info("Serving......");
                                serveFile(exchange, filePath);
                            }
                        } else {
                            LLogger.debug("File not found, sending 404.");
                            LLogger.info("File not found, sending 404.");
                            send404(exchange);
                        }
                    } else {
                        // 如果不是POST或GET请求，返回403错误
                        exchange.sendResponseHeaders(403, 0);
                    }
                }

            });
            server.start();
            LLogger.info("Started web server!");
            LLogger.info("Done!");
        } catch (IOException e) {
            Logger.getLogger("this").warning("Error while starting web server!!");
            LLogger.info("Error while starting web server!!");
            LLogger.info(Arrays.toString(e.getStackTrace()));
            LLogger.info("Message: " + e.getMessage());

            LLogger.error("Get more information at kernel.log!");
            LLogger.error("Cause by: " + e.getCause());

            LLogger.error("--------------------------");

            LLogger.error("trace: " + e.getStackTrace());
            e.printStackTrace();
            LLogger.error("--------------------------");

            LLogger.error("Message: " + e.getMessage());

            LLogger.error("--------------------------");
            System.out.println("Now stopping server......");
            System.exit(-1);
            throw new RuntimeException(e);
        }

    }

    private static void serveFile(HttpExchange exchange, Path filePath) throws IOException {


        System.out.println("ServeServer is trying to find file: " + filePath);
        LLogger.info("ServeServer is trying to find file: " + filePath);
        if (Files.isDirectory(filePath)) {
            Path indexPath = filePath.resolve("index.html");
            if (Files.exists(indexPath)) {
                serveFile(exchange, indexPath);
            } else {
                System.out.println("No index.html found, sending 404.");
                LLogger.info("No index.html found, sending 404.");
                send404(exchange);
            }
        } else if (Files.exists(filePath)) {
            byte[] content = Files.readAllBytes(filePath);
            exchange.getResponseHeaders().set("Content-Type", getContentType(filePath));
            exchange.sendResponseHeaders(200, content.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(content);
            }
        } else {
            System.out.println("File not found, sending 404.");
            LLogger.info("File not found, sending 404.");
            send404(exchange);
        }
    }


    private static void send404(HttpExchange exchange) throws IOException {
        Path notFoundPage = HTML_ROOT.resolve("404.html");
        if (Files.exists(notFoundPage)) {
            byte[] content = Files.readAllBytes(notFoundPage);
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(404, content.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(content);
            }
        } else {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    private static String getContentType(Path path) {
        String fileName = path.getFileName().toString();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        }
        // 添加更多内容类型...
        return "application/octet-stream"; // 默认内容类型
    }

}

