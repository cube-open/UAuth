/*
Java实现-启灵域界科技
JsonDB算法基于aztice的JsonDB
 */
package com.APRT.utmLogin;

import org.apache.maven.surefire.shared.io.output.TeeOutputStream;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;




public class Main {

    public static boolean Status = true;
    private static String LightSK_Key;
    private static Boolean LightSK = false;
    String Cmd;
    String sourceFilePath = "src/main/resources/config.yml";
    static String destinationFolderPath = "config/";
    static Scanner in = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static HashMap<String,Boolean> hashMap = new HashMap<>();
    public static void main(String[] args) {
        String logFileName = "latest.log";
        Scanner scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping server......");
            if (scanner != null) {
                scanner.close();
                System.out.println("Scanner was closed");
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


        System.out.println("UTM-Login now loading......");
        // 打印操作系统信息
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        System.out.println("Operating System: " + osName + " " + osVersion);

        // 打印Java版本
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java Version: " + javaVersion);

        // 打印运行时最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        String maxMemoryStr = maxMemory == Long.MAX_VALUE ? "N/A" : String.valueOf(maxMemory);
        System.out.println("Max Memory: " + maxMemoryStr);

        // 打印运行时最小内存
        long minMemory = Runtime.getRuntime().totalMemory();
        String minMemoryStr = String.valueOf(minMemory);
        System.out.println("Min Memory: " + minMemoryStr);
        Dir.mkdir(".","log");
        String Cmd;
        try {
            // 读取资源文件
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.yml");
            if (inputStream == null) {
                System.out.println("Can not find config!");
                LLogger.LogRec("Can not find config!");
                return;
            }
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

                System.out.println("Created config");
                LLogger.LogRec("Created config");
                // 创建目标文件夹
            }

            // 写入文件到目标文件夹


        } catch (IOException e) {
            logger.warning("Error while creating config!!!");
            LLogger.LogRec("Error while creating config!!!");
            LLogger.LogRec(Arrays.toString(e.getStackTrace()));
            System.out.println("Cause by: " + e.getCause()+ " " + e.getMessage());
        }
        System.out.println("Registering command......");
        Boolean CmdStatus = false;
        RegCommand("exit");
        RegCommand("reload");
        RegCommand("Ver");
        RegCommand("Version");
        RegCommand("version");
        RegCommand("help");
        System.out.println("Auto backup="+ ReadYaml.readYamlBoolean("./config/config.yml","Config.autoBackup.Enable"));
        LightSK = ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable");
        if (LightSK){
            if (ReadYaml.readYamlString("./config/config.yml","Config.key.key")==null){
                logger.warning("Key is null!Will not start server(key can't be like '123')!");
                LLogger.LogRec("Key is null,Now stopping server.");
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(-1);
            }
            LightSK_Key=ReadYaml.readYamlString("./config/config.yml","Config.key.key");

        }
        else{
            System.err.println("LightSK is not enable,server wont start.");
            System.exit(0);
        }
        sqlServer.con();

        LLogger.LogRec("Server started!");



        while(true){
            if(Status==false) break;
            System.out.print(">");
            Cmd = in.nextLine();
            System.out.println();
            if(Objects.equals(Cmd, "exit")){
                System.out.println("Bye");
                System.exit(0);
            }
            if(Objects.equals(Cmd, "Ver") || Cmd.equals("Version") || Cmd.equals("version")){
                System.out.println("""
                        UTM-Login 1.0 SnapShot
                        By QiLingYuJie-John
                        Welcome!
                        """);
            }
            if(Objects.equals(Cmd, "help")||Objects.equals(Cmd, "?")) {
                System.out.println("""
                        Help menu
                        "exit" stop server
                        "reload" reload server and test mysql again
                        "Ver","Version","version" show server version
                        """);
            }
            if(Objects.equals(Cmd, "reload")) {
                try {
                    // 读取资源文件
                    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.yml");
                    if (inputStream == null) {
                        System.out.println("Can not find config!");
                        LLogger.LogRec("Can not find config!");

                        return;
                    }
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

                        System.out.println("Created config");
                        LLogger.LogRec("Created config");
                        // 创建目标文件夹
                    }
                    // 创建目标文件夹

                } catch (IOException e) {
                    logger.warning("Error while creating config!!!");
                    LLogger.LogRec("Error while creating config!!!");
                    LLogger.LogRec(Arrays.toString(e.getStackTrace()));
                    System.out.println("Cause by: " + e.getCause()+ " " + e.getMessage());

                }

                if (ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable")){
                    if(LightSK!=ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable")){
                        LightSK=ReadYaml.readYamlBoolean("./config/config.yml", "Config.key.enable");
                    }
                    if (ReadYaml.readYamlString("./config/config.yml","Config.key.key")==null){
                        logger.warning("Key is null!Will not start server(key can't be like '123')!Server can't running!");
                        System.out.println("Please input LightSK key in config,then reload again!");
                        LLogger.LogRec("Key is null!");
                        LightSK=false;
                        try {
                            Thread.currentThread().sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    LightSK_Key=ReadYaml.readYamlString("./config/config.yml","Config.key.key");

                }
                else{
                    logger.warning("Error!LightSK is not enable!Server will not run.");
                    System.exit(-1);
                }
                System.out.println("Try to connect mysql server......");
                sqlServer.con();
                LLogger.LogRec("Reloaded the server.");
                System.out.println("Complete!");

            }
            for (String key : hashMap.keySet()) {
                if (Cmd!=null&&!key.equals(Cmd)) {
                    // 如果不包含特定字符串
                    CmdStatus = true;

                }
                else {
                    CmdStatus=false;
                    break;
                }

            }
            if(CmdStatus==true){
                System.out.println("Wrong command: " + Cmd);
                CmdStatus = false;
            }
        }

        // 其他代码逻辑

        // 关闭System.out和System.err的重定向
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));


    }

    public static void RegCommand(String string){
        try {
            hashMap.put(string,true);
        }catch (Exception exception){
            System.out.println("Reg command failed!Because: "+ exception.getMessage());
            LLogger.LogRec("Reg command failed!Because: "+ exception.getMessage());
            exception.printStackTrace();
            throw exception;
        }


    }
}


