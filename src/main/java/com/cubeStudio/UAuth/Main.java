/*
Java实现-启灵域界科技
JsonDB算法基于aztice的JsonDB
 */
package com.cubeStudio.UAuth;

import org.apache.maven.surefire.shared.io.output.TeeOutputStream;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class Main {
    public static boolean Status = true;
    private static String LightSK_Key;
    private static Boolean LightSK = false;
    static String destinationFolderPath = "config/";
    static Scanner in = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static HashMap<String, Boolean> hashMap = new HashMap<>();
    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static String Cmd = " ";
    static Boolean watchDog = ReadYaml.readYamlBoolean("config/config.yml","Config.watchDog");
    public static void main(String[] args) {

        String logFileName = "latest.log";
        Scanner scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LLogger.info("Stopping server......");
            Status = false;
            if (scanner != null) {
                LLogger.info("Closing scanner......");
                scanner.close();
                LLogger.info("Scanner was closed");

            }


        }));
        try {
            // 创建日志文件输出流
            FileOutputStream logOutputStream = new FileOutputStream(logFileName);
            PrintStream printStream = new PrintStream(logOutputStream);

            // 重定向标准输出和标准错误流到日志文件
            System.setOut(new PrintStream(new TeeOutputStream(System.out, printStream)));
            System.setErr(new PrintStream(new TeeOutputStream(System.err, printStream)));
        } catch (IOException e) {
            System.err.println("Error redirecting output to log file: " + e.getMessage());
        }

        // 输出一些日志信息和控制台输出

        long startTime = System.currentTimeMillis();
        LLogger.info("UAuth now loading......");
        long pid = getCurrentProcessId();
        LLogger.info("Running at pid: " + pid);
        if (watchDog) {
            WatchDog.WatchDog();
        }
        // 每500ms执行一次握手
        executor.scheduleAtFixedRate(WatchDog::handshake, 0, 500, TimeUnit.MILLISECONDS);
        // 打印操作系统信息
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        LLogger.info("Operating System: " + osName + " " + osVersion);

        // 打印Java版本
        String javaVersion = System.getProperty("java.version");
        LLogger.info("Java Version: " + javaVersion);

        // 打印运行时最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        String maxMemoryStr = maxMemory == Long.MAX_VALUE ? "N/A" : String.valueOf(maxMemory);
        LLogger.info("Max Memory: " + maxMemoryStr);

        // 打印运行时最小内存
        long minMemory = Runtime.getRuntime().totalMemory();
        String minMemoryStr = String.valueOf(minMemory);
        LLogger.info("Min Memory: " + minMemoryStr);
        Dir.mkdir(".", "log");


        try {

            // 读取资源文件
            Dir.mkdir(".", "html");
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.yml");

            if (inputStream == null) {
                LLogger.warn("Can not find config!");
                LLogger.exception("Can not find config!");
                return;
            }
            File destinationFolder = new File(destinationFolderPath);
            /*InputStream inputStreamHtml = Main.class.getClassLoader().getResourceAsStream("html/reg.html");
            File html = new File("html");
            if (!html.exists()){
                OutputStream outputHtml = new FileOutputStream("html/"+ "reg.html");
                byte[] buff = new byte[1024];
                int lengths;
                while ((lengths = inputStreamHtml.read(buff)) > 0) {
                    outputHtml.write(buff, 0, lengths);
                }
                outputHtml.close();
            }


             */
            extractResources("html", "html");
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
                OutputStream outputStream = new FileOutputStream(destinationFolderPath + "config.yml");
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // 关闭流
                inputStream.close();
                outputStream.close();

                LLogger.info("Created config");
                LLogger.debug("Created config");
                // 创建目标文件夹
            }

            // 写入文件到目标文件夹


        } catch (IOException e) {
            logger.warning("Error while creating config!!!");
            LLogger.error("Error while creating config!!!");
            LLogger.error(Arrays.toString(e.getStackTrace()));
            LLogger.error("Cause by: " + e.getCause() + " " + e.getMessage());
        }

        LLogger.info("Registering command......");
        Boolean CmdStatus = false;
        RegCommand("exit");
        RegCommand("reload");
        RegCommand("Ver");
        RegCommand("Version");
        RegCommand("version");
        RegCommand("help");
        RegCommand("?");
        LLogger.info("Auto backup=" + ReadYaml.readYamlBoolean("./config/config.yml", "Config.autoBackup.Enable"));
        LightSK = ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable");
        if (LightSK) {
            if (ReadYaml.readYamlString("./config/config.yml", "Config.key.key") == null) {
                logger.warning("Key is null!Will not start server(key can't be like '123')!");
                LLogger.info("Key is null,Now stopping server.");
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(-1);
            }
            LightSK_Key = ReadYaml.readYamlString("./config/config.yml", "Config.key.key");

        } else {
            System.err.println("LightSK is not enable,server wont start.");
            System.exit(0);
        }
        sqlServer.con();
        if (ReadYaml.readYamlValue("config/config.yml", "Config.web.port") == null) {
            logger.warning("Error!Web server listen port is null!");
            LLogger.exception("Web server listen port is null!");
            System.exit(-1);
        }

        webServer.webStart();
        LLogger.info("Server started!");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LLogger.info("Server started in " + duration + "ms");
        LLogger.info("Server started in " + duration + "ms");

        while (Status!=false) {


            System.out.print(">");

            if (in.hasNextLine()) {
                String cmd = in.nextLine().trim();
                if( cmd !=""){
                    Cmd = cmd;
                }else{continue;}

                if (Objects.equals(Cmd, "exit")) {
                    System.out.println("Bye");
                    System.exit(0);
                }
                if (Objects.equals(Cmd, "Ver") || Cmd.equals("Version") || Cmd.equals("version")) {
                    System.out.println("""
                            UAuth 1.0 SnapShot
                            Cube studio
                            Welcome!""");
                }
                if (Objects.equals(Cmd, "help") || Objects.equals(Cmd, "?")) {
                    System.out.println("""
                            Help menu
                            "exit" stop server
                            "reload" reload server and test mysql again
                            "Ver","Version","version" show server version""");
                }
                if (Objects.equals(Cmd, "reload")) {
                    try {
                        // 读取资源文件
                        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.yml");
                        if (inputStream == null) {
                            LLogger.warn("Can not find config!");
                            LLogger.error("Can not find config!");

                            return;
                        }
                        extractResources("html", "html");
                        File destinationFolder = new File(destinationFolderPath);
                        if (!destinationFolder.exists()) {
                            destinationFolder.mkdirs();
                            OutputStream outputStream = new FileOutputStream(destinationFolderPath + "config.yml");
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }

                            // 关闭流
                            inputStream.close();
                            outputStream.close();

                            LLogger.info("Created config");

                            // 创建目标文件夹
                        }
                        // 创建目标文件夹

                    } catch (IOException e) {

                        LLogger.exception("Error while creating config!!!");
                        LLogger.exception(Arrays.toString(e.getStackTrace()));
                        LLogger.error("Cause by: " + e.getCause() + " " + e.getMessage());

                    }

                    if (ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable")) {
                        if (LightSK != ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable")) {
                            LightSK = ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable");
                        }
                        if (ReadYaml.readYamlString("./config/config.yml", "Config.key.key") == null) {
                            logger.warning("Key is null!Will not start server(key can't be like '123')!Server can't running!");
                            LLogger.error("Please input LightSK key in config,then reload again!");
                            LLogger.exception("Key is null!");
                            LightSK = false;

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        LightSK_Key = ReadYaml.readYamlString("./config/config.yml", "Config.key.key");

                    } else {
                        logger.warning("Error!LightSK is not enable!Server will not run.");
                        System.exit(-1);
                    }
                    LLogger.info("Try to connect mysql server......");
                    sqlServer.con();
                    LLogger.info("Restart web server......");
                    webServer.server.stop(0);
                    webServer.webStart();
                    LLogger.info("Reloaded the server.");
                    LLogger.info("Complete!");

                }
                for (String key : hashMap.keySet()) {
                    if (Cmd != " " && !key.equals(Cmd)) {
                        // 如果不包含特定字符串
                        CmdStatus = true;

                    } else {
                        CmdStatus = false;
                        break;
                    }

                }
                if (CmdStatus == true && Cmd != " ") {
                    System.out.println("Wrong command: " + Cmd);
                    System.out.println("Please type ? or help to get  help.");
                    CmdStatus = true;
                }
            }

        }

        // 其他代码逻辑

        // 关闭System.out和System.err的重定向
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));


    }
    /**
     * 注册命令到哈希表中。
     *
     * <p>此方法尝试将指定的命令字符串添加到哈希表中，并设置其值为 `true`。
     * 如果在添加过程中发生异常，则会在控制台输出错误信息，并记录错误级别的日志。
     *
     * @param string 要注册的命令字符串
     * @throws Exception 如果添加命令到哈希表中时发生异常
     */
    public static void RegCommand(String string) {
        try {
            hashMap.put(string, true);
        } catch (Exception exception) {

            LLogger.error("Reg command failed!Because: " + exception.getMessage());
            exception.printStackTrace();
            throw exception;
        }

    }
    public static long getCurrentProcessId() {
        // 对于Java 9及以上版本
        if (System.getProperty("java.version").startsWith("1.")) {
            // Java 8及以下版本
            String name = ManagementFactory.getRuntimeMXBean().getName();
            return Long.parseLong(name.split("@")[0]);
        } else {
            // Java 9及以上版本
            return ProcessHandle.current().pid();
        }
    }


    private static void extractResources(String resourcePath, String targetDir) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourcePath);

        if (resourceUrl == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        // 如果资源是一个目录
        if ("file".equals(resourceUrl.getProtocol())) {
            copyDirectory(new File(resourceUrl.getPath()), new File(targetDir));
        } else if ("jar".equals(resourceUrl.getProtocol())) { // 如果资源在JAR中
            String jarPath = resourceUrl.getPath().substring(5, resourceUrl.getPath().indexOf("!")); // 提取JAR文件路径
            ZipFile jarFile = new ZipFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
            Enumeration<? extends ZipEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(resourcePath)) {
                    Path pathInJar = Paths.get(entryName);
                    Path basePath = Paths.get(resourcePath);
                    Path subPath = basePath.relativize(pathInJar);
                    Path pathOnDisk = Paths.get(targetDir).resolve(subPath); // 使用 resolve 方法


                    if (!entry.isDirectory()) {
                        Files.createDirectories(pathOnDisk.getParent()); // 确保父目录存在
                        try (InputStream in = jarFile.getInputStream(entry);
                             OutputStream out = Files.newOutputStream(pathOnDisk)) {
                            in.transferTo(out);
                        }
                    } else {
                        Files.createDirectories(pathOnDisk); // 创建目录
                    }
                }
            }
            jarFile.close();
        }
    }


    private static void copyDirectory(File sourceDir, File targetDir) throws IOException {
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        for (String file : sourceDir.list()) {
            File srcFile = new File(sourceDir, file);
            File destFile = new File(targetDir, file);
            if (srcFile.isDirectory()) {
                copyDirectory(srcFile, destFile);
            } else {
                InputStream in = new FileInputStream(srcFile);
                OutputStream out = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            }
        }
    }
}


