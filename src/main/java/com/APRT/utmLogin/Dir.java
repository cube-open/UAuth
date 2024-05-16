package com.APRT.utmLogin;

import java.io.File;

public class Dir {
    public static void mkdir(String path, String dirName) {
        String dirPath = path + "/" + dirName;
        File newDir = new File(dirPath);
        if (!newDir.exists()) {
            boolean result = newDir.mkdir();
            if (result) {
                System.out.println("Created!");
                LLogger.LogRec("Created dir at" + dirPath+dirName);
            } else {
                System.out.println("Failed");
                LLogger.LogRec("failed to create dir at" + dirPath+dirName);
            }
        } else {
            System.out.println("Already in there");
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
                    LLogger.LogRec("Remove dir at "+dirPath+dirName);
                } else {
                    System.out.println("Remove failed");
                    LLogger.LogRec("Error!Can't Remove at"+dirPath+dirName);
                }
            } else {
                System.out.println("Warn:It's not empty!");
                LLogger.LogRec("Warn!can't remove dir at "+ dirPath +dirName + " because it's not empty,please try to remove all!");
            }
        } else {
            System.out.println("Not such a place");
            LLogger.LogRec("Error!can't found dir at "+ dirPath +dirName);
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
                LLogger.LogRec("Remove all! at " +dirPath + dirName);
            } else {
                System.out.println("Remove all failed");
                LLogger.LogRec("Remove all failed at " +dirPath + dirName);
            }
        } else {
            System.out.println("Can't remove all because we can't found it");
            LLogger.LogRec("Error:Can't remove all because we can't found it at " +dirPath + dirName);
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
