/*
Java实现-启灵域界科技
JsonDB算法基于aztice的JsonDB
 */
package com.APRT.UTMDB;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import com.APRT.UTMDB.LLogger;
import org.yaml.snakeyaml.Yaml;
import com.APRT.UTMDB.Dir;
import com.APRT.UTMDB.LightSK;
import  java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class Main {

    public static boolean Status = true;
    String Cmd;
    String sourceFilePath = "src/main/resources/config.yml";
    static String destinationFolderPath = "config/";
    static Scanner in = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Ctrl+C detected. Program will exit,all data may will broke.");
            Status = false;
        }));
        Map<String, String> env = System.getenv();


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
        Dir.mkdir(".\\","log");
        HashMap<String,Boolean> hashMap = new HashMap<>();
        Boolean CmdStatus = false;
        hashMap.put("exit",true);
        hashMap.put("reload",true);
        hashMap.put("Ver",true);
        hashMap.put("Version",true);
        hashMap.put("logoff",true);
        hashMap.put("version",true);
        LLogger.LogRec("Starting server!");
        Dir.mkdir(".","db");
        String Cmd = "-";
        try {
            // 读取资源文件
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.yml");
            if (inputStream == null) {
                System.out.println("Can not find config!");
                LLogger.LogRec("Can not find config!");
                return;
            }

            // 创建目标文件夹
            File destinationFolder = new File(destinationFolderPath);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            // 写入文件到目标文件夹
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
        } catch (IOException e) {
            logger.warning("Error while creating config!!!");
            LLogger.LogRec("Error while creating config!!!");
            e.printStackTrace();
        }
        System.out.println("Auto backup?,="+ ReadYaml.readYamlBoolean("config/config.yml","Config.autoBackup.Enable"));
        LLogger.LogRec("Server started!");

        while(true){
            if(Status==false) break;
            System.out.print(">");
            Cmd = in.nextLine();
            System.out.println();
            if(Objects.equals(Cmd, "exit")){
                System.out.println("Bye");
                break;
            }
            if(Objects.equals(Cmd, "Ver") || Cmd.equals("Version") || Cmd.equals("version")){
                System.out.println("""
                        UTM-DB 1.0 SnapShot
                        By QiLingYuJie-John
                        Welcome!
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

                    // 创建目标文件夹
                    File destinationFolder = new File(destinationFolderPath);
                    if (!destinationFolder.exists()) {
                        destinationFolder.mkdirs();
                    }

                    // 写入文件到目标文件夹
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
                } catch (IOException e) {
                    logger.warning("Error while creating config!!!");
                    LLogger.LogRec("Error while creating config!!!");
                    e.printStackTrace();

                }
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

    }


}


