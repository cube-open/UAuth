package com.APRT.UTMDB;
import java.util.Scanner;

import org.yaml.snakeyaml.Yaml;

import  java.io.*;
import java.util.logging.Logger;


public class Main {
    Scanner input = new Scanner(System.in);
    Yaml yaml = new Yaml();
    String sourceFilePath = "src/main/resources/config.yml";
    static String destinationFolderPath = "config/";
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try {
            // 读取资源文件
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.yml");
            if (inputStream == null) {
                System.out.println("Can not find config!");
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
        } catch (IOException e) {
            logger.warning("Error while creating config!!!");
            e.printStackTrace();
        }
        System.out.print("自动备份？，="+ ReadYaml.readYamlBoolean("config/config.yml","Config.autoBackup.Enable"));


    }
}