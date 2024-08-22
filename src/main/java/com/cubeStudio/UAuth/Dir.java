package com.cubeStudio.UAuth;

import java.io.File;

public class Dir {
    public static void mkdir(String path, String dirName) {
        String dirPath = path + "/" + dirName;
        File newDir = new File(dirPath);
        if (!newDir.exists()) {
            boolean result = newDir.mkdir();
            if (result) {

                LLogger.info("Created dir at" + dirPath + dirName);
            } else {

                LLogger.error("failed to create dir at" + dirPath + dirName);
            }
        } else {
            System.out.println("Created.");
        }
    }

    public static void rmdir(String path, String dirName) {
        String dirPath = path + "/" + dirName;
        File dir = new File(dirPath);
        if (dir.exists()) {
            if (dir.listFiles().length == 0) {
                boolean result = dir.delete();
                if (result) {
                    System.out.println("Remove complete");
                    LLogger.info("Remove dir at " + dirPath + dirName);
                } else {
                    System.out.println("Remove failed");
                    LLogger.error("Error!Can't Remove at" + dirPath + dirName);
                }
            } else {
                System.out.println("Warn:It's not empty!");
                LLogger.warn("Warn!can't remove dir at " + dirPath + dirName + " because it's not empty,please try to remove all!");
            }
        } else {
            System.out.println("Not such a place");
            LLogger.exception("Error!can't found dir at " + dirPath + dirName);
        }
    }

    public static void rmdirAll(String path, String dirName) {
        String dirPath = path + "/" + dirName;
        File dir = new File(dirPath);
        if (dir.exists()) {
            if (dir.listFiles().length > 0) {
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        rmdirAll(dirPath, file.getName());
                    } else {
                        file.delete();
                    }
                }
            }
            boolean result = dir.delete();
            if (result) {
                System.out.println("Remove all!");
                LLogger.info("Remove all! at " + dirPath + dirName);
            } else {
                System.out.println("Remove all failed");
                LLogger.warn("Remove all failed at " + dirPath + dirName);
            }
        } else {
            System.out.println("Can't remove all because we can't found it");
            LLogger.error("Error:Can't remove all because we can't found it at " + dirPath + dirName);
        }
    }

   /* //如何调用？
   public static void main(String[] args) {
        mkdir("C:/example", "folder1");
        rmdir("C:/example", "folder1");
        mkdir("C:/example", "folder2");
        rmdir("C:/example", "folder2");
        mkdir("C:/example", "nestedFolder");
        mkdir("C:/example/nestedFolder", "subFolder");
        rmdirAll("C:/example", "nestedFolder");
    }

    */
}
